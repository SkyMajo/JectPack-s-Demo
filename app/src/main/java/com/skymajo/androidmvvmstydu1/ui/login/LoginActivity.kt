package com.skymajo.androidmvvmstydu1.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skymajo.androidmvvmstydu1.MainActivity
import com.skymajo.androidmvvmstydu1.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mb_login.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}
