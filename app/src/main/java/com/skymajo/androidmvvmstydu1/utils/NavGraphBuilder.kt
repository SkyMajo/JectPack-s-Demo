package com.skymajo.androidmvvmstydu1.utils

import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator

class NavGraphBuilder {
    fun builder(navController: NavController,activity:FragmentActivity,containerId:Int){
        var navigatorProvider = navController.navigatorProvider
        var fragmentNavigator = FixFragmentNavigation(activity,activity.supportFragmentManager,containerId)
//            navigatorProvider.getNavigator<FragmentNavigator>(FragmentNavigator::class.java)
        navigatorProvider.addNavigator(fragmentNavigator)
        var activityNavigator =
            navigatorProvider.getNavigator<ActivityNavigator>(ActivityNavigator::class.java)

        var navGraph = NavGraph(NavGraphNavigator(navigatorProvider))

//        var navGraph = navController.graph

        var sDestConfig = AppConfig.getDestinationConfig()

        for (value in sDestConfig.values) {
            var node = value
            if (node.isFragment) {
                var fragmentDestination = fragmentNavigator.createDestination()
                fragmentDestination.id = node.id
                fragmentDestination.addDeepLink(node.pageUrl)
                fragmentDestination.className = node.clzName

                navGraph.addDestination(fragmentDestination)
            }else{
                var activityDestination = activityNavigator.createDestination()
                activityDestination.id = node.id
                activityDestination.addDeepLink(node.pageUrl)
                activityDestination.setComponentName(ComponentName(AppGlobals.getApplication()!!.packageName,node.clzName))

                navGraph.addDestination(activityDestination)
            }
            if (node.asStarter){
                navGraph.startDestination = node.id
            }
        }
        navController.graph = navGraph
    }
}