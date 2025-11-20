package com.example.appshop.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appshop.db.repository.SpoonacularRepository
import com.example.appshop.model.spoonacular.SpoonacularRecipe
import kotlinx.coroutines.launch

data class RecipesState(
    val recipes: List<SpoonacularRecipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipesViewModel(
    private val repository: SpoonacularRepository = SpoonacularRepository()
) : ViewModel() {

    var state by mutableStateOf(RecipesState())
        private set

    fun search(query: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = repository.searchRecipes(query)
            when {
                result.isSuccess -> {
                    state = state.copy(
                        recipes = result.getOrDefault(emptyList()),
                        isLoading = false
                    )
                }

                result.isFailure -> {
                    state = state.copy(
                        error = result.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }


    fun searchRandom(){
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = repository.searchRandomRecipes()
            when {
                result.isSuccess -> {
                    state = state.copy(
                        recipes = result.getOrDefault(emptyList()),
                        isLoading = false
                    )
                }

                result.isFailure -> {
                    state = state.copy(
                        error = result.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}
