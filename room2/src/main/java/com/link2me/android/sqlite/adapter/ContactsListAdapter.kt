package com.link2me.android.sqlite.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.link2me.android.sqlite.R
import com.link2me.android.sqlite.model.ContactData
import com.link2me.android.sqlite.utils.Value
import kotlinx.android.synthetic.main.address_item.view.*

class ContactsListAdapter(val context: Context, val itemList: List<ContactData>) : RecyclerView.Adapter<ContactsListAdapter.ContactsViewHolder>() {
    private val TAG = this.javaClass.simpleName

    // itemClockListener 를 위한 인터페이스 정의
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener: OnItemClickListener

    fun  setItemClickListener(itemClickListener: OnItemClickListener){
        this.itemClickListener = itemClickListener
    }
    //////////////////////////////////////////////////////////////////

    // RecyclerView 초기화때 호출된다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        // ViewHolder에 쓰일 Layout을 inflate하는 함수
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.address_item, parent,false)
        return ContactsViewHolder(rootView,context)
        // 뷰 홀더가 생성된 후 RecyclerView가 뷰 홀더를 뷰의 데이터에 바인딩한다.
    }

    // 생성된 View에 보여줄 데이터를 설정
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        // RecyclerView는 ViewHolder를 데이터와 연결할 때 이 메서드를 호출한다.
        val item = itemList[position]

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }

        holder.apply {
            bind(item)
        }
    }

    override fun getItemCount(): Int = itemList.size

    class ContactsViewHolder(var view: View, val context: Context) : RecyclerView.ViewHolder(view) {
        // View를 저장할 수 있는 변수
        val img_photo = view.findViewById<ImageView>(R.id.profile_Image)
        val tv_name = view.findViewById<TextView>(R.id.child_name)
        val tv_mobileNO = view.findViewById<TextView>(R.id.child_mobileNO)
        val btn_phonecall = view.findViewById<ImageView>(R.id.child_Btn)

        // View와 데이터를 연결시키는 함수
        fun bind(item: ContactData){
            if (item.photo.contains("jpg")){
                val photoURL = Value.Photo_URL + item.photo
                Glide.with(context).load(photoURL).into(img_photo)
            }

            tv_name.text = item.userNM
            tv_mobileNO.text = item.mobileNO

            val items = arrayOf("휴대폰 전화걸기", "연락처 저장")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("해당작업을 선택하세요")
            builder.setItems(items, DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(context, items[which] + "선택했습니다.", Toast.LENGTH_SHORT).show()
                when (which) {
                    0 -> {
                        if (item.mobileNO.length == 0) {
                            Toast.makeText(context, "전화걸 휴대폰 번호가 없습니다.", Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }
                        val dialog1 = AlertDialog.Builder(context)
                            .setTitle(item.userNM)
                            .setMessage(PhoneNumberUtils.formatNumber(item.mobileNO) + " 통화하시겠습니까?")
                            .setPositiveButton("예",
                                { dialog, which ->
                                    val intent = Intent(Intent.ACTION_CALL,
                                        Uri.parse("tel:" + PhoneNumberUtils.formatNumber(item.mobileNO))
                                    )
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                })
                            .setNegativeButton("아니오") { dialog, which -> dialog.dismiss() }.create()
                        dialog1.show()
                    }
                    1 -> Toast.makeText(context, "전화번호 저장하는 로직은 직접 구현하시기 바랍니다.", Toast.LENGTH_SHORT).show()
                }
            })
            builder.create()
            btn_phonecall.setOnClickListener { builder.show() }
        }
    }
}
