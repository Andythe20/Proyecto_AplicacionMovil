package com.example.appshop.db.repository

import com.example.appshop.model.spoonacular.SpoonacularRecipe
import com.example.appshop.network.SpoonacularRetrofitInstance
import com.example.appshop.network.SpoonacularService

class SpoonacularRepository(
    private val api: SpoonacularService = SpoonacularRetrofitInstance.api
) {

    suspend fun searchRecipes(query: String): Result<List<SpoonacularRecipe>> {
        return try {
            val response = api.searchRecipes(query)
            if (response.isSuccessful) {
                val recipes = response.body()?.results ?: emptyList()
                Result.success(recipes)
            } else {
                Result.failure(Exception("API error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchRandomRecipes(): Result<List<SpoonacularRecipe>>{
        return try {
            val response = api.randomRecipes()
            if (response.isSuccessful) {
                val recipes = response.body()?.results ?: emptyList()
                Result.success(recipes)
            } else {
                Result.failure(Exception("API error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}