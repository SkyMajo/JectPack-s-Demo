package com.skymajo.androidmvvmstydu1.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.skymajo.androidmvvmstydu1.model.Destination
import java.io.BufferedReader
import java.io.InputStreamReader

object AppConfig {

    var sDestConfig = HashMap<String,Destination>()

    fun getDestinationConfig ():HashMap<String,Destination>{
        if (sDestConfig == null){
          val content = parseFile("NavGraphJson.json")
            sDestConfig = JSON.parseObject<HashMap<String,Destination>>(content, object : TypeReference<java.util.HashMap<String, Destination>>() {}.type)
        }
        return sDestConfig
    }

    private fun parseFile(fileName : String):String{

        var assets = AppGlobals.getApplication()!!.resources.assets
        var stream = assets.open(fileName)
        var reader = BufferedReader(InputStreamReader(stream))
        var line: String
        var buffer = StringBuffer()
        while ((reader.readLine()) != null){
            line = reader.readLine()
            buffer.append(line)
        }
        stream?.close()
        reader?.close()
        return buffer.toString()
    }
}