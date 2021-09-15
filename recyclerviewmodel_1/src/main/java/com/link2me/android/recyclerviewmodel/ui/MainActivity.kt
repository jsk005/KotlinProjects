package com.link2me.android.recyclerviewmodel.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.link2me.android.common.BackPressHandler
import com.link2me.android.common.HangulUtils
import com.link2me.android.common.Utils
import com.link2me.android.recyclerviewmodel.adapter.ContactsListAdapter
import com.link2me.android.recyclerviewmodel.databinding.ActivityMainBinding
import com.link2me.android.recyclerviewmodel.model.ContactData
import com.link2me.android.recyclerviewmodel.model.ContactDataDto
import com.link2me.android.recyclerviewmodel.retrofit.RetrofitService
import com.link2me.android.recyclerviewmodel.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    lateinit var mContext: Context
    private lateinit var mAdapter: ContactsListAdapter // 리스트뷰에 사용되는 ListViewAdapter

    private val addressItemList = mutableListOf<ContactData>() // 서버에서 가져온 원본 데이터 리스트
    private val searchItemList = mutableListOf<ContactData>() // 서버에서 가져온 원본 데이터 리스트

    private lateinit var backPressHandler: BackPressHandler

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)  }

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        mContext = this@MainActivity

        Log.e(TAG, "onCreate")

        backPressHandler = BackPressHandler(this) // 뒤로 가기 버튼 이벤트

        // 뷰모델 가져오기
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // 관찰하여 데이터 값이 변경되면 호출
        viewModel.getContactsData().observe(this, listUpdateObserver)
    }

    var listUpdateObserver: Observer<List<ContactData>> =
        Observer {
            mAdapter = ContactsListAdapter(mContext) // Adapter 생성
            binding.myRecyclerView.adapter = mAdapter // 어댑터를 리스트뷰에 세팅
            binding.myRecyclerView.layoutManager = LinearLayoutManager(this)
            mAdapter.submitList(it)

            addressItemList.clear()
            addressItemList.addAll(it)

            binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // 문자열 입력을 완료했을 때 문자열 반환
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // 문자열이 변할 때마다 바로바로 문자열 반환
                    filter(newText)
                    return false
                }
            })

            mAdapter.setItemClickListener(object : ContactsListAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    val contacts = addressItemList[position]
                    Toast.makeText(mContext, "클릭한 이름은 ${contacts.userNM} 입니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        }

    fun filter(charText: String) {
        // ListAdapter 에서 처리하는 방법을 알게되면 수정할 예정
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        searchItemList.clear()
        if (charText.length == 0) {
            mAdapter.submitList(addressItemList)
        } else {
            for (wp in addressItemList) {
                if (Utils.isNumber(charText)) { // 숫자여부 체크
                    if (wp.mobileNO.contains(charText) || wp.officeNO.contains(charText)) {
                        // 휴대폰번호 또는 사무실번호에 숫자가 포함되어 있으면
                        searchItemList.add(wp)
                        mAdapter.submitList(searchItemList)
                    }
                } else {
                    val iniName = HangulUtils.getHangulInitialSound(wp.userNM, charText)
                    if (iniName!!.indexOf(charText) >= 0) { // 초성검색어가 있으면 해당 데이터 리스트에 추가
                        searchItemList.add(wp)
                        mAdapter.submitList(searchItemList)
                    } else if (wp.userNM.toLowerCase(Locale.getDefault()).contains(charText)) {
                        searchItemList.add(wp)
                        mAdapter.submitList(searchItemList)
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged()  // 이거 없애면 어플 비정상 종료 처리됨
    }

    override fun onRestart() {
        super.onRestart()
        Log.e(TAG, "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
    }

    override fun onBackPressed() {
        backPressHandler.onBackPressed()
    }
}