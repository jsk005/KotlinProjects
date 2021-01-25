package com.link2me.android.sqlite.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.link2me.android.sqlite.model.ContactData

// DAO(Data Access Object)는 데이터베이스에 접근해서 DML(select, insert, update, delete) 쿼리를 실행하는 메소드의 모음이다.
@Dao
interface ContactsDao {
    // https://developer.android.com/training/data-storage/room?hl=ko 참조하면 도움된다.

    @Query("SELECT * FROM ContactData")
    fun getAll(): List<ContactData>

    @Query("SELECT * FROM ContactData WHERE userNM LIKE :search")
    fun findUserWithName(search: String): List<ContactData>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 동일한 값이 입력되었을 때 Update 쿼리로 실행된다.
    fun insert(contacts: ContactData)

    @Insert
    fun insertAll(vararg contacts: ContactData)

    @Delete
    fun delete(contacts: ContactData)
}