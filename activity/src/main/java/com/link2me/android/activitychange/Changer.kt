package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent

class Changer {

    companion object{
        private var activityChangListener: ActivityChangListener? = null // 2. Interface 변수 선언

        private fun init(context: Context) {
            if (activityChangListener == null) {
                activityChangListener = TargetActivity() // 구현 객체 생성
            }
        }

        fun saveData(context: Context, activityClassToGo: Class<*>?) {
            init(context)
            activityChangListener!!.activityClass(activityClassToGo)
            val intent = Intent(context, TargetActivity::class.java)
            context.startActivity(intent)
        }
    }

}