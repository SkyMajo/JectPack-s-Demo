package com.skymajo.androidmvvmstydu1.model

import com.alibaba.fastjson.annotation.JSONCreator

data class BottomBar (
    var activeColor:String,
    var inActiveColor:String,
    var selectTabs: Int,
    var tabs:List <Tabs>

)

