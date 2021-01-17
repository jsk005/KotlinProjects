package com.link2me.android.sqlite

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
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
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    lateinit var editsearch: SearchView
    private lateinit var listView: ListView
    private lateinit var listViewAdapter: ListViewAdapter // 리스트뷰에 사용되는 ListViewAdapter
    private val addressItemList = ArrayList<Address_Item>() // 서버에서 가져온 원본 데이터 리스트
    private val searchItemList = ArrayList<Address_Item>() // 검색한 데이터 리스트
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
        // Adapter에 추가 데이터를 저장하기 위한 ArrayList
        serverData // 서버 데이터 가져오기
        listView = findViewById(R.id.my_listView)
        listViewAdapter = ListViewAdapter(mContext, searchItemList) // Adapter 생성
        listView.adapter = listViewAdapter // 어댑터를 리스트뷰에 세팅
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Locate the EditText in listview_main.xml
        editsearch = findViewById(R.id.search)
        editsearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // 문자열 입력을 완료했을 때 문자열 반환
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // 문자열이 변할 때마다 바로바로 문자열 반환
                listViewAdapter.filter(newText)
                return false
            }
        })
    }

    // RequestQueue 생성 및 초기화
    // 생성한 StringRequest를 RequestQueue에 추가
    // 갱신된 데이터 내역을 어댑터에 알려줌// 데이터가 없다면

    // 화면에 반영하기 위하여 runOnUiThread()를 호출하여 실시간 갱신한다.
    // 데이터가 틀린 부분이 있으면 수정하라.// 서버에 있는 자료이면 1로 세팅// 서버에서 가져온 데이터 저장
    // 서버에서 가져온 데이터 초기화
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
                        listViewAdapter.notifyDataSetChanged() // 갱신된 데이터 내역을 어댑터에 알려줌
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

    // 아이템 데이터 추가를 위한 메소드
    fun getServerDataList(uid: String, name: String, mobileNO: String, officeNO: String, photo_image: String, checkItem_flag: Boolean) {
        val item = Address_Item(uid,name,mobileNO,officeNO,photo_image,checkItem_flag)
        addressItemList.add(item)
    }

    // 선택한 데이터 추가를 위한 메소드
    fun selectDataList(uid: String, name: String, mobileNO: String, officeNO: String, photo_image: String, checkItem_flag: Boolean) {
        val item = Address_Item(uid,name,mobileNO,officeNO,photo_image,checkItem_flag)
        searchItemList.add(item)
    }

    inner class ListViewAdapter(var context: Context, items: MutableList<Address_Item>) : BaseAdapter() {
        lateinit var lvItemList: MutableList<Address_Item>

        internal inner class ViewHolder {
            var child_layout: LinearLayout? = null
            var photo_Image: ImageView? = null
            var tv_name: TextView? = null
            var tv_mobileNO: TextView? = null
            var child_btn: ImageView? = null
        }

        override fun getCount(): Int {
            return lvItemList.size // 데이터 개수 리턴
        }

        override fun getItem(position: Int): Any {
            return lvItemList[position]
        }

        override fun getItemId(position: Int): Long {
            // 지정한 위치(position)에 있는 데이터 리턴
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
            var convertView = convertView
            val viewHolder: ViewHolder
            val context = parent.context
            val index = Integer.valueOf(position)

            // 화면에 표시될 View
            if (convertView == null) {
                viewHolder = ViewHolder()
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.address_item, parent, false)
                convertView.setBackgroundColor(0x00FFFFFF)
                convertView.invalidate()

                // 화면에 표시될 View 로부터 위젯에 대한 참조 획득
                viewHolder.photo_Image = convertView.findViewById(R.id.profile_Image)
                viewHolder.tv_name = convertView.findViewById(R.id.child_name)
                viewHolder.tv_mobileNO = convertView.findViewById(R.id.child_mobileNO)
                viewHolder.child_btn = convertView.findViewById(R.id.child_Btn)
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            // PersonData 에서 position 에 위치한 데이터 참조 획득
            val addressItem = lvItemList!![position]
            if (addressItem.photo.contains("jpg")) {
                val photoURL = Value.Photo_URL + addressItem.photo
                Glide.with(mContext!!).load(photoURL).into(viewHolder.photo_Image!!)
            }
            viewHolder.tv_name!!.text = addressItem.userNM
            viewHolder.tv_mobileNO!!.text = PhoneNumberUtils.formatNumber(addressItem.mobileNO)
            val items = arrayOf("휴대폰 전화걸기", "연락처 저장")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("해당작업을 선택하세요")
            builder.setItems(items, DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(context, items[which] + "선택했습니다.", Toast.LENGTH_SHORT).show()
                when (which) {
                    0 -> {
                        if (addressItem.mobileNO.length == 0) {
                            Toast.makeText(context, "전화걸 휴대폰 번호가 없습니다.", Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }
                        val dialog1 = AlertDialog.Builder(context)
                            .setTitle(addressItem.userNM)
                            .setMessage(PhoneNumberUtils.formatNumber(addressItem.mobileNO) + " 통화하시겠습니까?")
                            .setPositiveButton("예",
                                DialogInterface.OnClickListener { dialog, which ->
                                    val intent = Intent(Intent.ACTION_CALL,
                                        Uri.parse("tel:" + PhoneNumberUtils.formatNumber(addressItem.mobileNO))
                                    )
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CALL_PHONE
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),1)
                                        }
                                        return@OnClickListener
                                    }
                                    startActivity(intent)
                                })
                            .setNegativeButton(
                                "아니오"
                            ) { dialog, which -> dialog.dismiss() }.create()
                        dialog1.show()
                    }
                    1 -> Toast.makeText(context, "전화번호 저장하는 로직은 직접 구현하시기 바랍니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            builder.create()
            viewHolder.child_btn!!.setOnClickListener { builder.show() }
            return convertView!!
        }

        // Filter Class
        fun filter(charText: String) {
            var charText = charText
            charText = charText.toLowerCase(Locale.getDefault())
            lvItemList.clear()
            if (charText.length == 0) {
                lvItemList.addAll(addressItemList)
            } else {
                for (wp in addressItemList) {
                    if (Utils.isNumber(charText)) { // 숫자여부 체크
                        if (wp.mobileNO.contains(charText) || wp.officeNO.contains(charText)) {
                            // 휴대폰번호 또는 사무실번호에 숫자가 포함되어 있으면
                            lvItemList.add(wp)
                        }
                    } else {
                        val iniName = HangulUtils.getHangulInitialSound(wp.userNM, charText)
                        if (iniName!!.indexOf(charText) >= 0) { // 초성검색어가 있으면 해당 데이터 리스트에 추가
                            lvItemList.add(wp)
                        } else if (wp.userNM.toLowerCase(Locale.getDefault()).contains(charText)) {
                            lvItemList.add(wp)
                        }
                    }
                }
            }
            notifyDataSetChanged()
        }

        init {
            lvItemList = items
        }
    }

    override fun onBackPressed() {
        backPressHandler.onBackPressed()
    }
}