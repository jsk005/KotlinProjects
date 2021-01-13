package com.link2me.android.viewpager2.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.link2me.android.viewpager2.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private lateinit var rootView: View

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "F onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "F onCreateView")
        // Inflate the layout for this fragment ( 사용자 인터페이스와 관련된 뷰를 초기화하기 위해 사용)
        // Fragment가 인터페이스를 처음으로 그릴 때 호출된다.
        // inflater -> 뷰를 그려주는 역할,
        // container -> Fragment Layout 이 배치되는 부모 레이아웃
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    // 번들을 생성한 후 전달할 값을 담는다.
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "F onAttach")
        // 객체지향의 설계 구조로 인해 onAttach 메소드를 통해 넘어오는 Context에서만 상위 Activity를 꺼낼 수 있다.
        // API 23 이상부터는 Context만 받도록 변경되었다.
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "F onStart")
        // 화면에서 사라졌다 다시 나타나면 onStart()만 호출된다.
        // 주로 화면 생성 후에 화면에 입력될 값을 초기화하는 용도로 사용한다.
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "F onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "F onPause")
        // 현재 프래그먼트가 화면에서 사라지면 호출된다.
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "F onStop")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "F onDestroyView")
        // 뷰의 초기화를 해제하는 용도로 사용한다.
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "F onDestroy")
        // Activity에는 아직 남아있지만 프래그먼트 자체는 소멸된다.
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "F onDetach")
        // Activity에서 연결이 해제된다.
    }

}