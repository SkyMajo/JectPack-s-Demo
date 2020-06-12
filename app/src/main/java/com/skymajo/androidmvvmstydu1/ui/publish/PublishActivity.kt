package com.skymajo.androidmvvmstydu1.ui.publish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skymajo.androidmvvmstydu1.R
import com.skymajo.libnavannotation.ActivityDestination

@ActivityDestination(pathUrl = "main/tabs/publish", needLogin = true)
class PublishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_publish)
    }
}