package com.example.appshop.db.repository

import com.example.appshop.model.spoonacular.SpoonacularRecipe
import com.example.appshop.model.spoonacular.SpoonacularSummarizedRecipe
import com.example.appshop.network.SpoonacularRetrofitInstance
import com.example.appshop.network.SpoonacularService

class SpoonacularRepository(
    private val api: SpoonacularService
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

    suspend fun searchRecipeById(id: Int): Result<SpoonacularSummarizedRecipe?> {
        return try {
            val response = api.searchRecipeByID(id)
            if (response.isSuccessful) {
                val recipe = response.body()
                Result.success(recipe)
            } else {
                Result.failure(Exception("API error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}