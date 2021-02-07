// 코틀린 형식에 얽매이지 않고 필요한 유틸들을 적어두기 위한 용도

// 스마트폰의 인터넷 사용 IP주소
fun getMyIPaddress(){
	val gson = GsonBuilder().setLenient().create()
	val retrofit = Retrofit.Builder()
		.baseUrl("http://checkip.amazonaws.com/")
		.addConverterFactory(GsonConverterFactory.create(gson))
		.build()
	val service = retrofit.create(RetrofitService.IRetrofit::class.java)

	service.getMyIPAddress().enqueue(object: Callback<String> {
		override fun onResponse(call: Call<String>, response: Response<String>) {
			response.body()?.let {
				PrefsHelper.write("myIPaddress",it) // 평문 저장
				Log.e(TAG,"myIPaddress : ${it}")
			}
		}
		override fun onFailure(call: Call<String>, t: Throwable) {

		}
	})
}

// 스마트폰의 Local Public IP address
CoroutineScope(Dispatchers.Default).launch {
	// 새로운 CoroutineScope 로 동작하는 백그라운드 작업
	val mysocket = Socket("findip.kr", 80)
	MyIPAddress = mysocket.getLocalAddress().getHostAddress()
	Log.e(TAG, "ip address : ${MyIPAddress}")
	Log.e(TAG,"mobile ip address : ${Utils.getIPAddress(true)}")
}

