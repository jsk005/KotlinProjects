package com.link2me.android.activitychange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.link2me.android.activitychange.Changer.Companion.saveData
import com.link2me.android.activitychange.task.Task1Activity
import com.link2me.android.common.BackPressHandler

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context
    lateinit var ClassName: String
    lateinit var btn2Target: Button
    lateinit var backPressHandler: BackPressHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        // onCreate 메소드는 콜백 메소드로, C언어의 main()함수라고 생각하면 된다.
        // 콜백 메서드란, 내가 메서드를 실행하지 않아도 어떤 순간에 스스로 호출되는 메서드이다. 
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Layout을 화면에 표시해준다.
        // setContentView는 레이아웃 리소스를 인플레이팅하여 UI 레이아웃을 구성한다.
        // layout 폴더에 있는 activity_main.xml 파일을 찾는다.
        // 화면에 나타낼 View를 지정하고, XML Layout의 내용을 메모리상에 객체화한다.
        Log.e(TAG, "onCreate()")
        mContext = this@MainActivity
        backPressHandler = BackPressHandler(this)
        ClassName = this.localClassName

        // 데이터 묶어서 보내기
        val profile = Profiles("홍길동", 35, "남성")

        // activity_main.xml 에 정의한 위젯에 접근하려면 findViewById() 메소드를 사용하면 된다.
        // 모든 View 객체에는 정수 타입의 id를 설정할 수 있다. 이 id는 View를 식별하기 위한 식별자로 사용한다.
        // id는 전체 앱에서 고유할 필요는 없지만, 각 XML 안에서는 고유해야 한다.
        // 모듈 단위의 build.gradle 파일에 complileSdkVersion이 26이상이라면 
        // findViewById() 메서드 앞에 해당 View로 캐스팅하지 않아도 된다.
        val textView = findViewById<TextView>(R.id.maintextView)
        textView.text = "코틀린 메인화면"

        btn2Target = findViewById(R.id.mainbutton)
        btn2Target.setOnClickListener({ view: View? ->
            val intent = Intent(this@MainActivity, TargetActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("parcel", profile)
            intent.putExtra("classname", ClassName)
            startActivity(intent)
        })

        val btn2Sub = findViewById<Button>(R.id.btntoSub)
        btn2Sub.setOnClickListener { view: View? ->
            val intent = Intent(this@MainActivity, SubActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_interface).setOnClickListener { view: View? ->
            saveData(mContext, OtherActivity::class.java)
        }

        findViewById<View>(R.id.btn_move_activity).setOnClickListener { view: View? ->
            val changer = ActivityChanger()
            changer.saveData(mContext, OtherActivity::class.java)
        }

        findViewById<View>(R.id.btn_splash_togo).setOnClickListener { view: View? ->
            startActivity(Intent(mContext, SplashActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.btn_activity_task).setOnClickListener { v: View? ->
            // 안드로이드 생명주기에 대한 사항을 알아보기 위한 부분
            val intent = Intent(mContext, Task1Activity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 Task를 만들고 배치된다.
            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e(TAG, "onRestart()")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause()")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop()")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.e(TAG, "onNewIntent()")
        if (null != intent) {
            val defaultValue = 0
            setIntent(intent)
        }
    }

    override fun onBackPressed() {
        backPressHandler.onBackPressed()
    }
}