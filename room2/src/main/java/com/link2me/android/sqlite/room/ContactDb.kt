package com.link2me.android.sqlite.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.link2me.android.sqlite.model.ContactData

// Room 은 구조화된 SQL 데이터베이스를 앱에 추가하는 과정을 단순화 해주는 퍼시스턴스 라이브러리이다.
// ORM(Object-Relation Mapping)은 객체와 관계형 데이터베이스의 데이터를 매핑하고 변환하는 기술로
// 복잡한 쿼리를 잘 몰라도 코드만으로 데이터베이스의 모든 것을 컨트롤할 수 있게 도와준다.

// Room 을 사용하면 클래스명이나 변수명 위에 @어노테이션을 사용해서 코드를 변환할 수 있다.
// 어노테이션은 클래스 변수를 테이블 열로, 메소드는 SQL 문으로 연관시켜 준다.
// 따라서 추가, 삭제, 변경, 쿼리를 하는 SQL문을 별도로 유지하지 않아도 된다.

@Database(entities = [ContactData::class], version = 1, exportSchema = false)
abstract class ContactDb : RoomDatabase() { // 추상 클래스로 생성해야 한다.
    // DAO 인터페이스의 구현체를 사용할 수 있는 메소드명을 정의한다.
    abstract fun contactsDao(): ContactsDao

    companion object {
        private var instance: ContactDb? = null

        @Synchronized
        fun getInstance(context: Context): ContactDb? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDb::class.java,
                    "PBbook"
                ).allowMainThreadQueries().build()
            }
            return instance
        }
    }
}