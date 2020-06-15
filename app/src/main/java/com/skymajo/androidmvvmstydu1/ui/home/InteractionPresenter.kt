package com.skymajo.androidmvvmstydu1.ui.home

import android.content.Context
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.skymajo.androidmvvmstydu1.model.Feed
import com.skymajo.androidmvvmstydu1.model.User
import com.skymajo.libcommon.ShareDialog
import com.skymajo.androidmvvmstydu1.ui.login.UserManager
import com.skymajo.androidmvvmstydu1.utils.AppGlobals
import com.skymajo.libnetcache.ApiResponse
import com.skymajo.libnetcache.ApiServce
import com.skymajo.libnetcache.JsonCallBack
import org.json.JSONObject

object InteractionPresenter {

    private var URL_TOGGLE_FEED_LIKE:String = "/ugc/toggleFeedLike"
    private var URL_TOGGLE_FEED_DISSLIKE:String = "/ugc/dissFeed"
    private var URL_SHARE = "/ugc/increaseShareCount";

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

    private fun toggleFeedLikeInternal(feed: Feed) {
        ApiServce.get<Any>(URL_TOGGLE_FEED_LIKE)
            .addParam("userId",UserManager.get().userId)
            .addParam("itemId",feed.itemId)
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>?) {
                    super.onSusccess(response)
                    if (response!!.body != null) {
                        var hasLike = response!!.body.getBoolean("hasLiked")
                        feed.ugc.hasLiked = hasLike
                        if (hasLike) {
                            feed.ugc.hasdiss = false
                            feed.ugc.likeCount = feed.ugc.likeCount+1
                        }else{
                            feed.ugc.likeCount = feed.ugc.likeCount-1
                        }
                        feed.ugc.notifyPropertyChanged(BR._all)
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
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>?) {
                    super.onSusccess(response)
                    if (response!!.body != null) {
                        var hasdiss = response!!.body.getBoolean("hasLiked")
                        feed.ugc.hasdiss = hasdiss
                        if (hasdiss) {
                            feed.ugc.hasLiked = false
                        }
                        feed.ugc.notifyPropertyChanged(BR._all)
                    }
                }
            })
    }

    @JvmStatic
    fun openShare(context: Context,feed: Feed){

        var shareDialog =
            ShareDialog(context)
        shareDialog.setShareContent(""""因为啊！
            |梅花声音好听，打游戏又下饭，香喷喷的！
            |又温柔又可爱的！简直棒极了！！"""".trimMargin())
        shareDialog.setShareClickListener {

//            ApiServce.get<com.alibaba.fastjson.JSONObject>(URL_SHARE)
//                .addParam("itemId",feed.itemId)
//                .execute(object : JsonCallBack<JSONObject>() {
//                    override fun onSusccess(response: ApiResponse<JSONObject>) {
//                        super.onSusccess(response)
//                        if (response.body != null){
//                            var count =  response.body.getInt("count")
//                        }
//                    }
//                })

        }
        shareDialog.show()

    }



}