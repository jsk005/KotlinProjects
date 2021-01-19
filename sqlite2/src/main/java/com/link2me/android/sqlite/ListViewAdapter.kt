package com.link2me.android.sqlite

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.link2me.android.sqlite.model.Address_Item

class ListViewAdapter(context: Context, items: MutableList<Address_Item>) : BaseAdapter() {
    var lvItemList: MutableList<Address_Item>
    var context: Context

    init {
        lvItemList = items
        this.context = context
    }

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
        val addressItem = lvItemList[position]
        if (addressItem.photo.contains("jpg")) {
            val photoURL = Value.Photo_URL + addressItem.photo
            Glide.with(context).load(photoURL).into(viewHolder.photo_Image!!)
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
                            { dialog, which ->
                                val intent = Intent(
                                    Intent.ACTION_CALL,
                                    Uri.parse("tel:" + PhoneNumberUtils.formatNumber(addressItem.mobileNO))
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
        viewHolder.child_btn!!.setOnClickListener { builder.show() }
        return convertView!!
    }

}
