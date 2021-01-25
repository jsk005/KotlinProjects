package com.link2me.android.sqlite.retrofit

import com.google.gson.JsonObject
import com.link2me.android.sqlite.utils.Value
import com.link2me.android.sqlite.model.ContactData
import com.link2me.android.sqlite.model.ContactDataResult
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IRetrofit {
    // Retrofit 라이브러리의 여러 형태 처리 테스트 목적

    @FormUrlEncoded
    @POST(Value.Contacts)
    fun getContactDataResult(@Field("idx") idx: String): Call<ContactDataResult>

}