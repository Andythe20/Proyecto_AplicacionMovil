package com.example.appshop.network

import com.example.appshop.model.spoonacular.SpoonacularSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularService {

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") limit: Int = 10
    ): Response<SpoonacularSearchResponse>
}