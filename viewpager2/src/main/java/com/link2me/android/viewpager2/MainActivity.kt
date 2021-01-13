package com.link2me.android.viewpager2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.link2me.android.common.BackPressHandler
import com.link2me.android.viewpager2.adapter.MyViewPagerAdapter
import com.link2me.android.viewpager2.fragment.HomeFragment
import com.link2me.android.viewpager2.fragment.MapViewFragment
import com.link2me.android.viewpager2.fragment.NotificationsFragment

class MainActivity : FragmentActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var backPressHandler: BackPressHandler

    lateinit var mViewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    var code: String =""

    val tabTitles = listOf<String>("Home", "공지", "지도", "알림")

    // https://developer.android.com/training/animation/screen-slide-2?hl=ko 을 참조하자.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(TAG, "onCreate")

        backPressHandler = BackPressHandler(this)

        mViewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tab_layout)

        // 프래그먼트를 생성하면서 값을 전달하는 방법으로 arguments 를 제공한다.
        // newInstance를 통해 Fragment에 data를 넣어준다
        val frag1: Fragment = HomeFragment.newInstance(code, "")
        val frag2: Fragment = NotificationsFragment.newInstance(code, "https://m.naver.com")
        val frag3: Fragment = MapViewFragment.newInstance(code, "")

        val myPagerAdapter = MyViewPagerAdapter(this)
        myPagerAdapter.addFrag(frag1)
        myPagerAdapter.addFrag(frag2)
        myPagerAdapter.addFrag(frag3)

        mViewPager.adapter = myPagerAdapter

        //displaying tabs
        TabLayoutMediator(tabLayout, mViewPager){ tab, position -> tab.text = tabTitles[position] }.attach()
    }

    override fun onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            backPressHandler.onBackPressed()
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
    }
}