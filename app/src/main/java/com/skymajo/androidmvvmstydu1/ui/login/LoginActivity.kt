package com.skymajo.androidmvvmstydu1.ui.login

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skymajo.androidmvvmstydu1.MainActivity
import com.skymajo.androidmvvmstydu1.R
import com.skymajo.androidmvvmstydu1.model.User
import com.skymajo.androidmvvmstydu1.utils.AppGlobals
import com.skymajo.libnetcache.ApiResponse
import com.skymajo.libnetcache.ApiServce
import com.skymajo.libnetcache.JsonCallBack
import com.tencent.connect.UserInfo
import com.tencent.connect.auth.QQToken
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    lateinit var mTencent: Tencent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mb_login.setOnClickListener {
//            Acp.getInstance(this).request(
//                AcpOptions.Builder()
//                    .setPermissions(
//                    )
//                    .build(),
//                object : AcpListener {
//                    override fun onGranted() {
            Login()
//                        Log.e("设备数据-", "权限同意")
//                    }

//                    override fun onDenied(permissions: List<String>) {
//                        Log.e("设备数据-", "权限拒绝")
//                    }
//                })
        }
    }

    private fun Login() {
        mTencent = Tencent.createInstance("101882974", AppGlobals.getApplication()!!.applicationContext)
        mTencent.login(this,"all", object : IUiListener {
            override fun onComplete(p0: Any?) {
                Toast.makeText(this@LoginActivity,"登陆成功",Toast.LENGTH_SHORT).show()
                Log.e("Login-", "登陆成功"+p0.toString())
                var jsonObject = p0 as JSONObject
                var openid = jsonObject.getString("openid")
                var gaccess_tokenet = jsonObject.getString("access_token")
                var expires_in = jsonObject.getString("expires_in")
                var expires_time = jsonObject.getString("expires_time")
                mTencent.setAccessToken(gaccess_tokenet,expires_in)
                mTencent.openId = openid
                var qqToken = mTencent.qqToken
                getUserInfo(qqToken,expires_time,openid)
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            }



            override fun onCancel() {
                Toast.makeText(this@LoginActivity,"登陆取消",Toast.LENGTH_SHORT).show()
                Log.e("Login-", "登陆取消")
            }

            override fun onError(p0: UiError?) {
                Toast.makeText(this@LoginActivity,"登陆失败",Toast.LENGTH_SHORT).show()
                Log.e("Login-", "登陆失败")

            }
        })

    }

    private fun getUserInfo(
        qqToken: QQToken,
        expiresTime: String,
        openid: String
    ) {
        var userInfo = UserInfo(applicationContext, qqToken)
        userInfo.getUserInfo(object : IUiListener {
            override fun onComplete(p0: Any?) {
                Log.e("Login-", "登陆成功"+p0.toString())
                var jsonObject = p0 as JSONObject
                var nickname = jsonObject.getString("nickname")
                var figureurl_2 = jsonObject.getString("figureurl_2")
                save(nickname,figureurl_2,expiresTime,openid)
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity,"取消获取用户信息",Toast.LENGTH_SHORT).show()
            }

            override fun onError(p0: UiError?) {
                Toast.makeText(this@LoginActivity,"用户信息获取失败",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun save(nickname: String, avatar: String, expiresTime: String, openid: String) {
        Log.e("用户信息","$nickname - $avatar - $expiresTime - $openid")
        var test = 1599466896151
//        "/user/insert?avatar=http%3A%2F%2Fqzapp.qlogo.cn%2Fqzapp%2F101882974%2F7DC505348CDAD5940CB595D40F5CBAF4%2F100&expires_time=1599547404223&followCount=0&likeCount=0&name=%E6%A2%85%E4%B9%8B%E9%AD%94%E5%A5%B3%E2%80%87%E2%80%88%E2%80%89%E2%80%8A&qqOpenId=7DC505348CDAD5940CB595D40F5CBAF4&topCommentCount=0"
        ApiServce.get<Any>("/user/insert")
            .addParam("name", nickname)
            .addParam("avatar", avatar)
            .addParam("qqOpenId", openid)
////            .addParam("qqOpenId","123")
            .addParam("expires_time", expiresTime.toBigInteger())
//            .addParam("expires_time",test)
            .execute(object : JsonCallBack<User>() {
                override fun onSusccess(response: ApiResponse<User>?) {
                    super.onSusccess(response)
                    if (response!!.body == null){
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity,"用户信息获取失败",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        UserManager.get().save(response!!.body!!)
                    }
                    Log.e("LoginActivity",""+response.message)

                }

                override fun onError(response: ApiResponse<User>?) {
                    if (response != null) {
                        Log.e("LoginActivity",""+response.message)
                    }
                    super.onError(response)

                }
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Tencent.onActivityResultData(requestCode, resultCode, data, object : IUiListener {
            override fun onComplete(p0: Any?) {
                Log.e("Login-", "登陆成功"+p0.toString())
            }

            override fun onCancel() {
                Log.e("Login-", "登陆取消")
            }

            override fun onError(p0: UiError?) {
                Log.e("Login-", "登陆错误"+p0.toString())
            }

        });

        if(requestCode == Constants.REQUEST_LOGIN) {

                Tencent.handleResultData(data, object : IUiListener {
                    override fun onComplete(p0: Any?) {
                        Log.e("Login-", "登陆成功"+p0.toString())
                    }

                    override fun onCancel() {
                        Log.e("Login-", "登陆取消")
                    }

                    override fun onError(p0: UiError?) {
                        Log.e("Login-", "登陆错误"+p0.toString())
                    }

                });
        }

    }
}
