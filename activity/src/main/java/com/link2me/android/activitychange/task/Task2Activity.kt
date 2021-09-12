package com.link2me.android.activitychange.task

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.link2me.android.activitychange.R
import com.link2me.android.activitychange.databinding.ActivityTask2Binding

class Task2Activity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    // lazy 를 사용해서 처음 호출될 때 초기화 되도록 설정
    val binding by lazy { ActivityTask2Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_task2)
        setContentView(binding.root)
        Log.d(TAG, "onCreate()")

        //val textView = findViewById<TextView>(R.id.tv_task2)
        binding.tvTask2.text = "태스크 2"
        binding.tvTask2.textSize = 30f
        findViewById<View>(R.id.btn_task2).setOnClickListener { v: View? ->
            startActivity(Intent(this,Task3Activity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent()")
    }

    override fun onBackPressed() {
        finish()
    }

}