package com.example.appshop.network

import com.example.appshop.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface IApiService {
    @GET("products")
    suspend fun getProducts(): Response<List<Product>>
}