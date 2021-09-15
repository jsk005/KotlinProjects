package com.link2me.android.recyclerviewmodel.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.link2me.android.recyclerviewmodel.R

class SplashActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    var handler = Handler(Looper.getMainLooper())
    private val SPLASH_TIME_OUT = 2000

    private val runnable = Runnable {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            initView()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>) {
            Toast.makeText(
                this@SplashActivity,
                "권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this@SplashActivity
        checkPermission()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(mContext)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                .setPermissions(
                    *arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,  // 전화걸기 및 관리
                        Manifest.permission.READ_CONTACTS,  // 주소록 액세스 권한
                        Manifest.permission.WRITE_CONTACTS,  // 주소록 액세스 권한
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE // 기기, 사진, 미디어, 파일 엑세스 권한
                    )
                )
                .check()
        } else {
            initView()
        }
    }

    private fun initView() {
        if (Build.VERSION.SDK_INT >= 26) { // 출처를 알 수 없는 앱 설정 화면 띄우기
            val pm = mContext!!.packageManager
            Log.e("Package Name", packageName)
            if (!pm.canRequestPackageInstalls()) {
                startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:$packageName")
                    )
                )
            }
        }

        // 여기서는 로그인 처리 기능은 생략한다.
        handler.postDelayed(runnable, SPLASH_TIME_OUT.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}