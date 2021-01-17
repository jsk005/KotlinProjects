package com.link2me.android.sqlite.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SQLite_Item (
    var idx: String, // 서버 idx 칼럼
    var userNM: String, // 성명
    var mobileNO: String = "", // 휴대폰 번호
    var telNO: String = "", // 사무실 번호
    var team: String = "", // 팀(소속)
    var mission: String = "", // 담당업무
    var position: String = "", // 직위
    var photo: String = "", // 사진
    var status: String = "" // 서버 데이터와 동기화를 위한 체크 필드
): Parcelable
