package com.link2me.viewmodel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.link2me.viewmodel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val TAG: String = "로그"
    }

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // 나중에 값이 설정될 것이라고 lateinit 으로 설정
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root) // View Bindg 과정

        // 뷰 모델 프로바이더를 통해 뷰모델 가져오기
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // 뷰모델이 가지고 있는 값의 변경사항을 관찰할 수 있는 라이브 데이터를 관찰한다
        mainViewModel.currentValue.observe(this, Observer {
            Log.d(TAG,"MainActivity - myNumberViewModel - CurrentValue 라이브 데이터 값 변경 : $it")
            binding.tvNumber.text = it.toString()
        })

        // 리스너 연결
        binding.btnPlus.setOnClickListener(this)
        binding.btnMinus.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val userInput: Int  = binding.etNumber.text.toString().toInt()

        // ViewModel 에 LiveData 값을 변경하는 메소드
        when(view){
            binding.btnPlus ->
                mainViewModel.updateValue(actionType = ActionType.PLUS, userInput)
            binding.btnMinus ->
                mainViewModel.updateValue(actionType = ActionType.MINUS, userInput)
        }

    }
}