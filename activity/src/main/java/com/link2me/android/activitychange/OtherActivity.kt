package com.link2me.android.activitychange

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "이곳은 Activity Changer를 통해 이동한 곳입니다."
    }

}