package com.skymajo.androidmvvmstydu1.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.skymajo.androidmvvmstydu1.model.User;
import com.skymajo.libnetcache.cache.Cache;
import com.skymajo.libnetcache.cache.CacheManager;


public class UserManager {
    private static final String KEY_CACHE_USER = "cache_user";
    private static UserManager userManager = new UserManager();
    private MutableLiveData<User> mutableLiveData = new MutableLiveData<>();
    private User user;


    public static UserManager get(){
        return userManager;
    }

    private UserManager(){
        User cache = (User) CacheManager.getCache(KEY_CACHE_USER);
        if(cache!=null && cache.getExpires_time()>System.currentTimeMillis()){
            user = cache;
        }
    }
    public void save(User user) {
        this.user = user;
        CacheManager.save(KEY_CACHE_USER, user);
        if (getUserLiveData().hasObservers()) {
            getUserLiveData().postValue(user);
        }
    }

    //这里返回LiveData父类防止接收方拿到MutableLiveData乱发数据
    public LiveData<User> login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return getUserLiveData();
    }

    public boolean isLogin(){
        Log.e("UserManager.isLogin",""+(user == null?false:(user.getExpires_time())>System.currentTimeMillis()));
        return user == null?false:(user.getExpires_time())>System.currentTimeMillis();
    }

    public User getUser() {
        return isLogin()?user:null;
    }

    public long getUserId(){
        return isLogin()?user.getUserId():0;
    }


    /**
     * bugfix:  liveData默认情况下是支持黏性事件的，即之前已经发送了一条消息，当有新的observer注册进来的时候，也会把先前的消息发送给他，
     * <p>
     * 就造成了{@linkplain com.skymajo.androidmvvmstydu1.MainActivity#onNavigationItemSelected(MenuItem) }死循环
     * <p>
     * 那有两种解决方法
     * 1.我们在退出登录的时候，把livedata置为空，或者将其内的数据置为null
     * 2.利用我们改造的stickyLiveData来发送这个登录成功的事件
     * <p>
     * 我们选择第一种,把livedata置为空
     */
    public void logout() {
        CacheManager.delete(KEY_CACHE_USER, user);
        user = null;
        mutableLiveData = null;
    }

    private MutableLiveData<User> getUserLiveData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }

}
