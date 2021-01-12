package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent
import android.util.Log

class ActivityChanger {
    private val TAG = this.javaClass.simpleName

    var mListener: ActivityChangListener // 2. 인터페이스 변수 선언

    init {
        Log.e(TAG, "ActivityChanger Start. ")
        mListener = TargetActivity() // 구현 객체 생성
    }

    fun saveData(context: Context, activityClassToGo: Class<*>) {
        Log.e(TAG, "ActivityChanger Method :$activityClassToGo")
        mListener.activityClass(activityClassToGo)
        val intent = Intent(context, TargetActivity::class.java)
        context.startActivity(intent)
    }
}