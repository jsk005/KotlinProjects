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

class MapViewFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapview, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

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