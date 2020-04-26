package com.skymajo.libnavannotation

//作用在Class上
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class ActivityDestination(
    val pathUrl: String,
    val needLogin: Boolean = false,
    val asStarter: Boolean = false
)
