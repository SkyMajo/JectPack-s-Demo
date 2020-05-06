package com.skymajo.androidmvvmstydu1.model

data class Feed(
    var id:Int,
    var itemId:Long,
    var itemType:Int,
    var createTime:Int,
    var duration:Double,
    var feeds_text:String,
    var authorId:Long,
    var activityIcon:String,
    var activityText : String,
    var width : Int,
    var height:Int,
    var url:String,
    var cover:String,
    var author:User,
    var comment: Comment,
    var ugc: Ugc
)