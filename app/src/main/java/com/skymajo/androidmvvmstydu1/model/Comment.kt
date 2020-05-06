package com.skymajo.androidmvvmstydu1.model

data class Comment(
    var id:Int,
    var itemId:Int,
    var commentId :Int,
    var userId : Int,
    var commentType:Int,
    var createTime : Int,
    var commentCount:Int,
    var likeCount:Int,
    var commentText:String,
    var imageUrl:String,
    var videoUrl:String,
    var width:Int,
    var height:Int,
    var hasLiked:Boolean,
    var author:User,
    var ugc: Ugc

)