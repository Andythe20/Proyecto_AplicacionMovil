package com.example.appshop.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appshop.ui.components.RecipeCard
import com.example.appshop.viewmodel.RecipesViewModel

@Composable
fun RecipeSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipesViewModel,
    navController: NavController
) {
    val state by viewModel.recipesListState
    var query by remember { mutableStateOf("") }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar recetas") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Boton buscar
        Button(
            onClick = { viewModel.search(query) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.error?.let { errorMsg ->
                Text(
                    text = "Error: $errorMsg",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Ahora puedes acceder a 'state.recipes' directamente
                items(state.recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onRecipeClick = { recipeId ->
                            navController.navigate("recipeDetail/$recipeId")
                        }
                    )
                }
            }
        }
    }
}
