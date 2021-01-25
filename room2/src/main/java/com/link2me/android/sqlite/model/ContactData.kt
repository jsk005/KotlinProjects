package com.link2me.android.sqlite.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

// Room 라이브러리는 @Entity 애노테이션이 적용된 클래스를 찾아 테이블로 변환한다.
@Entity(tableName = "ContactData", indices = [Index(value = ["idx"], unique = true)])
@Parcelize
data class ContactData (
    @PrimaryKey(autoGenerate = true)
    var _Id: Long,
    var idx: String,
    var userNM: String = "", // 성명
    var mobileNO: String = "", // 휴대폰 번호
    var telNO: String = "", // 사무실 번호
    var team: String = "", // 팀(소속)
    var mission: String = "", // 담당업무
    var position: String = "", // 직위
    var photo: String = "" // 이미지 경로를 String으로 받기 위해서
): Parcelable