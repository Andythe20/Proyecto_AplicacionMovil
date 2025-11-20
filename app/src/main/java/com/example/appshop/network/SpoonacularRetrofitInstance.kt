package com.example.appshop.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpoonacularRetrofitInstance {
    private const val BASE_URL = "https://api.spoonacular.com/"

    private const val API_KEY = "8ea918db1ac2451ca4840de0baff7186"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiKeyInterceptor = { chain: Interceptor.Chain ->
        val original = chain.request()
        val originalUrl = original.url

        // Añadir el apiKey automáticamente a TODAS las requests
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("apiKey", API_KEY)
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(apiKeyInterceptor)
        .build()

    val api: SpoonacularService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpoonacularService::class.java)
    }

}
