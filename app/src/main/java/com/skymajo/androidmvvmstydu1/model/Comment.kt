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

){
    override fun equals(other: Any?): Boolean {
        var comment = other as Comment
        return (comment.id == id
                && comment.itemId == itemId
                && comment.userId == userId
                && comment.commentType == commentType
                && comment.createTime == createTime
                && comment.commentCount == commentCount
                && comment.likeCount == likeCount
                && comment.commentText == commentText
                && comment.imageUrl == imageUrl
                && comment.videoUrl == videoUrl
                && comment.width == width
                && comment.height == height
                && comment.hasLiked == hasLiked
                && comment.author == author
                && comment.ugc == ugc)
    }
}