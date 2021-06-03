package com.link2me.expandablerv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.link2me.expandablerv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // https://www.geeksforgeeks.org/how-to-create-expandable-recyclerview-items-in-android-using-kotlin/ 사이트 소스를 수정

    // view binding for the activity
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // get reference to the adapter class
    private var languageList = ArrayList<DataItem>()
    private lateinit var expandableAdapter: ExpandableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // define layout manager for the Recycler view
        binding.rvList.layoutManager = LinearLayoutManager(this)
        // attach adapter to the recyclerview
        expandableAdapter = ExpandableAdapter(languageList)

        getData()

        binding.rvList.adapter = expandableAdapter
    }

    private fun getData() {
        // 서버에서 가져온 데이터라고 가정한다.
        // create new objects and add some row data
        val language1 = DataItem(
            "Java",
            "Java is an Object Oriented Programming language." +
                    " Java is used in all kind of applications like Mobile Applications (Android is Java based), " +
                    "desktop applications, web applications, client server applications, enterprise applications and many more. ",
            false
        )
        val language2 = DataItem(
            "Kotlin",
            "Kotlin is a statically typed, general-purpose programming language" +
                    " developed by JetBrains, that has built world-class IDEs like IntelliJ IDEA, PhpStorm, Appcode, etc.",
            false
        )
        val language3 = DataItem(
            "Python",
            "Python is a high-level, general-purpose and a very popular programming language." +
                    " Python programming language (latest Python 3) is being used in web development, Machine Learning applications, " +
                    "along with all cutting edge technology in Software Industry.",
            false
        )
        val language4 = DataItem(
            "CPP",
            "C++ is a general purpose programming language and widely used now a days for " +
                    "competitive programming. It has imperative, object-oriented and generic programming features. ",
            false
        )

        // add items to list
        languageList.add(language1)
        languageList.add(language2)
        languageList.add(language3)
        languageList.add(language4)

        expandableAdapter.notifyDataSetChanged()

    }

    // on destroy of view make the binding reference to null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}