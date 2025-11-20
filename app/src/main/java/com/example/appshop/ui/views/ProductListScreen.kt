package com.example.appshop.ui.views

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appshop.model.Product
import com.example.appshop.network.NetworkMonitor
import com.example.appshop.ui.components.ProductCard
import com.example.appshop.viewmodel.ProductViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(),
    navController: NavController
) {
    val products by viewModel.products.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val context = LocalContext.current

    var wasConnected by remember { mutableStateOf(true) }
    val isConnected by NetworkMonitor
        .networkStatus(context)
        .collectAsState(initial = true)

    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            Toast.makeText(context, "Se ha perdido la conexión a Internet", Toast.LENGTH_LONG).show()
        } else if (!wasConnected) {

            if (!loading && !isRefreshing) {
                Toast.makeText(context, "Conexión restaurada", Toast.LENGTH_LONG).show()
                viewModel.refreshProducts()
            }
        }
        wasConnected = isConnected
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Productos") }
            )
        }
    ) { padding ->

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                if (isConnected) {
                    viewModel.refreshProducts()
                } else {
                    Toast.makeText(context, "No hay conexión para refrescar", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(padding)
        ){
            AnimatedVisibility(
                visible = products.isEmpty() && !loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EmptyState(
                    isConnected = isConnected,
                    onRetry = {
                        if (isConnected) {
                            viewModel.loadProducts()
                        } else {
                            Toast.makeText(context, "No se detecta acceso a Internet", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            AnimatedVisibility(
                visible = products.isNotEmpty() && !loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ProductListContent(products, onEndReached = { viewModel.refreshProducts()})
            }

            AnimatedVisibility(
                visible = loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FullScreenLoading()
            }
        }
    }
}

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(
    isConnected: Boolean,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = "Error",
            tint = Color.Gray,
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isConnected)
                "No pudimos cargar los productos"
            else
                "Sin conexión a internet",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onRetry) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Reintentar"

            )
        }
    }
}

@Composable
fun ProductListContent(
    products: List<Product>,
    onEndReached: () -> Unit
) {
    val listState = rememberLazyListState()

    // Detecta cuando llega al final
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ==
                    listState.layoutInfo.totalItemsCount - 1
        }.collect { reachedEnd ->
            if (reachedEnd) {
                onEndReached()
            }
        }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        items(products) { product ->
            ProductCard(product)
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
