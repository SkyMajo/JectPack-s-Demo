package com.skymajo.libnavannotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class ActivityDestination(
    val pathUrl: String,
    val needLogin: Boolean = false,
    val asStarter: Boolean = false
)
