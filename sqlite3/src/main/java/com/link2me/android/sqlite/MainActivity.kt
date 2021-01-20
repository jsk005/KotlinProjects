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
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.link2me.android.common.BackPressHandler
import com.link2me.android.common.HangulUtils
import com.link2me.android.common.Utils
import com.link2me.android.sqlite.localdb.ContactContract
import com.link2me.android.sqlite.localdb.ContactDbFacade
import com.link2me.android.sqlite.model.Address_Item
import com.link2me.android.sqlite.model.SQLite_Item
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    lateinit var editsearch: SearchView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ContactsListAdapter // 리스트뷰에 사용되는 ListViewAdapter
    private val addressItemList = mutableListOf<Address_Item>() // 서버에서 가져온 원본 데이터 리스트
    private val searchItemList = mutableListOf<Address_Item>() // 검색한 데이터 리스트
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
        SQLiteDB2ArrayList()
        initView()
    }

    private fun initView() {
        serverData // 서버 데이터 가져오기

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

        mAdapter.setItemClickListener(object: ContactsListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val contacts = searchItemList[position]
                Toast.makeText(mContext, "클릭한 이름은 ${contacts.userNM} 입니다.", Toast.LENGTH_SHORT).show()
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

    // 화면에 반영하기 위하여 runOnUiThread()를 호출하여 실시간 갱신한다.
    // 데이터가 틀린 부분이 있으면 수정하라.// 서버에 있는 자료이면 1로 세팅// 서버에서 가져온 데이터 저장
    private val serverData: Unit
        private get() {
            val responseListener = Response.Listener { response: String? ->
                var newcnt = 0
                var upcnt = 0
                var no_cnt = 0
                var peoples: JSONArray? = null
                try {
                    val jsonObj = JSONObject(response)
                    peoples = jsonObj.getJSONArray(ContactContract._RESULTS)
                    addressItemList.clear() // 서버에서 가져온 데이터 초기화
                    for (i in 0 until peoples.length()) {
                        val c = peoples.getJSONObject(i)
                        val idx = c.getString(ContactContract.Entry._IDX)
                        val name = c.getString(ContactContract.Entry._NAME)
                        val mobileNO = c.getString(ContactContract.Entry._MobileNO)
                        val officeNO = c.getString(ContactContract.Entry._telNO)
                        val Team = ""
                        val Mission = ""
                        val Position = ""
                        val PhotoImage = c.getString(ContactContract.Entry._Photo)
                        val Status = "1"

                        Log.e(TAG,"name : " + name)

                        // 서버에서 가져온 데이터 저장
                        getServerDataList(idx, name, mobileNO, officeNO, PhotoImage, false)
                        selectDataList(idx, name, mobileNO, officeNO, PhotoImage, false)
                        if (sqliteDBMap.containsKey(idx)) {
                            val item = sqliteDBMap[idx]
                            val sqLiteIdx = item!!.idx
                            val sqLiteName = item.userNM
                            val sqLiteMobileNO = item.mobileNO
                            val sqLiteOfficeNO = item.telNO
                            val sqLiteTeam = item.team
                            val sqLiteMission = item.mission
                            val sqLitePosition = item.position
                            aryCheck[aryIdx.indexOf(idx)] = 1 // 서버에 있는 자료이면 1로 세팅
                            if (sqLiteIdx == idx && sqLiteName == name && sqLiteMobileNO == mobileNO && sqLiteOfficeNO.equals(
                                    officeNO, ignoreCase = true) && sqLiteTeam.equals(Team, ignoreCase = true)
                                && sqLiteMission.equals(Mission,ignoreCase = true) && sqLitePosition.equals(Position, ignoreCase = true)
                            ) {
                                no_cnt++
                            } else {
                                // 데이터가 틀린 부분이 있으면 수정하라.
                                upcnt++
                                mFacade.updateDB(idx, name, mobileNO, officeNO, Team, Mission, Position, PhotoImage, Status)
                            }
                        } else { // 데이터가 없다면
                            newcnt++
                            mFacade.InsertData(idx, name, mobileNO, officeNO, Team, Mission, Position, PhotoImage, Status)
                        }
                    }
                    Log.e(TAG, "No Change : $no_cnt, Update : $upcnt, New Insert : $newcnt")

                    // 화면에 반영하기 위하여 runOnUiThread()를 호출하여 실시간 갱신한다.
                    runOnUiThread {
                        mAdapter.notifyDataSetChanged() // 갱신된 데이터 내역을 어댑터에 알려줌
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            val request = ContactRequest(mContext, "1", responseListener)
            val requestQueue = Volley.newRequestQueue(mContext) // RequestQueue 생성 및 초기화
            requestQueue.add(request) // 생성한 StringRequest를 RequestQueue에 추가
        }

    fun SQLiteDB2ArrayList() {
        sqliteDBMap.clear() // 메모리 초기화
        aryIdx.clear()
        aryCheck.clear()
        val cursor = mFacade.LoadSQLiteDBCursor()
        try {
            cursor!!.moveToFirst()
            println("SQLiteDB 개수 = " + cursor.count)
            while (!cursor.isAfterLast) {
                val item = SQLite_Item(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
                )
                // HashMap 에 추가
                sqliteDBMap[cursor.getString(0)] = item
                aryIdx.add(cursor.getString(0))
                aryName.add(cursor.getString(1))
                aryCheck.add(cursor.getInt(8)) // 로컬에 있고 서버에 없는 자료 제거 목적
                cursor.moveToNext()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    // 아이템 전체 데이터 추가 메소드
    fun getServerDataList(uid: String, name: String, mobileNO: String, officeNO: String, photo_image: String, checkItem_flag: Boolean) {
        val item = Address_Item(uid,name,mobileNO,officeNO,photo_image,checkItem_flag)
        addressItemList.add(item)
    }

    // 선택한 데이터 추가 메소드
    fun selectDataList(uid: String, name: String, mobileNO: String, officeNO: String, photo_image: String, checkItem_flag: Boolean) {
        val item = Address_Item(uid,name,mobileNO,officeNO,photo_image,checkItem_flag)
        searchItemList.add(item)
    }

    override fun onBackPressed() {
        backPressHandler.onBackPressed()
    }
}