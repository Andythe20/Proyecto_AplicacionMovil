package com.example.appshop.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appshop.db.repository.SpoonacularRepository
import com.example.appshop.model.spoonacular.SpoonacularRecipe
import com.example.appshop.model.spoonacular.SpoonacularSummarizedRecipe
import com.example.appshop.network.SpoonacularRetrofitInstance
import com.example.appshop.network.SpoonacularService
import kotlinx.coroutines.launch

data class RecipesState(
    val recipes: List<SpoonacularRecipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class RecipeDetailState(
    val recipe: SpoonacularSummarizedRecipe? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipesViewModel() : ViewModel() {

    private val repository: SpoonacularRepository

    // --- Propiedades para el estado del detalle de la receta ---
    private val _recipeDetailState = mutableStateOf(RecipeDetailState())
    val recipeDetailState: State<RecipeDetailState> = _recipeDetailState // Esta línea ahora compilará

    // --- Propiedades para el estado de la lista de recetas ---
    private val _recipesListState = mutableStateOf(RecipesState())
    val recipesListState: State<RecipesState> = _recipesListState // Esta línea también compilará


    init {
        val api: SpoonacularService = SpoonacularRetrofitInstance.api
        repository = SpoonacularRepository(api)
        search("")
    }


    fun search(query: String) {
        viewModelScope.launch {
            _recipesListState.value = _recipesListState.value.copy(isLoading = true, error = null)
            val result = repository.searchRecipes(query)
            when {
                result.isSuccess -> {
                    _recipesListState.value = _recipesListState.value.copy(
                        recipes = result.getOrDefault(emptyList()),
                        isLoading = false
                    )
                }
                result.isFailure -> {
                    _recipesListState.value = _recipesListState.value.copy(
                        error = result.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }


    fun searchRecipeById(id: Int){
        viewModelScope.launch {
            _recipeDetailState.value = _recipeDetailState.value.copy(isLoading = true, error = null)
            val result = repository.searchRecipeById(id)
            when {
                result.isSuccess -> {
                    _recipeDetailState.value = _recipeDetailState.value.copy(
                        recipe = result.getOrDefault(null),
                        isLoading = false
                    )
                }
                result.isFailure -> {
                    _recipeDetailState.value = _recipeDetailState.value.copy(
                        error = result.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun clearRecipeDetail() {
        _recipeDetailState.value = RecipeDetailState()
    }
}
