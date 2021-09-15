package com.link2me.android.common

import android.content.Context
import android.content.SharedPreferences

class PrefsHelper private constructor(private val mContext: Context) {
    companion object {
        const val PREFERENCE_NAME = "pref"
        private var prefs: SharedPreferences? = null
        private var prefsEditor: SharedPreferences.Editor? = null
        private var instance: PrefsHelper? = null

        @Synchronized
        fun init(context: Context): PrefsHelper? {
            if (instance == null) instance = PrefsHelper(context)
            return instance
        }

        fun read(key: String?, defValue: String?): String? {
            return prefs?.getString(key, defValue)
        }

        fun write(key: String?, value: String?) {
            prefsEditor?.putString(key, value)
            prefsEditor?.commit()
        }

        fun read(key: String?, defValue: Int): Int {
            return prefs!!.getInt(key, defValue)
        }

        fun write(key: String?, value: Int?) {
            prefsEditor!!.putInt(key, value!!).commit()
        }

        fun read(key: String?, defValue: Boolean): Boolean {
            return prefs!!.getBoolean(key, defValue)
        }

        fun write(key: String?, value: Boolean) {
            prefsEditor!!.putBoolean(key, value)
            prefsEditor!!.commit()
        }
    }

    init {
        prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        prefsEditor = prefs?.edit()
    }
}