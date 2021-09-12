package com.link2me.android.activitychange.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.link2me.android.activitychange.R
import com.link2me.android.activitychange.databinding.ActivityTask1Binding

class Task1_Activity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context

    // lazy 를 사용해서 처음 호출될 때 초기화 되도록 설정
    val binding by lazy { ActivityTask1Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_task1)
        setContentView(binding.root) // * setContentView에는 binding.root를 꼭 전달
        mContext = this@Task1_Activity
        Log.d(TAG, "onCreate()")

        // Task 에 대한 사항은 유투브 동영상
        // https://www.youtube.com/watch?v=icoOFKwnGc0&t=67s
        // 이것이 안드로이드다 (강의코드 037 ~ 40) 참고
        // adb shell dumpsys activity activities > activity_stack_1_1.txt

        setSupportActionBar(binding.task1Toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        //val textView = findViewById<TextView>(R.id.tv_task1)
        binding.contentTask1.tvTask1.text = "태스크 1"
        binding.contentTask1.tvTask1.textSize = 30f
        binding.contentTask1.btnTask1.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    Task2Activity::class.java
                )
            )
        }

        // 자기 자신의 Activity를 다시 호출할 때 FLAG_ACTIVITY_SINGLE_TOP 플래그는 어떤 걸 호출하는지 알아보자.
        // findViewById<View>(R.id.btn_call_task1)
        binding.contentTask1.btnCallTask1.setOnClickListener { v: View? ->
            val intent = Intent(mContext, Task1_Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            finish() // toolbar HOME 버튼 누르면 현재 Activity 스택에서 제거
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

}