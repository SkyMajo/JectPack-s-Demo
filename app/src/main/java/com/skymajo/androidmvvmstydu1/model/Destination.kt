package com.skymajo.androidmvvmstydu1.model

data class Destination(val isFragment: Boolean = false,
                       val asStarter: Boolean = false,
                       val needLogin: Boolean = false,
                       val id: Int = 0,
                       val clzName: String = "",
                       val pageUrl: String = ""
)