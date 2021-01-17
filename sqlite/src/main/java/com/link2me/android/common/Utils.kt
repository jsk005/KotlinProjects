package com.link2me.android.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.regex.Pattern

object Utils : AppCompatActivity() {
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

    fun NetworkConnection(context: Context): Boolean {
        val networkTypes =
            intArrayOf(ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI)
        try {
            val manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
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

    fun getDeviceId(context: Context): String {
        // 단말기의 ID 정보를 얻기 위해서는 READ_PHONE_STATE 권한이 필요
        val deviceId: String
        deviceId = Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        return deviceId
    }

    fun getVersionCode(context: Context): Int {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val versionCode = packageInfo!!.versionCode
        val versionName = packageInfo.versionName
        return versionCode
    }

    fun getVersionName(context: Context): String {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packageInfo!!.versionName
    }

    fun isValidCellPhoneNumber(cellphoneNumber: String): Boolean {
        var returnValue = false
        Log.i("cell", cellphoneNumber)
        val regex =
            "^\\s*(010|011|012|013|014|015|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$"
        val p = Pattern.compile(regex)
        val m = p.matcher(cellphoneNumber)
        if (m.matches()) {
            returnValue = true
        }
        return returnValue
    }

    // 숫자인지 여부 체크
    fun isNumber(str: String): Boolean {
        return try {
            str.toDouble()
            true
        } catch (e: Exception) {
            false
        }
    }
}