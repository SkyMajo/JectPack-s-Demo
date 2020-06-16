package com.skymajo.androidmvvmstydu1.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.alibaba.fastjson.JSONObject
import com.google.gson.Gson
import com.skymajo.androidmvvmstydu1.model.Commonent
import com.skymajo.androidmvvmstydu1.model.Feed
import com.skymajo.androidmvvmstydu1.model.User
import com.skymajo.androidmvvmstydu1.ui.login.UserManager
import com.skymajo.androidmvvmstydu1.utils.AppGlobals
import com.skymajo.libcommon.ShareDialog
import com.skymajo.libnetcache.ApiResponse
import com.skymajo.libnetcache.ApiServce
import com.skymajo.libnetcache.JsonCallBack
import org.w3c.dom.Comment

object InteractionPresenter {

    private const val URL_TOGGLE_FEED_LIKE:String = "/ugc/toggleFeedLike"
    private const val URL_TOGGLE_FEED_DISSLIKE:String = "/ugc/dissFeed"
    private const val URL_SHARE = "/ugc/increaseShareCount";
    private const val URL_TOGGLE_COMMENT_LIKE = "/ugc/toggleCommentLike"

    @JvmStatic
    fun toggleFeedLike(owner: LifecycleOwner,feed: Feed){
        if(!UserManager.get().isLogin){
            var loginLiveData : LiveData<User> = UserManager.get().login(AppGlobals.getApplication())
            loginLiveData.observe(owner,
                object : Observer<User?> {
                    override fun onChanged(user: User?) {
                        if (user != null){
                            toggleFeedLikeInternal(feed)
                        }
                        loginLiveData.removeObserver(this)
                    }
                })
            return
        }
        toggleFeedLikeInternal(feed)
    }


    data class likeData(
       var data:data
    )

    data class data(
        var hasLiked:Boolean
    )

    private fun toggleFeedLikeInternal(feed: Feed) {
        ApiServce.get<Any>(URL_TOGGLE_FEED_LIKE)
            .addParam("userId",UserManager.get().userId)
            .addParam("itemId",feed.itemId)
            .execute(object : JsonCallBack<String>() {
                override fun onSusccess(response: ApiResponse<String>?) {
                    super.onSusccess(response)

                    if (response!!.body != null) {
                        var hasLike = Gson().fromJson<likeData>(response.body,likeData::class.java).data.hasLiked
                        feed.ugc.hasLiked = hasLike
                        if (hasLike) {
                            feed.ugc.hasdiss = false
                            feed.ugc.likeCount = feed.ugc.likeCount+1
                        }else{
                            feed.ugc.likeCount = feed.ugc.likeCount-1
                        }
                        feed.ugc.notifyChange()
                    }
                }
            })
    }

    @JvmStatic
    fun toggleFeedDiss(owner: LifecycleOwner,feed: Feed){
        if(!UserManager.get().isLogin){
            var loginLiveData : LiveData<User> = UserManager.get().login(AppGlobals.getApplication())
            loginLiveData.observe(owner,
                object : Observer<User?> {
                    override fun onChanged(user: User?) {
                        if (user != null){
                            toggleFeedDissInternal(feed)
                        }
                        loginLiveData.removeObserver(this)
                    }
                })
            return
        }
        toggleFeedDissInternal(feed)
    }


    private fun toggleFeedDissInternal(feed: Feed) {
        ApiServce.get<Any>(URL_TOGGLE_FEED_DISSLIKE)
            .addParam("userId",UserManager.get().userId)
            .addParam("itemId",feed.itemId)
            .execute(object : JsonCallBack<String>() {
                override fun onSusccess(response: ApiResponse<String>?) {
                    super.onSusccess(response)
                    if (response!!.body != null) {
                        var hasLike = Gson().fromJson<likeData>(response.body,likeData::class.java).data.hasLiked
                        feed.ugc.hasdiss = hasLike
                        if (hasLike) {
                            feed.ugc.hasLiked = false
                        }
                        feed.ugc.notifyChange()
                    }
                }
            })
    }

    @JvmStatic
    fun openShare(context: Context?,feed: Feed){
        val url = "http://h5.aliyun.ppjoke.com/item/%s?timestamp=%s&user_id=%s"
        val shareContent = feed.feeds_text
        Log.e("InteractionPresenter", "LifecycleOwner is null?:" + (context == null))
        var shareDialog =
            ShareDialog(context!!)
//        shareDialog.setShareContent(""""因为啊！
//            |梅花声音好听，打游戏又下饭，香喷喷的！
//            |又温柔又可爱的！简直棒极了！！"""".trimMargin())
        shareDialog.setShareContent(shareContent)
        shareDialog.setShareClickListener {


            ApiServce.get<Any>(URL_SHARE)
                .addParam("itemId", feed.itemId)
                .execute(object : JsonCallBack<JSONObject?>() {
                    fun onSuccess(response: ApiResponse<JSONObject?>) {
                        if (response.body != null) {
                            val count = response.body!!.getIntValue("count")
                            feed.ugc.shareCount = count
                        }
                    }

                    override fun onError(response: ApiResponse<JSONObject?>?) {
                        super.onError(response)
                        InteractionPresenter.showToast(response!!.message)
                    }
                })

        }
        shareDialog.show()

    }


    //给一个帖子的评论点赞/取消点赞
    fun toggleCommentLike(owner: LifecycleOwner?, comment: Commonent) {
        if (!isLogin(owner,
                Observer { (id, userId, name, avatar, description, likeCount, topCommentCount, followCount, followerCount, qqOpenId, expires_time, score, historyCount, commentCount, favoriteCount, feedCount, hasFollow) ->
                    InteractionPresenter.toggleCommentLikeInternal(
                        comment
                    )
                })
        ) {
        } else {
            InteractionPresenter.toggleCommentLikeInternal(comment)
        }
    }

    private fun toggleCommentLikeInternal(comment: Commonent) {
        ApiServce.get<Any>(InteractionPresenter.URL_TOGGLE_COMMENT_LIKE)
            .addParam("commentId", comment.commentId)
            .addParam("userId", UserManager.get().userId)
            .execute(object : JsonCallBack<JSONObject?>() {
                fun onSuccess(response: ApiResponse<JSONObject?>) {
                    if (response.body != null) {
                        val hasLiked = response.body!!.getBooleanValue("hasLiked")
                        comment.ugc.hasLiked = hasLiked
                    }
                }

                override fun onError(response: ApiResponse<JSONObject?>?) {
                    super.onError(response)
                    showToast(response!!.message)
                }


            })
    }


    private fun isLogin(
        owner: LifecycleOwner?,
        observer: Observer<User>
    ): Boolean {
        return if (UserManager.get().isLogin) {
            true
        } else {
            val liveData = UserManager.get().login(AppGlobals.getApplication())
            if (owner == null) {
                liveData.observeForever(InteractionPresenter.loginObserver(observer, liveData))
            } else {
                liveData.observe(owner, InteractionPresenter.loginObserver(observer, liveData))
            }
            false
        }
    }

    private fun loginObserver(
        observer: Observer<User>?,
        liveData: LiveData<User>
    ): Observer<User?> {
        return object : Observer<User?> {
            override fun onChanged(user: User?) {
                liveData.removeObserver(this)
                if (user != null && observer != null) {
                    observer.onChanged(user)
                }
            }
        }
    }


    @SuppressLint("RestrictedApi")
    private fun showToast(message: String) {
        ArchTaskExecutor.getMainThreadExecutor().execute {
            Toast.makeText(AppGlobals.getApplication(), message, Toast.LENGTH_SHORT).show()
        }
    }



}