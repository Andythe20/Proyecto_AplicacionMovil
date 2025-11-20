package com.example.appshop.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {
    // 10.0.2.2 es la IP especial que usa el emulador de Android para referirse al localhost
    private const val BASE_URL = "http://34.204.118.73/api/v1/"

    val api: IApiService by lazy {
        // Crear el interceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Lo configuramos en BODY para ver toda la información posible
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Crear el cliente de OkHttp y añadirle el interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()


        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IApiService::class.java)
    }
}