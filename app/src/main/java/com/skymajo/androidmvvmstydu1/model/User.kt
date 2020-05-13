package com.skymajo.androidmvvmstydu1.model

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.android.parcel.Parcelize


/*
{
					"id": 1250,
					"userId": 1578919786,
					"name": "、蓅哖╰伊人为谁笑",
					"avatar": "http://qzapp.qlogo.cn/qzapp/101794421/FE41683AD4ECF91B7736CA9DB8104A5C/100",
					"description": "这是一只神秘的jetpack",
					"likeCount": 3,
					"topCommentCount": 0,
					"followCount": 0,
					"followerCount": 2,
					"qqOpenId": "FE41683AD4ECF91B7736CA9DB8104A5C",
					"expires_time": 1586695789903,
					"score": 0,
					"historyCount": 222,
					"commentCount": 9,
					"favoriteCount": 0,
					"feedCount": 0,
					"hasFollow": false
				}
 */
@Parcelize
data class User(
    val id:Int,
    val userId:Long,
    val name:String,
    val avatar:String,
    val description:String,
    val likeCount:Int,
    val topCommentCount:Int,
    val followCount:Int,
    val followerCount:Int,
    val qqOpenId:Long,
    val expires_time:Int,
    val score:Int,
    val historyCount:Int,
    val commentCount:Int,
    val favoriteCount:Int,
    val feedCount:Int,
    val hasFollow:Boolean
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if(other==null || !(other is User))
            return false
        var newUser= other as User
        return TextUtils.equals(name,newUser.name)
                && TextUtils.equals(avatar,newUser.avatar)
                && TextUtils.equals(description,newUser.description)
                &&likeCount==newUser.likeCount
                &&topCommentCount==newUser.topCommentCount
                &&followCount==newUser.followCount
                &&followerCount==newUser.followerCount
                &&qqOpenId==newUser.qqOpenId
                &&expires_time==newUser.expires_time
                &&score==newUser.score
                &&historyCount==newUser.historyCount
                &&commentCount==newUser.commentCount
                &&favoriteCount==newUser.favoriteCount
                &&feedCount==newUser.feedCount
                &&hasFollow==newUser.hasFollow
    }
}
