package com.link2me.android.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Process
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Utils : AppCompatActivity() {
    companion object{
        fun showAlert(context: Context, title: String?, message: String?) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, id -> dialog.dismiss() }
            val alert = builder.create()
            alert.show()
        }

        fun NotConnected_showAlert(context: Context, activity: Activity) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("네트워크 연결 오류")
            builder.setMessage(
                """
    사용 가능한 무선네트워크가 없습니다.
    먼저 무선네트워크 연결상태를 확인해 주세요.
    """.trimIndent()
            )
                .setCancelable(false)
                .setPositiveButton("확인") { dialog, id ->
                    activity.finish() // exit
                    //application 프로세스를 강제 종료
                    Process.killProcess(Process.myPid())
                }
            val alert = builder.create()
            alert.show()
        }

        @SuppressLint("MissingPermission")
        fun NetworkConnection(context: Context): Boolean {
            val networkTypes = intArrayOf(ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI)
            try {
                val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                for (networkType in networkTypes) {
                    val activeNetwork = manager.activeNetworkInfo
                    if (activeNetwork != null && activeNetwork.type == networkType) {
                        return true
                    }
                }
            } catch (e: Exception) {
                return false
            }
            return false
        }

        @SuppressLint("MissingPermission")
        fun getPhoneNumber(context: Context): String {
            val telephony = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            var phoneNumber = ""
            try {
                if (telephony.line1Number != null) {
                    phoneNumber = telephony.line1Number
                } else {
                    if (telephony.simSerialNumber != null) {
                        phoneNumber = telephony.simSerialNumber
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (phoneNumber.startsWith("+82")) {
                phoneNumber = phoneNumber.replace("+82", "0")
            }
            //phoneNumber = phoneNumber.substring(phoneNumber.length()-10,phoneNumber.length());
            //phoneNumber="0"+phoneNumber;
            phoneNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country)
            } else {
                PhoneNumberUtils.formatNumber(phoneNumber)
            }
            return phoneNumber
        }
    }
}