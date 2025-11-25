package com.example.appshop.network

import com.example.appshop.model.spoonacular.SpoonacularSearchResponse
import com.example.appshop.model.spoonacular.SpoonacularSummarizedRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularService {

    @GET("recipes/complexSearch") // añade el endpoint a la URL base
    suspend fun searchRecipes(
        // Parametros de la consulta
        @Query("query") query: String,
        @Query("number") limit: Int = 10
    ): Response<SpoonacularSearchResponse>

    @GET("recipes/{id}/summary")
    suspend fun searchRecipeByID(
        @Path("id") id: Int
    ): Response<SpoonacularSummarizedRecipe>
}

