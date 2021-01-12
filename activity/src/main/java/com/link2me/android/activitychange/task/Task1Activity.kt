package com.link2me.android.activitychange.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.link2me.android.activitychange.R

class Task1Activity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task1)
        mContext = this@Task1Activity
        Log.e(TAG, "onCreate()")

        // Task 에 대한 사항은 유투브 동영상
        // https://www.youtube.com/watch?v=icoOFKwnGc0&t=67s
        // 이것이 안드로이드다 (강의코드 037 ~ 40) 참고
        // adb shell dumpsys activity activities > activity_stack_1_1.txt
        val textView = findViewById<TextView>(R.id.tv_task1)
        textView.text = "태스크 1"
        textView.textSize = 30f
        findViewById<View>(R.id.btn_task1).setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    Task2Activity::class.java
                )
            )
        }

        // 자기 자신의 Activity를 다시 호출할 때 FLAG_ACTIVITY_SINGLE_TOP 플래그는 어떤 걸 호출하는지 알아보자.
        findViewById<View>(R.id.btn_call_task1).setOnClickListener { v: View? ->
            val intent = Intent(mContext, Task1Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
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
}