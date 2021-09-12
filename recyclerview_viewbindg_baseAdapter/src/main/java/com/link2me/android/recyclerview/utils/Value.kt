package com.link2me.android.recyclerview.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor

class Value : AppCompatActivity() {
    companion object{
        // IP 주소는 public 망에서 접속할 수 있는 IP주소 또는 도메인 필요
        const val API_BASE_URL = "https://test.05rg.com/androidSample/"
        const val Photo_URL = "https://test.05rg.com/androidSample/photos/"

        const val Contacts = "getContactDto.php" // 상황에 맞게 수정 필요
    }
}