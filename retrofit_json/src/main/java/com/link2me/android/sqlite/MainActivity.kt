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
import com.google.gson.JsonObject
import com.link2me.android.common.BackPressHandler
import com.link2me.android.common.HangulUtils
import com.link2me.android.common.Utils
import com.link2me.android.sqlite.localdb.ContactContract
import com.link2me.android.sqlite.localdb.ContactDbFacade
import com.link2me.android.sqlite.model.ContactData
import com.link2me.android.sqlite.model.ContactDataResult
import com.link2me.android.sqlite.model.SQLite_Item
import com.link2me.android.sqlite.retrofit.RetroClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    lateinit var editsearch: SearchView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ContactsListAdapter // 리스트뷰에 사용되는 ListViewAdapter
    private val addressItemList = mutableListOf<ContactData>() // 서버에서 가져온 원본 데이터 리스트
    private val searchItemList = mutableListOf<ContactData>() // 검색한 데이터 리스트
    private lateinit var backPressHandler: BackPressHandler
    private lateinit var mFacade: ContactDbFacade

    var sqliteDBMap = HashMap<String, SQLite_Item>()
    var aryIdx = ArrayList<String>()
    var aryName = ArrayList<String>()
    var aryCheck = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this@MainActivity
        backPressHandler = BackPressHandler(this) // 뒤로 가기 버튼 이벤트
        mFacade = ContactDbFacade(mContext)

        initView()
    }

    private fun getServerData() {
        RetroClient.getInstance()?.getContactsObject("1")?.enqueue(object :
            Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful){
                    addressItemList.clear()
                    searchItemList.clear()

                    val jsonObj = JSONObject(response.body().toString())
                    val person = jsonObj.getJSONArray(ContactContract._RESULTS)

                    for (i in 0 until person.length()) {
                        val c = person.getJSONObject(i)
                        val idx = c.getString(ContactContract.Entry._IDX)
                        val name = c.getString(ContactContract.Entry._NAME)
                        val mobileNO = c.getString(ContactContract.Entry._MobileNO)
                        val officeNO = c.getString(ContactContract.Entry._telNO)
                        val PhotoImage = c.getString(ContactContract.Entry._Photo)

                        Log.e(TAG,"name : " + name)

                        getAllDataList(idx, name, mobileNO, officeNO, PhotoImage, false)
                        selectDataList(idx, name, mobileNO, officeNO, PhotoImage, false)
                    }

                    runOnUiThread { // 갱신된 데이터 내역을 어댑터에 알려줌
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e(TAG,"Retrofit response fail.")
            }

        })

    }

    private fun initView() {
        getServerData() // 서버 데이터 가져오기

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
                    if (wp.mobileNO.contains(charText) || wp.officeNO.contains(charText)) {
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

    // 아이템 전체 데이터 추가 메소드
    fun getAllDataList(uid: String, name: String, mobileNO: String, officeNO: String, photo_image: String, checkItem_flag: Boolean) {
        val item = ContactData(uid, name, mobileNO, officeNO, photo_image, checkItem_flag)
        addressItemList.add(item)
    }

    // 선택한 데이터 추가 메소드
    fun selectDataList(uid: String, name: String, mobileNO: String, officeNO: String, photo_image: String, checkItem_flag: Boolean) {
        val item = ContactData(uid, name, mobileNO, officeNO, photo_image, checkItem_flag)
        searchItemList.add(item)
    }

    override fun onBackPressed() {
        backPressHandler.onBackPressed()
    }
}