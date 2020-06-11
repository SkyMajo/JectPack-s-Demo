package com.skymajo.androidmvvmstydu1.ui.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
    public void save(User user){
        this.user = user;
        CacheManager.save(KEY_CACHE_USER,user);
        if(mutableLiveData.hasObservers()){
            Log.e("UserManager.save","postValue");
            mutableLiveData.postValue(user);
//            mutableLiveData = null;
            mutableLiveData.postValue(null);
        }
    }

    //这里返回LiveData父类防止接收方拿到MutableLiveData乱发数据
    public LiveData<User> login(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
        return mutableLiveData;
    }

    public boolean isLogin(){
        Log.e("UserManager.isLogin",""+(user == null?false:(user.getExpires_time())>System.currentTimeMillis()));
        return user == null?false:(user.getExpires_time())<System.currentTimeMillis();
    }

    public User getUser() {
        return isLogin()?user:null;
    }

    public long getUserId(){
        return isLogin()?user.getUserId():0;
    }



}
