package com.skymajo.libnavcompile


import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.google.auto.service.AutoService
import com.skymajo.libnavannotation.ActivityDestination
import com.skymajo.libnavannotation.FragmentDestination

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.HashMap

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(
    "com.skymajo.libnavannotation.FragmentDestination",
    "com.skymajo.libnavannotation.ActivityDestination"
)
class NavProcessor : AbstractProcessor() {
    private var messager: Messager? = null
    private var filer: Filer? = null

    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        //打印日志信息
        messager = processingEnvironment.messager
        filer = processingEnvironment.filer
    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        val fragmentElements =
            roundEnvironment.getElementsAnnotatedWith(FragmentDestination::class.java)
        val activityElements =
            roundEnvironment.getElementsAnnotatedWith(ActivityDestination::class.java)
        if (!fragmentElements.isEmpty() || !activityElements.isEmpty()) {
            val destMap = HashMap<String, JSONObject>()
            handleDestination(fragmentElements, FragmentDestination::class.java, destMap)
            handleDestination(activityElements, ActivityDestination::class.java, destMap)
            var outputStream: FileOutputStream? = null
            var writer: OutputStreamWriter? = null
            try {
                val resource =
                    filer!!.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME)
                val path = resource.toUri().path
                messager!!.printMessage(Diagnostic.Kind.NOTE, path.toString())
                val appPath = path.substring(0, path.indexOf("app") + 4)
                val asstesPath = "$appPath/src/main/res/assets/"
                val file = File(asstesPath)
                if (!file.exists()) {
                    file.mkdirs()
                }
                val outPutFile = File(file, OUTPUT_FILE_NAME)
                if (outPutFile.exists()) {
                    outPutFile.delete()
                }
                outPutFile.createNewFile()
                val content = JSON.toJSONString(destMap)
                outputStream = FileOutputStream(outPutFile)
                writer = OutputStreamWriter(outputStream!!)
                writer.write(content)
                writer.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (writer != null) {
                    try {
                        writer.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                if (outputStream != null) {
                    try {
                        outputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

        }

        return true
    }

    private fun handleDestination(
        elements: Set<Element>,
        annotationClz: Class<out Annotation>,
        destMap: HashMap<String, JSONObject>
    ) {
        for (element in elements) {
            val typeElement = element as TypeElement
            var pageUrl: String? = null
            val clazName = typeElement.qualifiedName.toString()
            val id = Math.abs(clazName.hashCode())
            var needLogin = false
            var asStarter = false
            var isFragment = false

            val annotation = typeElement.getAnnotation<Annotation>(annotationClz as Class<Annotation>?)
            if (annotation is FragmentDestination) {
                asStarter = annotation.asStarter
                needLogin = annotation.needLogin
                pageUrl = annotation.pathUrl
                isFragment = true
            } else if (annotation is ActivityDestination) {
                asStarter = annotation.asStarter
                needLogin = annotation.needLogin
                pageUrl = annotation.pathUrl
                isFragment = false
            }

            if (destMap.containsKey(pageUrl)) {
                messager!!.printMessage(
                    Diagnostic.Kind.ERROR,
                    "不同的页面，不允许拥有相同的Url\n Different pages cant have same pageUrl!\n $clazName"
                )
            } else {
                val jsonObj = JSONObject()
                jsonObj["id"] = id
                jsonObj["needLogin"] = needLogin
                jsonObj["asStarter"] = asStarter
                jsonObj["isFragment"] = isFragment
                jsonObj["clzName"] = clazName
                jsonObj["pageUrl"] = pageUrl
                destMap[pageUrl!!] = jsonObj
            }

        }

    }

    companion object {

        private val OUTPUT_FILE_NAME = "NavGraphJson.json"
    }
}
