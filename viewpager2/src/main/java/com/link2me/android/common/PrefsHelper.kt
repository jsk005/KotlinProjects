package com.link2me.android.common

import android.content.Context
import android.content.SharedPreferences

class PrefsHelper(val context: Context) {

    init {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        prefsEditor = prefs.edit()
    }

    companion object {
        const val PREFERENCE_NAME = "pref"
        private lateinit var prefs: SharedPreferences
        private lateinit var prefsEditor: SharedPreferences.Editor
        private var instance: PrefsHelper? = null

        @Synchronized
        fun init(context: Context): PrefsHelper? {
            if (instance == null) instance = PrefsHelper(context)
            return instance
        }

        fun read(key: String?, defValue: String?): String? {
            return prefs.getString(key, defValue)
        }

        fun write(key: String?, value: String?) {
            prefsEditor.putString(key, value)
            prefsEditor.commit()
        }

        fun read(key: String?, defValue: Int): Int? {
            return prefs.getInt(key, defValue)
        }

        fun write(key: String?, value: Int?) {
            prefsEditor.putInt(key, value!!).commit()
        }

        fun read(key: String?, defValue: Boolean): Boolean {
            return prefs.getBoolean(key, defValue)
        }

        fun write(key: String?, value: Boolean) {
            prefsEditor.putBoolean(key, value)
            prefsEditor.commit()
        }
    }

}