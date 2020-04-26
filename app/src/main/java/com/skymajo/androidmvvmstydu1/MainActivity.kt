package com.skymajo.androidmvvmstydu1

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.skymajo.androidmvvmstydu1.utils.NavGraphBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        var of =
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        val appBarConfiguration = AppBarConfiguration(of)
//        setupActionBarWithNavController(navCont   roller, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        NavGraphBuilder().builder(navController,this,nav_host_fragment.id)
        nav_view.setOnNavigationItemSelectedListener{
            navController.navigate(it.itemId)
            it.title.isNotEmpty()
        }
    }


}
