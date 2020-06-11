package com.skymajo.androidmvvmstydu1

import android.os.Bundle
import android.os.UserManager
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.alibaba.fastjson.util.JavaBeanInfo.build
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skymajo.androidmvvmstydu1.model.Destination
import com.skymajo.androidmvvmstydu1.model.User
import com.skymajo.androidmvvmstydu1.ui.login.UserManager.get
import com.skymajo.androidmvvmstydu1.utils.AppConfig
import com.skymajo.androidmvvmstydu1.utils.NavGraphBuilder
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    lateinit var navController: NavController
    lateinit var  navView:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById<BottomNavigationView>(R.id.nav_view)
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = NavHostFragment.findNavController(fragment!!)
        NavGraphBuilder().builder(navController,this,fragment.id)
        navView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var sDestConfig = AppConfig.sDestConfig
        var iterator = sDestConfig!!.entries.iterator()
        while (iterator.hasNext()) {
            var entry: Map.Entry<String, Destination> = iterator.next()
            var value = entry.value
            if (value != null && !com.skymajo.androidmvvmstydu1.ui.login.UserManager.get().isLogin && value.id == menuItem.itemId && value.needLogin) {
                var login = com.skymajo.androidmvvmstydu1.ui.login.UserManager.get()
                    .login(this@MainActivity)
                login.observe(this,
                    Observer<User>(){
                        if(it != null){
                            Log.e("MainActivity","LiveData接收")
                            navView.selectedItemId = menuItem.itemId
                        }


                    })

                    return false
            }
        }


        navController.navigate(menuItem.itemId)
        return !TextUtils.isEmpty(menuItem.title)
    }


}

