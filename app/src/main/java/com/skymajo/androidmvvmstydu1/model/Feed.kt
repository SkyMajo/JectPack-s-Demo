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
){
    override fun equals(other: Any?): Boolean {
        var feed = other as Feed
        return (feed.id == id
                && feed.itemType == itemType
                && feed.createTime == createTime
                && feed.duration == duration
                && feed.feeds_text == feeds_text
                && feed.authorId == authorId
                && feed.activityIcon == activityIcon
                && feed.activityText == activityText
                && feed.width == width
                && feed.height == height
                && feed.url == url
                && feed.cover == cover
                && feed.author == author
                && feed.comment == comment
                && feed.ugc == ugc)
    }
}