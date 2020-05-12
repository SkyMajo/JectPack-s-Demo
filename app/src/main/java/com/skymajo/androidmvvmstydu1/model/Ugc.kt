package com.skymajo.androidmvvmstydu1.model

data class Ugc(
    var likeCount:Int,
    var shareCount:Int,
    var commentCount:Int,
    var hasFavorite:Boolean,
    var hasLiked:Boolean,
    var hasdiss:Boolean,
    var hasDissed:Boolean
){
    override fun equals(other: Any?): Boolean {
        var ugc = other as Ugc
        return (ugc.likeCount == likeCount
                && ugc.shareCount == shareCount
                && ugc.commentCount == commentCount
                && ugc.hasFavorite == hasFavorite
                )
    }
}
