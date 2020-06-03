package com.skymajo.androidmvvmstydu1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/*
"topComment": {
					"id": 1126,
					"itemId": 1578976510452,
					"commentId": 1579007787804000,
					"userId": 1578919786,
					"commentType": 1,
					"createTime": 1579007787804,
					"commentCount": 0,
					"likeCount": 1001,
					"commentText": "2020他来了，就在眼前了~Happy New Year",
					"imageUrl": "",
					"videoUrl": "",
					"width": 0,
					"height": 0,
					"hasLiked": false,
					"author": {
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
					},
					"ugc": {
						"likeCount": 103,
						"shareCount": 10,
						"commentCount": 10,
						"hasFavorite": false,
						"hasLiked": false,
						"hasdiss": false,
						"hasDissed": false
					}
				}
 */

@Parcelize
data class Commonent(val id:String,
                     val itemId:String,
                     val commentId:String,
                     val userId:String,
                     val commentType:Int,
                     val createTime:String,
                     val commentCount:String,
                     val likeCount:Int,
                     val commentText:String,
                     val imageUrl:String,
                     val videoUrl:String,
                     val width:Int,
                     val height:Int,
                     val hasLiked:Boolean,
                     val author:User,
                     val ugc:Ugc

):Serializable, Parcelable{
    override fun equals(other: Any?): Boolean {
        if(other==null || !(other is Common))
            return false
        var newCommon= other as Common
        return likeCount == newCommon.likeCount && hasLiked == newCommon.hasLiked && author!=null && author.equals(newCommon.author) && ugc!=null && ugc.equals(newCommon.ugc)
    }
}