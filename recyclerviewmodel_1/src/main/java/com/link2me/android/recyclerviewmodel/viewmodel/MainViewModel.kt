package com.link2me.android.recyclerviewmodel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.link2me.android.recyclerviewmodel.model.ContactData
import com.link2me.android.recyclerviewmodel.model.ContactDataDto
import com.link2me.android.recyclerviewmodel.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 데이터의 변경사항을 알려주는 라이브 데이터를 가지는 뷰모델
class MainViewModel : ViewModel() {
    private val TAG = this.javaClass.simpleName

    // 뮤터블 라이브 데이터 - 수정 가능, 라이브 데이터 - 값 변경 안됨
    private val liveData: MutableLiveData<List<ContactData>> by lazy {
        MutableLiveData<List<ContactData>>().also {
            loadContacts()
        }
    }

    fun getContactsData(): LiveData<List<ContactData>> {
        return liveData
    }

    // LiveData는 관찰 가능한 데이터들의 홀더 클래스

    private fun loadContacts(){
        RetrofitService.getInstance()?.getContactDataResult("1")?.enqueue(object :
            Callback<ContactDataDto> {
            override fun onResponse(call: Call<ContactDataDto>, response: Response<ContactDataDto>) {
                response.body()?.let {
                    if(it.status.contains("success")){
                        liveData.value = it.message
                    }
                }
            }

            override fun onFailure(call: Call<ContactDataDto>, t: Throwable) {
                Log.d(TAG,"Retrofit response fail.")
                t.stackTrace
            }

        })
    }

    fun searchNameChanged(name: String) {

    }

}
