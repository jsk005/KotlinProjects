package com.link2me.android.sqlite

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.link2me.android.common.Utils
import java.util.*

class ContactRequest(context: Context, idx: String, listener: Response.Listener<String?>?) :
    StringRequest(Method.POST, URL, listener, null) {
    private val params: MutableMap<String, String>

    public override fun getParams(): Map<String, String> {
        return params
    }

    companion object {
        private const val URL = Value.IPADDRESS + "getContact.php"
    }

    init {
        params = HashMap()
        //params["idx"] = Utils.getDeviceId(context) // 스마트폰 고유장치번호
        params["idx"] = "1"
    }
}