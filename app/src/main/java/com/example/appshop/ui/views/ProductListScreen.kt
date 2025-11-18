package com.example.appshop.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appshop.ui.components.ProductCard
import com.example.appshop.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(),
    navController: NavController

) {
    val products by viewModel.productos.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(title = {Text("Productos")})
        }
    ) { padding ->
        LazyColumn (
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ){
            items(products) {
                product -> ProductCard(product)
            }
        }
    }
}