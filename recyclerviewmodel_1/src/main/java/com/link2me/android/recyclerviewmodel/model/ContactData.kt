package com.link2me.android.recyclerviewmodel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactData (
    // PersonData 정보를 담고 있는 객체 생성
    var idx: String,
    var userNM: String = "",
    var mobileNO: String = "",
    var officeNO: String = "",
    var photo: String = "", // 이미지 경로를 String으로 받기 위해서
    var isCheckBoxState: Boolean = false
): Parcelable