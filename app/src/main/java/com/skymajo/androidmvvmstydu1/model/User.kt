package com.skymajo.androidmvvmstydu1.model

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("followCount")
                  val followCount: Int,
                @SerializedName("hasFollow")
                  val hasFollow: Boolean,
                @SerializedName("qqOpenId")
                  val qqOpenId: String,
                @SerializedName("feedCount")
                  val feedCount: Int,
                @SerializedName("description")
                  val description: String,
                @SerializedName("likeCount")
                  val likeCount: Int,
                @SerializedName("avatar")
                  val avatar: String,
                @SerializedName("userId")
                  val userId: Int,
                @SerializedName("commentCount")
                  val commentCount: Int,
                @SerializedName("topCommentCount")
                  val topCommentCount: Int,
                @SerializedName("score")
                  val score: Int,
                @SerializedName("name")
                  val name: String,
                @SerializedName("id")
                  val id: Int,
                @SerializedName("historyCount")
                  val historyCount: Int,
                @SerializedName("followerCount")
                  val followerCount: Int,
                @SerializedName("expires_time")
                  val expiresTime: Long,
                @SerializedName("favoriteCount")
                  val favoriteCount: Int){
        override fun equals(other: Any?): Boolean {
            var user = other as User
            return (user.followCount == followCount
                    &&user.hasFollow == hasFollow
                    &&user.qqOpenId == qqOpenId
                    &&user.feedCount == feedCount
                    &&user.description == description
                    &&user.likeCount == likeCount
                    &&user.avatar == avatar
                    &&user.commentCount == commentCount
                    &&user.topCommentCount == topCommentCount
                    &&user.score == score
                    &&user.name == name
                    &&user.id == id
                    &&user.historyCount == historyCount
                    &&user.followerCount == followerCount
                    &&user.expiresTime == expiresTime
                    &&user.favoriteCount ==favoriteCount)
    }
}