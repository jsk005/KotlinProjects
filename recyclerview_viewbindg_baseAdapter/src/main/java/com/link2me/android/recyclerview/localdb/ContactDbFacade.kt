package com.link2me.android.recyclerview.localdb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.util.Log
import com.link2me.android.recyclerview.model.SQLite_Item
import java.util.*

class ContactDbFacade(context: Context) {
    private val TAG = this.javaClass.simpleName
    private val mHelper: ContactDBHelper
    var mContext: Context? = null

    fun InsertData_Phone(
        idx: String, name: String, mobileNO: String, officeNO: String, Team: String,
        Mission: String, Position: String, Photo: String, Status: String
    ) {
        val db = mHelper.writableDatabase // 쓰기 가능한 데이터베이스를 가져와 입력

        // 이름 + 휴대폰번호 기준으로 중복 체크
        val query = ("select idx from " + ContactContract.Entry.TABLE_NAME
                + " where " + ContactContract.Entry._NAME + "= '" + name + "' and " + ContactContract.Entry._MobileNO + "= '" + mobileNO + "'")
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst() // Cursor를 제일 첫행으로 이동
        if (cursor.count == 0) {  // 중복이 없으면 저장하라.
            val cv = ContentValues() // 객체 생성
            cv.put(ContactContract.Entry._IDX, idx)
            cv.put(ContactContract.Entry._NAME, name)
            cv.put(ContactContract.Entry._MobileNO, mobileNO)
            cv.put(ContactContract.Entry._telNO, officeNO)
            cv.put(ContactContract.Entry._Team, Team)
            cv.put(ContactContract.Entry._Mission, Mission)
            cv.put(ContactContract.Entry._Position, Position)
            cv.put(ContactContract.Entry._Photo, Photo)
            cv.put(ContactContract.Entry._Status, Status)
            db.beginTransaction() // 대량건수 데이터 입력 처리를 고려
            try {
                val rowId = db.insert(ContactContract.Entry.TABLE_NAME, null, cv)
                if (rowId < 0) {
                    throw SQLException("Fail to Insert")
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.i(TAG, e.toString())
            } finally {
                db.endTransaction()
                Log.v(TAG, "DB Inserted $name idx =$idx")
            }
        }
        cursor.close()
        db.close()
    }

    fun InsertData(
        idx: String, userNM: String, mobileNO: String, officeNO: String, Team: String,
        Mission: String, Position: String, Photo: String, Status: String
    ) {
        val db = mHelper.writableDatabase // 쓰기 가능한 데이터베이스를 가져와 입력
        val query = "select idx from ${ContactContract.Entry.TABLE_NAME} where ${ContactContract.Entry._IDX} = '${idx}'"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst() // Cursor를 제일 첫행으로 이동
        if (cursor.count == 0) {  // 중복이 없으면 저장하라.
            val cv = ContentValues() // 객체 생성
            cv.put(ContactContract.Entry._IDX, idx)
            cv.put(ContactContract.Entry._NAME, userNM)
            cv.put(ContactContract.Entry._MobileNO, mobileNO)
            cv.put(ContactContract.Entry._telNO, officeNO)
            cv.put(ContactContract.Entry._Team, Team)
            cv.put(ContactContract.Entry._Mission, Mission)
            cv.put(ContactContract.Entry._Position, Position)
            cv.put(ContactContract.Entry._Photo, Photo)
            cv.put(ContactContract.Entry._Status, Status)
            db.beginTransaction() // 대량건수 데이터 입력 처리를 고려
            try {
                val rowId = db.insert(ContactContract.Entry.TABLE_NAME, null, cv)
                if (rowId < 0) {
                    throw SQLException("Fail to Insert")
                }
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                Log.i(TAG, e.toString())
            } finally {
                db.endTransaction()
                Log.v(TAG, "DB Inserted $userNM uid =$idx")
            }
        }
        cursor.close()
        db.close()
    }// Cursor를 제일 첫행으로 이동

    /* Get the first row Column_ID from the table */
    val firstId: Int
        get() {
            var idToUpdate = 0
            val query = "select idx from ${ContactContract.Entry.TABLE_NAME} LIMIT 1"
            val db = mHelper.readableDatabase
            val res = db.rawQuery(query, null)
            if (null != res && res.count > 0) {
                res.moveToFirst() // Cursor를 제일 첫행으로 이동
                idToUpdate = res.getInt(0)
            }
            return idToUpdate
        }

    /* Update the table row with Column_ID - id */
    fun updateDB(idx: Int, name: String?, mobileNO: String?, officeNO: String?): Boolean {
        Log.i(TAG, "Updating Column_ID : $idx")
        val cv = ContentValues()
        cv.put(ContactContract.Entry._NAME, name)
        cv.put(ContactContract.Entry._MobileNO, mobileNO)
        cv.put(ContactContract.Entry._telNO, officeNO)
        val db = mHelper.writableDatabase
        db.update(ContactContract.Entry.TABLE_NAME, cv, "idx = ? ", arrayOf(Integer.toString(idx)))
        return true
    }

    fun updateDB(
        idx: String, userNM: String, mobileNO: String, officeNO: String, Team: String,
        Mission: String, Position: String, Photo: String, Status: String
    ): Boolean {
        Log.i(TAG, "Updating Column_ID : $idx")
        val cv = ContentValues()
        cv.put(ContactContract.Entry._NAME, userNM)
        cv.put(ContactContract.Entry._MobileNO, mobileNO)
        cv.put(ContactContract.Entry._telNO, officeNO)
        cv.put(ContactContract.Entry._Team, Team)
        cv.put(ContactContract.Entry._Mission, Mission)
        cv.put(ContactContract.Entry._Position, Position)
        cv.put(ContactContract.Entry._Photo, Photo)
        cv.put(ContactContract.Entry._Status, Status)
        val db = mHelper.writableDatabase
        db.update(ContactContract.Entry.TABLE_NAME, cv, "idx = ? ", arrayOf(idx))
        return true
    }

    /* Delete the row with Column_ID - id from the employees table */
    fun deleteRow(idx: String): Int {
        val db = mHelper.writableDatabase
        val selection = ContactContract.Entry._IDX + " = ?"
        return db.delete(ContactContract.Entry.TABLE_NAME, selection, arrayOf(idx))
    }

    val tableRowCount: Int
        get() {
            val countQuery = "SELECT * FROM " + ContactContract.Entry.TABLE_NAME
            val db = mHelper.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            Log.i(TAG, "Total Row : " + cursor.count)
            cursor.close()
            return cursor.count
        } // ArrayList에 추가

    // return ArrayList
    // Select All Query
    // SQLiteDB 모든 데이터 ArrayList 에 저장하기
    val allSQLiteData: ArrayList<SQLite_Item>
        get() {
            val sqliteDBData = ArrayList<SQLite_Item>()
            // Select All Query
            val selectQuery = "SELECT * FROM ${ContactContract.Entry.TABLE_NAME}"
            val db = mHelper.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null && cursor.count > 0) {
                do {
                    // 입력 변수 불일치 확인 필요
                    val item = SQLite_Item(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)
                    )
                    sqliteDBData.add(item) // ArrayList에 추가
                } while (cursor.moveToNext())
            }
            cursor!!.close()
            return sqliteDBData // return ArrayList
        }

    fun LoadSQLiteDBCursor(): Cursor? {
        val db = mHelper.readableDatabase
        //db.beginTransaction();
        // Select All Query
        val selectQuery = "SELECT idx,userNM,mobileNO,telNO,Team,Mission,Position,photo,status FROM ${ContactContract.Entry.TABLE_NAME}"
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
            // db.query 사용시에는 방법이 다르다.
            // https://developer.android.com/training/data-storage/sqlite?hl=ko#java 참조하면 사용법 나온다.
            //db.setTransactionSuccessful();
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            //db.endTransaction();
        }
        return cursor
    }

    fun SelectPhoneNO(Number: String): Cursor? {
        var Number = Number
        val db = mHelper.readableDatabase
        Number = Number.replace("[^0-9]".toRegex(), "") // 숫자를 제외한 문자열 제거
        db.beginTransaction()
        val selectQuery = "SELECT userNM,mobileNO,telNO,Team,Mission,Position FROM ${ContactContract.Entry.TABLE_NAME}" +
                    " where ${ContactContract.Entry._MobileNO} = '${Number}' or ${ContactContract.Entry._telNO} = '${Number}'"
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
        return cursor
    }

    init {
        mHelper = ContactDBHelper.getInstance(context)!! // Helper 객체 생성
        mContext = context
    }
}