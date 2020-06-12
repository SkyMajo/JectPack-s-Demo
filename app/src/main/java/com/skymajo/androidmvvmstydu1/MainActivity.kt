package com.skymajo.androidmvvmstydu1

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skymajo.androidmvvmstydu1.model.Destination
import com.skymajo.androidmvvmstydu1.model.User
import com.skymajo.androidmvvmstydu1.ui.login.UserManager
import com.skymajo.androidmvvmstydu1.utils.AppConfig
import com.skymajo.androidmvvmstydu1.utils.NavGraphBuilder
import com.skymajo.androidmvvmstydu1.view.AppBottomBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/* App 主页 入口
* <p>
* 1.底部导航栏 使用AppBottomBar 承载
* 2.内容区域 使用WindowInsetsNavHostFragment 承载
* <p>
* 3.底部导航栏 和 内容区域的 切换联动 使用NavController驱动
* 4.底部导航栏 按钮个数和 内容区域destination个数。由注解处理器NavProcessor来收集,生成assetsdestination.json。而后我们解析它。
*/

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    private var navController: NavController? = null
    lateinit var navView:AppBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        //由于 启动时设置了 R.style.launcher 的windowBackground属性
        //势必要在进入主页后,把窗口背景清理掉
        setTheme(R.style.AppTheme)

        //启用沉浸式布局，白底黑字
//        StatusBar.fitSystemBar(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById<AppBottomBar>(R.id.nav_view)
        navController = NavHostFragment.findNavController(nav_host_fragment)
        NavGraphBuilder().builder(navController!!,this,nav_host_fragment.id)
        navView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val destConfig: HashMap<String, Destination> = AppConfig.getDestinationConfig()
        val iterator: Iterator<Map.Entry<String, Destination>> =
            destConfig.entries.iterator()
        //遍历 target destination 是否需要登录拦截
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val value = entry.value
            if (value != null && !UserManager.get()
                    .isLogin() && value.needLogin && value.id === menuItem.itemId
            ) {
                UserManager.get().login(this)
                    .observe(this,
                        Observer<User?> { user ->
                            if (user != null) {
                                navView!!.selectedItemId = menuItem.itemId
                            }
                        })
                return false
            }
        }
        navController!!.navigate(menuItem.itemId)
        return !TextUtils.isEmpty(menuItem.title)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
//        boolean shouldIntercept = false;
//        int homeDestinationId = 0;
//
//        Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
//        String tag = fragment.getTag();
//        int currentPageDestId = Integer.parseInt(tag);
//
//        HashMap<String, Destination> config = AppConfig.getDestConfig();
//        Iterator<Map.Entry<String, Destination>> iterator = config.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Destination> next = iterator.next();
//            Destination destination = next.getValue();
//            if (!destination.asStarter && destination.id == currentPageDestId) {
//                shouldIntercept = true;
//            }
//
//            if (destination.asStarter) {
//                homeDestinationId = destination.id;
//            }
//        }
//
//        if (shouldIntercept && homeDestinationId > 0) {
//            navView.setSelectedItemId(homeDestinationId);
//            return;
//        }
//        super.onBackPressed();

        //当前正在显示的页面destinationId
        val currentPageId = navController!!.currentDestination!!.id

        //APP页面路导航结构图  首页的destinationId
        val homeDestId = navController!!.graph.startDestination

        //如果当前正在显示的页面不是首页，而我们点击了返回键，则拦截。
        if (currentPageId != homeDestId) {
            navView!!.setSelectedItemId(homeDestId)
            return
        }

        //否则 finish，此处不宜调用onBackPressed。因为navigation会操作回退栈,切换到之前显示的页面。
        finish()
    }

    /**
     * bugfix:
     * 当MainActivity因为内存不足或系统原因 被回收时 会执行该方法。
     *
     *
     * 此时会触发 FragmentManangerImpl#saveAllState的方法把所有已添加的fragment基本信息给存储起来(view没有存储)，以便于在恢复重建时能够自动创建fragment
     *
     *
     * 但是在fragment嵌套fragment的情况下，被内嵌的fragment在被恢复时，生命周期被重新调度，出现了错误。没有重新走onCreateView 方法
     * 从而会触发throw new IllegalStateException("Fragment " + fname did not create a view.");的异常
     *
     *
     * 但是在没有内嵌fragment的情况，没有问题、
     *
     *
     *
     *
     * 那我们为了解决这个问题，网络上也有许多方案，但都不尽善尽美。
     *
     *
     * 此时我们复写onSaveInstanceState，不让 FragmentManangerImpl 保存fragment的基本数据，恢复重建时，再重新创建即可
     *
     * @param outState
     */
    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        //super.onSaveInstanceState(outState);
    }
}

