package com.skymajo.androidmvvmstydu1.ui.home

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.alibaba.fastjson.JSONObject
import com.skymajo.androidmvvmstydu1.model.Commonent
import com.skymajo.androidmvvmstydu1.model.Feed
import com.skymajo.androidmvvmstydu1.model.User
import com.skymajo.androidmvvmstydu1.ui.login.UserManager
import com.skymajo.androidmvvmstydu1.utils.AppGlobals.getApplication
import com.skymajo.libcommon.ShareDialog
import com.skymajo.libnetcache.ApiResponse
import com.skymajo.libnetcache.ApiServce
import com.skymajo.libnetcache.JsonCallBack

object InteractionPresenter2 {
    const val DATA_FROM_INTERACTION = "data_from_interaction"
    private const val URL_TOGGLE_FEED_LIK = "/ugc/toggleFeedLike"
    private const val URL_TOGGLE_FEED_DISS = "/ugc/dissFeed"
    private const val URL_SHARE = "/ugc/increaseShareCount"
    private const val URL_TOGGLE_COMMENT_LIKE = "/ugc/toggleCommentLike"

    //给一个帖子点赞/取消点赞，它和给帖子点踩一踩是互斥的
    @JvmStatic
    fun toggleFeedLike(owner: LifecycleOwner?, feed: Feed) {
        if (!isLogin(
                owner,
                Observer { toggleFeedLikeInternal(feed) })
        ) {
        } else {
            toggleFeedLikeInternal(feed)
        }
    }

    private fun toggleFeedLikeInternal(feed: Feed) {
        ApiServce.get<Any>(URL_TOGGLE_FEED_LIK)
            .addParam("userId", UserManager.get().userId)
            .addParam("itemId", feed.itemId)
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>) {
                    if (response.body != null) {
                        val hasLiked: Boolean =
                            response.body.getBoolean("hasLiked")
                        feed.getUgc().isHasLiked = hasLiked
                        //                            LiveDataBus.get().with(DATA_FROM_INTERACTION)
//                                    .postValue(feed);
                    }
                }

                override fun onError(response: ApiResponse<JSONObject>) {
//                        showToast(response.message);
                }
            })
    }

    //给一个帖子点踩一踩/取消踩一踩,它和给帖子点赞是互斥的
    @JvmStatic
    fun toggleFeedDiss(owner: LifecycleOwner?, feed: Feed) {
        if (!isLogin(
                owner,
                Observer { toggleFeedDissInternal(feed) })
        ) {
        } else {
            toggleFeedDissInternal(feed)
        }
    }

    private fun toggleFeedDissInternal(feed: Feed) {
        ApiServce.get<Any>(URL_TOGGLE_FEED_DISS)
            .addParam("userId", UserManager.get().userId)
            .addParam("itemId", feed.itemId)
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>) {
                    if (response.body != null) {
                        val hasLiked: Boolean =
                            response.body.getBoolean("hasLiked")
                        feed.getUgc().isHasdiss = hasLiked
                    }
                }

                override fun onError(response: ApiResponse<JSONObject>) {
//                        showToast(response.message);
                }
            })
    }

    //打开分享面板
    @JvmStatic
    fun openShare(context: Context?, feed: Feed) {
        val url = "http://h5.aliyun.ppjoke.com/item/%s?timestamp=%s&user_id=%s"
        var shareContent = feed.feeds_text
        if (!TextUtils.isEmpty(feed.url)) {
            shareContent = feed.url
        } else if (!TextUtils.isEmpty(feed.cover)) {
            shareContent = feed.cover
        }
        val shareDialog = ShareDialog(context!!)
        shareDialog.setShareContent(shareContent)
        shareDialog.setShareClickListener {
            ApiServce.get<Any>(URL_SHARE)
                .addParam("itemId", feed.itemId)
                .execute(object : JsonCallBack<JSONObject>() {
                    override fun onSusccess(response: ApiResponse<JSONObject>) {
                        if (response.body != null) {
                            val count = response.body.getIntValue("count")
                            feed.getUgc().setShareCount(count)
                        }
                    }

                    override fun onError(response: ApiResponse<JSONObject>) {
//                                showToast(response.message);
                    }
                })
        }
        shareDialog.show()
    }

    //给一个帖子的评论点赞/取消点赞
    fun toggleCommentLike(owner: LifecycleOwner?, comment: Commonent) {
        if (!isLogin(
                owner,
                Observer { toggleCommentLikeInternal(comment) })
        ) {
        } else {
            toggleCommentLikeInternal(comment)
        }
    }

    private fun toggleCommentLikeInternal(comment: Commonent) {
        ApiServce.get<Any>(URL_TOGGLE_COMMENT_LIKE)
            .addParam("commentId", comment.commentId)
            .addParam(
                "userId",
                UserManager.get().userId
            )
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>) {
                    if (response.body != null) {
                        val hasLiked = response.body.getBooleanValue("hasLiked")
                        comment.getUgc().isHasLiked = hasLiked
                    }
                }

                override fun onError(response: ApiResponse<JSONObject>) {
//                        showToast(response.message);
                }
            })
    }

    //收藏/取消收藏一个帖子
    fun toggleFeedFavorite(owner: LifecycleOwner?, feed: Feed) {
        if (!isLogin(
                owner,
                Observer { toggleFeedFavorite(feed) })
        ) {
        } else {
            toggleFeedFavorite(feed)
        }
    }

    private fun toggleFeedFavorite(feed: Feed) {
        ApiServce.get<Any>("/ugc/toggleFavorite")
            .addParam("itemId", feed.itemId)
            .addParam(
                "userId",
                UserManager.get().userId
            )
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>) {
                    if (response.body != null) {
                        val hasFavorite =
                            response.body.getBooleanValue("hasFavorite")
                        feed.getUgc().isHasFavorite = hasFavorite
                        //                            LiveDataBus.get().with(DATA_FROM_INTERACTION)
//                                    .postValue(feed);
                    }
                }

                override fun onError(response: ApiResponse<JSONObject>) {
//                        showToast(response.message);
                }
            })
    }

    //关注/取消关注一个用户
    fun toggleFollowUser(owner: LifecycleOwner?, feed: Feed) {
        if (!isLogin(
                owner,
                Observer { toggleFollowUser(feed) })
        ) {
        } else {
            toggleFollowUser(feed)
        }
    }

    private fun toggleFollowUser(feed: Feed) {
        ApiServce.get<Any>("/ugc/toggleUserFollow")
            .addParam(
                "followUserId",
                UserManager.get().userId
            )
            .addParam("userId", feed.author.userId)
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>) {
                    if (response.body != null) {
                        val hasFollow = response.body.getBooleanValue("hasLiked")
                        feed.getAuthor().isHasFollow = hasFollow
                        //                            LiveDataBus.get().with(DATA_FROM_INTERACTION)
//                                    .postValue(feed);
                    }
                }

                override fun onError(response: ApiResponse<JSONObject>) {
//                        showToast(response.message);
                }
            })
    }

    fun deleteFeed(
        context: Context?,
        itemId: Long
    ): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        AlertDialog.Builder(context!!)
            .setNegativeButton(
                "删除"
            ) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                deleteFeedInternal(liveData, itemId)
            }.setPositiveButton(
                "取消"
            ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }.setMessage("确定要删除这条评论吗？")
            .create().show()
        return liveData
    }

    private fun deleteFeedInternal(
        liveData: MutableLiveData<Boolean>,
        itemId: Long
    ) {
        ApiServce.get<Any>("/feeds/deleteFeed")
            .addParam("itemId", itemId)
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>) {
                    if (response.body != null) {
                        val success = response.body.getBoolean("result")
                        liveData.postValue(success)
                        //                            showToast("删除成功");
                    }
                }

                override fun onError(response: ApiResponse<JSONObject>) {
//                        showToast(response.message);
                }
            })
    }

    //删除某个帖子的一个评论
    fun deleteFeedComment(
        context: Context?,
        itemId: Long,
        commentId: Long
    ): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        AlertDialog.Builder(context!!)
            .setNegativeButton(
                "删除"
            ) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                deleteFeedCommentInternal(liveData, itemId, commentId)
            }.setPositiveButton(
                "取消"
            ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }.setMessage("确定要删除这条评论吗？")
            .create().show()
        return liveData
    }

    private fun deleteFeedCommentInternal(
        liveData: LiveData<*>,
        itemId: Long,
        commentId: Long
    ) {
        ApiServce.get<Any>("/comment/deleteComment")
            .addParam(
                "userId",
                UserManager.get().userId
            )
            .addParam("commentId", commentId)
            .addParam("itemId", itemId)
            .execute(object : JsonCallBack<JSONObject>() {
                override fun onSusccess(response: ApiResponse<JSONObject>) {
                    if (response.body != null) {
                        val result = response.body.getBooleanValue("result")
//                        (liveData as MutableLiveData<*>).postValue(result)
                        ////                            showToast("评论删除成功");
                    }
                }

                override fun onError(response: ApiResponse<JSONObject>) {
////                        showToast(response.message);
                }
            })
    }

    //
    //    //关注/取消关注一个帖子标签
    //    public static void toggleTagLike(LifecycleOwner owner, TagList tagList) {
    //        if (!isLogin(owner, new Observer<User>() {
    //            @Override
    //            public void onChanged(User user) {
    //                toggleTagLikeInternal(tagList);
    //            }
    //        })) ;
    //        else {
    //            toggleTagLikeInternal(tagList);
    //        }
    //    }
    //
    //    private static void toggleTagLikeInternal(TagList tagList) {
    //        ApiService.get("/tag/toggleTagFollow")
    //                .addParam("tagId", tagList.tagId)
    //                .addParam("userId", UserManager.get().getUserId())
    //                .execute(new JsonCallback<JSONObject>() {
    //                    @Override
    //                    public void onSuccess(ApiResponse<JSONObject> response) {
    //                        if (response.body != null) {
    //                            Boolean follow = response.body.getBoolean("hasFollow");
    //                            tagList.setHasFollow(follow);
    //                        }
    //                    }
    //
    //                    @Override
    //                    public void onError(ApiResponse<JSONObject> response) {
    ////                        showToast(response.message);
    //                    }
    //                });
    //    }
    //
    ////    private static void showToast(String message) {
    //        ArchTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
    //            @Override
    //            public void run() {
    //                Toast.makeText(AppGlobals.getApplication(), message, Toast.LENGTH_SHORT).show();
    //            }
    //        });
    //    }
    private fun isLogin(
        owner: LifecycleOwner?,
        observer: Observer<User>
    ): Boolean {
        return if (UserManager.get().isLogin) {
            true
        } else {
            val liveData =
                UserManager.get()
                    .login(getApplication())
            if (owner == null) {
                liveData.observeForever(loginObserver(observer, liveData))
            } else {
                liveData.observe(owner, loginObserver(observer, liveData))
            }
            false
        }
    }

    private fun loginObserver(
        observer: Observer<User>?,
        liveData: LiveData<User>
    ): Observer<User> {
        return object : Observer<User> {
            override fun onChanged(user: User) {
                liveData.removeObserver(this)
                if (user != null && observer != null) {
                    observer.onChanged(user)
                }
            }
        }
    }
}