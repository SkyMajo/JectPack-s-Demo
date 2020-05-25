package com.skymajo.androidmvvmstydu1.model

import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.w3c.dom.Comment
import java.io.Serializable

/*
                "id": 428,
				"itemId": 1578976510452,
				"itemType": 2,
				"createTime": 1578977844500,
				"duration": 8,
				"feeds_text": "2020他来了，就在眼前了",
				"authorId": 1578919786,
				"activityIcon": null,
				"activityText": "2020新年快乐",
				"width": 960,
				"height": 540,
				"url": "https://pipijoke.oss-cn-hangzhou.aliyuncs.com/New%20Year%20-%2029212-video.mp4",
				"cover": "https://pipijoke.oss-cn-hangzhou.aliyuncs.com/2020%E5%B0%81%E9%9D%A2%E5%9B%BE.png",
 */

@Parcelize
data class Feed(val id:Int,
                val itemId:Long,
                val itemType:Int,
                val createTime:String,
                val duration:Double,
                val feeds_text:String,
                val authorId:Long,
                val activityIcon:String,
                val activityText:String,
                val width:Int,
                val height:Int,
                val url:String,
                val cover:String,
                val author:User,
                val topComment: Commonent,
                val ugc:Ugc
): Serializable, Parcelable {
    override fun equals(other: Any?): Boolean {
        if(other ==null || !(other is Feed))
            return false
        val feed=other as Feed
        return id == feed.id
                &&itemId==feed.itemId
                &&itemType==feed.itemType
                &&createTime==feed.createTime
                &&duration==feed.duration
                &&TextUtils.equals(feeds_text,feed.feeds_text)
                &&authorId==feed.authorId
                &&TextUtils.equals(activityIcon,feed.activityIcon)
                &&TextUtils.equals(activityText,feed.activityText)
                &&width==feed.width
                &&height==feed.height
                &&TextUtils.equals(url,feed.url)
                &&TextUtils.equals(cover,feed.cover)
                &&(author!=null && author.equals(feed.author))
                &&(topComment!=null && topComment.equals(feed.topComment))
                &&(ugc!=null && ugc.equals(feed.ugc))
    }
}