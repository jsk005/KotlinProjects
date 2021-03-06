package com.link2me.android.sqlite

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.link2me.android.common.BackPressHandler
import com.link2me.android.common.HangulUtils
import com.link2me.android.common.Utils
import com.link2me.android.sqlite.adapter.ContactsListAdapter
import com.link2me.android.sqlite.model.ContactData
import com.link2me.android.sqlite.model.ContactDataResult
import com.link2me.android.sqlite.retrofit.RetroClient
import com.link2me.android.sqlite.room.ContactDb
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    lateinit var editsearch: SearchView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ContactsListAdapter
    private val addressItemList = mutableListOf<ContactData>() // DB 전체 데이터 리스트
    private val searchItemList = mutableListOf<ContactData>() // 검색한 데이터 리스트
    private lateinit var backPressHandler: BackPressHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this@MainActivity
        backPressHandler = BackPressHandler(this) // 뒤로 가기 버튼 이벤트

        getServerData() // 서버 데이터 가져와서 SQLite DB에 저장
        initView()
    }

    private fun getServerData() {
        var db = ContactDb.getInstance(applicationContext)

        RetroClient.getInstance()?.getContactDataResult("1")?.enqueue(object :
            Callback<ContactDataResult> {
            override fun onResponse(call: Call<ContactDataResult>, response: Response<ContactDataResult>) {
                if(response.body()!!.status.contains("success")){
                    val contactData = response.body()!!.message

                    addressItemList.clear()
                    searchItemList.clear()

                    var i = 0
                    for (item in contactData){
                        db?.contactsDao()?.insert(item)
                    }

                    addressItemList.addAll(db?.contactsDao()?.getAll()!!)
                    searchItemList.addAll(db?.contactsDao()?.getAll()!!)

                    runOnUiThread { // 갱신된 데이터 내역을 어댑터에 알려줌
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ContactDataResult>, t: Throwable) {
                Log.e(TAG,"Retrofit response fail.")
            }

        })

    }

    private fun initView() {

        // Adapter에 추가 데이터를 저장하기 위한 ArrayList
        mRecyclerView = findViewById(R.id.my_recyclerView)
        mAdapter = ContactsListAdapter(mContext, searchItemList) // Adapter 생성
        mRecyclerView.adapter = mAdapter // 어댑터를 리스트뷰에 세팅
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Locate the EditText in listview_main.xml
        editsearch = findViewById(R.id.search)
        editsearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                val contacts = searchItemList[position]
                Toast.makeText(mContext, "클릭한 이름은 ${contacts.userNM} 입니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        searchItemList.clear()
        if (charText.length == 0) {
            searchItemList.addAll(addressItemList)
        } else {
            for (wp in addressItemList) {
                if (Utils.isNumber(charText)) { // 숫자여부 체크
                    if (wp.mobileNO.contains(charText) || wp.telNO.contains(charText)) {
                        // 휴대폰번호 또는 사무실번호에 숫자가 포함되어 있으면
                        searchItemList.add(wp)
                    }
                } else {
                    val iniName = HangulUtils.getHangulInitialSound(wp.userNM, charText)
                    if (iniName!!.indexOf(charText) >= 0) { // 초성검색어가 있으면 해당 데이터 리스트에 추가
                        searchItemList.add(wp)
                    } else if (wp.userNM.toLowerCase(Locale.getDefault()).contains(charText)) {
                        searchItemList.add(wp)
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        backPressHandler.onBackPressed()
    }
}