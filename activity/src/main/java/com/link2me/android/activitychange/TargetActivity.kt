package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TargetActivity : AppCompatActivity(), ActivityChangListener {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    var ClassName: String? = null
    var getData: String? = null
    var textView: TextView? = null
    var button: Button? = null

    companion object {
        private var classToGoAfter: Class<*>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        mContext = this@TargetActivity
        Log.e(TAG, "onCreate classToGoAfter :" + classToGoAfter)
        initView()
    }

    private fun initView() {
        if (classToGoAfter == null) {
            ClassName = intent.extras!!.getString("classname").toString()
            val profile: Profiles? = intent?.getParcelableExtra("parcel")
            Log.e(TAG, "이름 :" + profile?.name)
            Log.e(TAG, "나이 :" + profile?.age)
            Log.e(TAG, "성별 :" + profile?.gender)
        }
        Log.e(TAG, "get ClassName :$ClassName")
        Log.e(TAG, "classToGoAfter :" + classToGoAfter)

        textView = findViewById(R.id.targettextView)
        textView?.text = "TargetActivity 입니다."
        button = findViewById(R.id.targetbutton)
        button?.setOnClickListener(View.OnClickListener { view: View? ->
            if (ClassName != null) {
                // Activity 이동의 상태를 기억했다가 다시 원래의 activity로 되돌아가기 위한 경우
                try {
                    val intent = Intent(
                        this@TargetActivity, Class.forName(
                            ClassName!!
                        )
                    )
                    startActivity(intent)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
                finish() // 현재 Activity 를 없애줌
            } else if (classToGoAfter != null) {
                gotoActivity()
            }
        })
        findViewById<View>(R.id.btn_changer).setOnClickListener {
            gotoActivity()
            Toast.makeText(mContext, "Other Activity 로 이동합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun gotoActivity() {
        Log.e(TAG, "gotoActivity :" + classToGoAfter)
        val intent = Intent(mContext, classToGoAfter)
        startActivity(intent)
        finish()
    }

    override fun activityClass(activityClassToGo: Class<*>?) {
        Log.e(TAG, "activityClass :$activityClassToGo")
        // 인터페이스 구현
        classToGoAfter = activityClassToGo
    }

    override fun onBackPressed() {
        finish()
    }

}