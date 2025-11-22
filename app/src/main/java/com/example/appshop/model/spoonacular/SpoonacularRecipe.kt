package com.example.appshop.model.spoonacular

data class SpoonacularRecipe(
    val id: Int,
    val title: String,
    val image: String
)

data class SpoonacularSummarizedRecipe(
    val id: Int,
    val title: String,
    val summary: String
)
