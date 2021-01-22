package com.link2me.android.common

import android.app.Activity
import android.os.Build
import android.widget.Toast

class BackPressHandler(private val activity: Activity) {
    private var backKeyPressedTime: Long = 0
    private var toast: Toast? = null

    fun onBackPressed() {
        if (isAfter2Seconds) {
            backKeyPressedTime = System.currentTimeMillis()
            // 현재시간을 다시 초기화
            toast = Toast.makeText(
                activity,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
                Toast.LENGTH_SHORT
            )
            toast?.show()
            return
        }
        if (isBefore2Seconds) {
            appShutdown()
            toast!!.cancel()
        }
    }

    // 2초 지났을 경우
    private val isAfter2Seconds: Boolean
        private get() = System.currentTimeMillis() > backKeyPressedTime + 2000

    // 2초가 지나지 않았을 경우
    private val isBefore2Seconds: Boolean
        private get() = System.currentTimeMillis() <= backKeyPressedTime + 2000

    private fun appShutdown() {
        // 홈버튼 길게 누르면 나타나는 히스토리에도 남아있지 않고 어플 종료
        if (Build.VERSION.SDK_INT >= 21) activity.finishAndRemoveTask() else activity.finish()
        System.exit(0)
    }
}