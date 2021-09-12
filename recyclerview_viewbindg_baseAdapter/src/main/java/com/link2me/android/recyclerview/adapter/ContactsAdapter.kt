package com.link2me.android.recyclerview.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.link2me.android.recyclerview.R
import com.link2me.android.recyclerview.databinding.ItemAddressBinding
import com.link2me.android.recyclerview.model.ContactData
import com.link2me.android.recyclerview.utils.Value
import java.util.*

class ContactsAdapter(val context: Context) : ListAdapter<ContactData, ContactsAdapter.ViewHolder>(diffUtil) {

    // itemClickListener 를 위한 인터페이스 정의
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener: OnItemClickListener

    fun  setItemClickListener(itemClickListener: OnItemClickListener){
        this.itemClickListener = itemClickListener
    }
    //////////////////////////////////////////////////////////////////

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        // View와 데이터를 연결시키는 함수
        fun bind(item: ContactData){
            val profileImage = view.findViewById<ImageView>(R.id.profile_Image)
            val itemName = view.findViewById<TextView>(R.id.item_name)
            val itemMobileNO = view.findViewById<TextView>(R.id.item_mobileNO)
            val itemBtn = view.findViewById<ImageView>(R.id.item_Btn)

            if (item.photo.contains("jpg")){
                val photoURL = Value.Photo_URL + item.photo
                Glide.with(context).load(photoURL).into(profileImage)
            }

            itemName.text = item.userNM
            itemMobileNO.text = item.mobileNO

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
            itemBtn.setOnClickListener { builder.show() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // View Binding 으로 하면 달라지는 부분 중 하나
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent,false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // RecyclerView는 ViewHolder를 데이터와 연결할 때 이 메서드를 호출한다.

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }

        holder.apply {
            bind(currentList[position])
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ContactData>() {
            override fun areContentsTheSame(oldItem: ContactData, newItem: ContactData) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ContactData, newItem: ContactData) =
                oldItem.idx == newItem.idx
        }
    }

}
