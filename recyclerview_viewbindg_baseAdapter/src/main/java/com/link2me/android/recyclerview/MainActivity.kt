package com.link2me.android.recyclerview

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.link2me.android.common.BackPressHandler
import com.link2me.android.common.HangulUtils
import com.link2me.android.common.Utils
import com.link2me.android.recyclerview.databinding.ActivityMainBinding
import com.link2me.android.recyclerview.localdb.ContactDbFacade
import com.link2me.android.recyclerview.model.ContactData
import com.link2me.android.recyclerview.model.ContactDataDto
import com.link2me.android.recyclerview.model.SQLite_Item
import com.link2me.android.recyclerview.retrofit.RetroClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    private lateinit var binding: ActivityMainBinding
//    lateinit var editsearch: SearchView
//    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ContactsAdapter // 리스트뷰에 사용되는 ListViewAdapter
    private val addressItemList = mutableListOf<ContactData>() // 서버에서 가져온 원본 데이터 리스트
    private val searchItemList = mutableListOf<ContactData>() // 서버에서 가져온 원본 데이터 리스트
    private lateinit var backPressHandler: BackPressHandler
    private lateinit var mFacade: ContactDbFacade

    var sqliteDBMap = HashMap<String, SQLite_Item>()
    var aryIdx = ArrayList<String>()
    var aryName = ArrayList<String>()
    var aryCheck = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        mContext = this@MainActivity
        backPressHandler = BackPressHandler(this) // 뒤로 가기 버튼 이벤트
        mFacade = ContactDbFacade(mContext)

        initView()
    }

    private fun getServerData() {
        RetroClient.getInstance()?.getContactDataResult("1")?.enqueue(object :
            Callback<ContactDataDto> {
            override fun onResponse(call: Call<ContactDataDto>, response: Response<ContactDataDto>) {
                response.body()?.let {
                    if(it.status.contains("success")){
                        addressItemList.clear()

                        val item = it.message
                        addressItemList.addAll(item)
                        mAdapter.submitList(item)
                    }
                }
            }

            override fun onFailure(call: Call<ContactDataDto>, t: Throwable) {
                Log.e(TAG,"Retrofit response fail.")
            }

        })
    }

    private fun initView() {
        getServerData() // 서버 데이터 가져오기

        // Adapter에 추가 데이터를 저장하기 위한 ArrayList
        //mRecyclerView = findViewById(R.id.my_recyclerView)
        mAdapter = ContactsAdapter(mContext) // Adapter 생성
        binding.myRecyclerView.adapter = mAdapter // 어댑터를 리스트뷰에 세팅
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this)

        // Locate the EditText in listview_main.xml
        //editsearch = findViewById(R.id.search)
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

        mAdapter.setItemClickListener(object : ContactsAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val contacts = addressItemList[position]
                Toast.makeText(mContext, "클릭한 이름은 ${contacts.userNM} 입니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun filter(charText: String) {
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
        mAdapter.notifyDataSetChanged()  // 이거 없으면 어플 비정상 종료처리됨
    }

    override fun onBackPressed() {
        backPressHandler.onBackPressed()
    }
}