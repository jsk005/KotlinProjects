package com.link2me.android.viewpager2.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.link2me.android.viewpager2.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NotificationsFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private lateinit var rootView: View
    private lateinit var mWebView: WebView
    private lateinit var mWebSettings: WebSettings
    var url: String? = null

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notification, container, false)
        mWebView = rootView.findViewById(R.id.webview_notice); // Layout 와의 연결
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myWebView()
    }

    private fun myWebView() {
        url = param2
        mWebSettings = mWebView.settings // 세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true) // 자바스크립트 사용 허용
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE)
        mWebSettings.setBuiltInZoomControls(true) // 화면 확대 축소 허용 여부
        mWebSettings.setDisplayZoomControls(false)
        mWebSettings.setSupportZoom(true) // 줌 사용 여부
        if (Build.VERSION.SDK_INT >= 21) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
        }
        mWebView.loadUrl(url)
        mWebView.setWebChromeClient(WebChromeClient())
        mWebView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->

            // WebView 에서 뒤로 가기 버튼을 눌렀을 때 동작하도록 하는 코드 구현
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action === MotionEvent.ACTION_UP && mWebView.canGoBack()) {
                handler.sendEmptyMessage(1)
                return@OnKeyListener true
            }
            false
        })
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                1 -> webViewGoBack()
            }
        }
    }

    private fun webViewGoBack() {
        mWebView.goBack()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // 아래 프래그먼트 생명주기 관련 메소드는 실제 코드 구현시에는 불필요할 수 있다.

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "F onAttach")
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "F onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "F onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "F onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "F onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "F onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "F onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "F onDetach")
    }

}