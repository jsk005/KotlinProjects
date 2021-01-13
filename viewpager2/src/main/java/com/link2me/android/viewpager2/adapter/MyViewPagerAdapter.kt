package com.link2me.android.viewpager2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val TAG = this.javaClass.simpleName

    var mFragmentList = mutableListOf<Fragment>()

    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getItemCount(): Int = mFragmentList.size

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

}