package com.skymajo.androidmvvmstydu1.model

import com.alibaba.fastjson.annotation.JSONCreator

data class Tabs @JSONCreator constructor(
    var size : Int,
    var enable : Boolean,
    var index : Int,
    var pageUrl:String,
    var title :String,
    var tintColor:String
)