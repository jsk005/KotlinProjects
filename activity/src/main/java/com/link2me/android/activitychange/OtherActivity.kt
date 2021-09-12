package com.link2me.android.activitychange

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.link2me.android.activitychange.databinding.ActivityOtherBinding

class OtherActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    val binding by lazy { ActivityOtherBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_other)
        setContentView(binding.root)

        //val textView = findViewById<TextView>(R.id.textView)
        binding.textView.text = "이곳은 Activity Changer를 통해 이동한 곳입니다."
    }

}