package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SubActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    var ClassName: String? = null
    var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)
        mContext = this@SubActivity
        ClassName = this.localClassName
        initView()
    }

    private fun initView() {
        textView = findViewById<TextView>(R.id.subtextView)
        textView?.text = "SubActivity UI"
        val btn2Main = findViewById<Button>(R.id.btntoMain)
        val btn2Target = findViewById<Button>(R.id.btntoTarget)
        btn2Main.setOnClickListener(this)
        btn2Target.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btntoMain -> {
                // 일반적인 Activity 간 이동
                val mainintent = Intent(this@SubActivity, MainActivity::class.java)
                startActivity(mainintent)
                finish()
            }
            R.id.btntoTarget -> {
                // 일반적인 Activity 간 이동
                val intentTarget = Intent(mContext, TargetActivity::class.java)
                intentTarget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intentTarget.putExtra("classname", ClassName)
                startActivity(intentTarget)
            }
        }
    }
}