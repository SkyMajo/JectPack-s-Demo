package com.skymajo.androidmvvmstydu1.utils

import android.content.ComponentName
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

class NavGraphBuilder {
    fun builder(navController: NavController){
        var navigatorProvider = navController.navigatorProvider
        var fragmentNavigator =
            navigatorProvider.getNavigator<FragmentNavigator>(FragmentNavigator::class.java)

        var activityNavigator =
            navigatorProvider.getNavigator<ActivityNavigator>(ActivityNavigator::class.java)

        var navGraph = navController.graph

        var sDestConfig = AppConfig.sDestConfig
        for (value in sDestConfig.values) {
            if (value.isFragment) {
                var fragmentDestination = fragmentNavigator.createDestination()
                fragmentDestination.id = value.id
                fragmentDestination.addDeepLink(value.pageUrl)
                fragmentDestination.className = value.clzName

                navGraph.addDestination(fragmentDestination)
            }else{
                var activityDestination = activityNavigator.createDestination()
                activityDestination.id = value.id
                activityDestination.addDeepLink(value.pageUrl)
                activityDestination.setComponentName(ComponentName(AppGlobals.getApplication()!!.packageName,value.clzName))

                navGraph.addDestination(activityDestination)
            }

            if (value.asStarter){
                navGraph.startDestination = value.id
            }
        }
            navController.setGraph(navGraph)
    }
}