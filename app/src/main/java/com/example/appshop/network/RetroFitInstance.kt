package com.example.appshop.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {
    // 10.0.2.2 es la IP especial que usa el emulador de Android para referirse al localhost
    private const val BASE_URL = "http://10.0.2.2:8080/api/v1"

    val api: IApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IApiService::class.java)
    }
}