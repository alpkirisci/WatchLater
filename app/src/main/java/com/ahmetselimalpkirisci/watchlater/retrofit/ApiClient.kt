package com.ahmetselimalpkirisci.watchlater.retrofit

import com.ahmetselimalpkirisci.watchlater.util.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//STEP1
object ApiClient {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Utils.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit!!.create(ApiService::class.java)
    }
}