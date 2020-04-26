package com.skymajo.androidmvvmstydu1.model

import com.alibaba.fastjson.annotation.JSONCreator

data class Destination  @JSONCreator constructor(val isFragment: Boolean,
                                                 val asStarter: Boolean,
                                                 val needLogin: Boolean,
                                                 val id: Int,
                                                 val clzName: String,
                                                 val pageUrl: String
)