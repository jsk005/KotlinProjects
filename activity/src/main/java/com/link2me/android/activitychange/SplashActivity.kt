package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.link2me.android.common.BackPressHandler

class SplashActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    private lateinit var backPressHandler: BackPressHandler

    private val handler = Handler()
    private val runnable = Runnable {
        startActivity(Intent(mContext, Sub2Activity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this@SplashActivity
        backPressHandler = BackPressHandler(this)
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.animationView)
        lottieAnimationView.setOnClickListener { view: View? ->
            startActivity(Intent(mContext, Sub1Activity::class.java))
            finish()
        }
        handler.postDelayed(runnable, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        //handler.removeCallbacks(runnable);
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //backPressHandler.onBackPressed();
        startActivity(Intent(mContext, MainActivity::class.java))
        finish()
    }
}