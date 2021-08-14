package com.link2me.android.recyclerview.localdb

import android.provider.BaseColumns

object ContactContract {
    const val _RESULTS = "result" // 서버 정보를 파싱하기 위한 변수 선언
    const val _CallingNO = "callingNO" // 전화 수신
    const val _CallingTime = "received_time"

    object Entry : BaseColumns {
        // BaseColumns 인터페이스를 구현함으로써 내부 클래스는 _ID라고 하는 기본 키 필드를 상속할 수 있다.
        const val TABLE_NAME = "PBbook"
        const val _IDX = "idx" // 서버 테이블의 실제 필드명
        const val _NAME = "userNM"
        const val _MobileNO = "mobileNO"
        const val _telNO = "telNO"
        const val _Team = "Team"
        const val _Mission = "Mission"
        const val _Position = "Position"
        const val _Photo = "photo" // 이미지 필드
        const val _Status = "status"
    } // SQLite의 데이터 타입은 NULL, INTEGER, REAL, TEXT, BLOB 만 지원한다.
}