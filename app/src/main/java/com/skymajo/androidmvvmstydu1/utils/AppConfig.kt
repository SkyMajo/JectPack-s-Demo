package com.skymajo.androidmvvmstydu1.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.google.gson.Gson
import com.skymajo.androidmvvmstydu1.model.BottomBar
import com.skymajo.androidmvvmstydu1.model.Destination
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object AppConfig {

    var sDestConfig:HashMap<String, Destination>? = null
    var sBottomBar : BottomBar? = null

    fun getBottomBarConfig(): BottomBar? {
        if (sBottomBar == null) {
            val content = parseFile("MainTabsJson.json")
            sBottomBar =
                JSON.parseObject(content, BottomBar::class.java)
        }
        return sBottomBar
    }

    open fun getDestinationConfig ():HashMap<String,Destination>{
        if (sDestConfig == null){
          val content = parseFile("NavGraphJson.json")
            sDestConfig = Gson().fromJson<HashMap<String,Destination>>(content,object : TypeReference<java.util.HashMap<String, Destination>>() {}.type)
//            sDestConfig = JSON.parseObject<HashMap<String,Destination>>(content, object : TypeReference<java.util.HashMap<String, Destination>>() {}.type)
        }
        return sDestConfig!!
    }

    private fun parseFile(fileName : String):String{
        val assets = AppGlobals.getApplication()!!.assets
        var `is`: InputStream? = null
        var br: BufferedReader? = null
        val builder = StringBuilder()
        try {
            `is` = assets.open(fileName)
            br = BufferedReader(InputStreamReader(`is`))
            var line: String? = null
            line = br!!.readLine()
            while ((line) != null) {
                builder.append(line)
                line = br!!.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (`is` != null) {
                    `is`!!.close()
                }
                if (br != null) {
                    br!!.close()
                }
            } catch (e: Exception) {

            }

        }

        return builder.toString()
    }
}