package com.link2me.android.activitychange.task

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.link2me.android.activitychange.R

class Task2Activity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task2)
        Log.e(TAG, "onCreate()")
        val textView = findViewById<TextView>(R.id.tv_task2)
        textView.text = "태스크 2"
        textView.textSize = 30f
        findViewById<View>(R.id.btn_task2).setOnClickListener { v: View? ->
            startActivity(Intent(this,Task3Activity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e(TAG, "onRestart()")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause()")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy()")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.e(TAG, "onNewIntent()")
    }
}