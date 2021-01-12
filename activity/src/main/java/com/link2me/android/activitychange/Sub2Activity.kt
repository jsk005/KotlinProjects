package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Sub2Activity : AppCompatActivity() {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub2)
        mContext = this@Sub2Activity
        val textView = findViewById<TextView>(R.id.sub_tv2)
        textView.text = "SubActivity 2"
        textView.setTextColor(Color.RED)
        textView.textSize = 20f
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(mContext, SplashActivity::class.java))
        finish()
    }
}