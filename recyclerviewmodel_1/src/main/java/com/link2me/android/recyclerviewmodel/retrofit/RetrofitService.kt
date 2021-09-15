package com.link2me.android.recyclerviewmodel.retrofit

import com.google.gson.GsonBuilder
import com.link2me.android.common.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService { // 싱글턴
    // 레트로핏 클라어언트 선언
    private var retrofitClient: Retrofit? = null
    var instance: IRetrofit? = null

    val gson = GsonBuilder().setLenient().create()

    // 레트로핏 클라어언트 가져오기
    fun getClient(baseUrl: String): Retrofit? {
        if (retrofitClient == null){
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(Utils.createOkHttpClient())
                .build()
        }
        return retrofitClient
    }

    @JvmName("getInstance1")
    fun getInstance(): IRetrofit? {
        if (instance == null) {
            instance = getClient(RetrofitURL.API_BASE_URL)?.create(IRetrofit::class.java)
        }
        return instance
    }

}