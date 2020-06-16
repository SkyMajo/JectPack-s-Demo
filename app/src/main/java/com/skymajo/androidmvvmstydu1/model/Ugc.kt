package com.skymajo.androidmvvmstydu1.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/*
"ugc": {
						"likeCount": 103,
						"shareCount": 10,
						"commentCount": 10,
						"hasFavorite": false,
						"hasLiked": false,
						"hasdiss": false,
						"hasDissed": false
					}
 */
@Parcelize
data class Ugc(var likeCount:Int,
               var shareCount:Int,
               val commentCount:Int,
               val hasFavorite:Boolean,
               @Bindable var hasLiked:Boolean,
               var hasdiss:Boolean
//               val hasDissed:Boolean
): BaseObservable(), Serializable, Parcelable{
    override fun equals(other: Any?): Boolean {
        if(other==null || !(other is Ugc))
            return false
        var newUgc=other as Ugc
        return likeCount==newUgc.likeCount
                &&shareCount==newUgc.shareCount
                &&commentCount==newUgc.commentCount
                &&hasFavorite==newUgc.hasFavorite
                &&hasLiked==newUgc.hasLiked
                &&hasdiss==newUgc.hasdiss
//                &&hasDissed==newUgc.hasDissed
    }
}