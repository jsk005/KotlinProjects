package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Sub1Activity : AppCompatActivity() {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub1)
        mContext = this@Sub1Activity
        val textView = findViewById<TextView>(R.id.sub_tv1)
        textView.text = "SubActivity 1"
        textView.setTextColor(Color.BLUE)
        textView.textSize = 20f
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(mContext, SplashActivity::class.java))
        finish()
    }
}