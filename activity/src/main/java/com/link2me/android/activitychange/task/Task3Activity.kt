package com.link2me.android.activitychange.task

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.link2me.android.activitychange.R

class Task3Activity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task3)
        Log.e(TAG, "onCreate()")
        val textView = findViewById<TextView>(R.id.tv_task3)
        textView.text = "태스크 3"
        textView.textSize = 30f
        findViewById<View>(R.id.btn_task3).setOnClickListener { v: View? ->
            val intent = Intent(this, Task1_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // TaskActivity1 부터 스택 전부 삭제하고 TaskActivity1 다시 시작해라.
            startActivity(intent)
            finish()
        }

        // FLAG_ACTIVITY_CLEAR_TOP 플래그만 주었을 때와 FLAG_ACTIVITY_SINGLE_TOP 을 같이 사용할 때의 차이점을 명확하게 이해하자.
        findViewById<View>(R.id.btn_task3B).setOnClickListener { v: View? ->
            val intent = Intent(this, Task1_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP) // TaskActivity1 재사용하라.
            startActivity(intent)
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

    override fun onBackPressed() {
        finish()
    }

}