package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.link2me.android.activitychange.databinding.ActivitySplashBinding
import com.link2me.android.common.BackPressHandler

class SplashActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var mContext: Context
    private lateinit var backPressHandler: BackPressHandler

    val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        startActivity(Intent(mContext, Sub2Activity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        setContentView(binding.root)
        mContext = this@SplashActivity
        backPressHandler = BackPressHandler(this)

        //val lottieAnimationView = findViewById<LottieAnimationView>(R.id.animationView)
        binding.animationView.setOnClickListener { view: View? ->
            startActivity(Intent(mContext, Sub1Activity::class.java))
            finish() // Sub1Activity 로 전환되면서 Stack 에서 제거하기 위한 목적
        }
        handler.postDelayed(runnable, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable);
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backPressHandler.onBackPressed();
        //startActivity(Intent(mContext, MainActivity::class.java))
        //finish()
    }
}