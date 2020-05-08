package com.skymajo.androidmvvmstydu1.utils

object StringConvert {

    fun ConvertFeedUgc(count :Int):String= if(count<10000){ count.toString() }else{ "${count/1000}k"}



}