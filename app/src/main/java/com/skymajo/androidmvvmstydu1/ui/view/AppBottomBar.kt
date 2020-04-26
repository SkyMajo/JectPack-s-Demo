package com.skymajo.androidmvvmstydu1.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.skymajo.androidmvvmstydu1.R
import com.skymajo.androidmvvmstydu1.model.BottomBar
import com.skymajo.androidmvvmstydu1.utils.AppConfig

class AppBottomBar @SuppressLint("RestrictedApi")
constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    BottomNavigationView(context, attrs, defStyleAttr) {

    var drableList = arrayListOf<Int>(R.mipmap.drable_sofa,R.mipmap.drable_sofa,R.mipmap.drable_user)


    @SuppressLint("RestrictedApi")
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {


        var bottomBar = AppConfig.getBottomBar()
        var tabs = bottomBar!!.tabs
        var statesA = arrayOfNulls<IntArray>(2)

        statesA[0] = intArrayOf()
        statesA[1] = intArrayOf()

        var colors = intArrayOf(Color.parseColor(bottomBar.activeColor),Color.parseColor(bottomBar.inActiveColor))
        var colorStateList = ColorStateList(statesA, colors)
        itemIconTintList = colorStateList
        itemTextColor = colorStateList
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        selectedItemId = bottomBar!!.selectTabs
        for (tab in tabs) {
            if(!tab.enable){
                return
            }
            var id = getId(tab.pageUrl)
            if (id<0){
                return
            }
            var item = menu.add(0, id, tab.index, tab.title)
            Log.e("MaxItemCount", maxItemCount.toString())
            item.setIcon(drableList[tab.index])
        }
        for (tab in tabs) {
            var iconSize = dp2px(tab.size)
            var bottomNavigationMenuView =
                getChildAt(0) as BottomNavigationMenuView
            var bottomNavigationItemView: BottomNavigationItemView? =
                bottomNavigationMenuView.getChildAt(tab.index) as BottomNavigationItemView? ?: break
            bottomNavigationItemView!!.setIconSize(iconSize)
            if (tab.title.isNotEmpty()) {
                bottomNavigationItemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)))
                bottomNavigationItemView.setShifting(false)
            }
        }

    }

    private fun dp2px(size: Int): Int = (context.resources.displayMetrics.density*size+0.5f).toInt()


    private fun getId(pageUrl: String):Int = AppConfig.getDestinationConfig()[pageUrl]?.id ?: -1


}
