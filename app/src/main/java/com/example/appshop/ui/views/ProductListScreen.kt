package com.example.appshop.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appshop.ui.components.ProductCard
import com.example.appshop.viewmodel.ProductViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(),
    navController: NavController
) {
    val products by viewModel.products.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(errorMessage){
        errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Productos") })
        }
    ) { padding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.refreshProducts() },  // Aquí llamas a tu lógica de refresh
            modifier = Modifier.padding(padding)
        ) {
            if (products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay productos disponibles")
                }
            } else {
                LazyColumn(
                    contentPadding = padding,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    items(products) { product ->
                        ProductCard(product)
                    }
                }
            }
        }
    }
}
