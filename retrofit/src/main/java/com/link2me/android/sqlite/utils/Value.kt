package com.link2me.android.sqlite.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor

class Value : AppCompatActivity() {
    companion object{
        const val API_BASE_URL = "https://test.abc.com/androidSample/"
        const val Photo_URL = "https://test.abc.com/androidSample/photos/"

        const val Contacts = "getContactData.php"
    }
}