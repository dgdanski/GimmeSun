package com.gimmesun

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

fun fetchData(): String? {
    val request: Request = Request.Builder()
        .url("http://api.openweathermap.org/data/2.5/forecast?q=Warsaw&appid=febf9e117b8a8ad155e6801eb6f971fd&units=metric&cnt=50")
        .build()

    val okHttpClient = OkHttpClient.Builder()
        .build()
    val call: Call = okHttpClient.newCall(request)
    val response: Response = call.execute()
    return response.body()?.string()
}