// C:/Users/lamdo/Desktop/Proyecto_AplicacionMovil/app/src/main/java/com/example/appshop/ui/views/CartScreen.kt

package com.example.appshop.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appshop.R // Asegúrate de importar tu R
import com.example.appshop.model.Product
import com.example.appshop.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel) {
    val productosEnCarrito by cartViewModel.productosEnCarrito.collectAsState()
    val total by cartViewModel.total.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        // Para cuando el carrito este vacio
        if (productosEnCarrito.isEmpty()) {
            EmptyCartView(modifier = Modifier.padding(paddingValues))
        } else {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState) // Hacemos que toda la columna sea desplazable
            ) {
                // Lista de items del carrito
                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // Esto asegura que la lista ocupe el espacio y el resumen quede abajo
                        .padding(horizontal = 16.dp)
                ) {
                    // Recorremos el mapa que viene del viewModel
                    items(productosEnCarrito.toList()){ (product, cantidad) ->
                        CartItemCard(
                            item = product,
                            cantidad = cantidad,
                            onQuantityChange = { newQuantity ->
                                cartViewModel.cambiarCantidad(product, newQuantity)
                            },
                            onDelete = {
                                // Lógica para eliminar el item
                                cartViewModel.eliminarProducto(product)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Resumen y botón de pago
                OrderSummary(total)
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: Product,
    cantidad: Int,
    onQuantityChange : (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.url,
                contentDescription = item.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = String.format("$ ${item.precio}"), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                QuantitySelector(
                    quantity = cantidad,
                    onQuantityChange = onQuantityChange
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .padding(horizontal = 8.dp)
    ) {
        IconButton(onClick = { onQuantityChange(quantity - 1) }, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "Restar uno")
        }
        Text(
            text = quantity.toString(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = { onQuantityChange(quantity + 1) }, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Añadir uno")
        }
    }
}

// Calcular total, subtotal y costo de envio
@Composable
fun OrderSummary(total: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Resumen de la Orden",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)

            Spacer(modifier = Modifier.height(16.dp))

//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text("Subtotal", color = Color.Gray)
//                Text(String.format("$ $subtotal"), fontWeight = FontWeight.Medium)
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text("Envío", color = Color.Gray)
//                Text(String.format("$ $costoEnvio"), fontWeight = FontWeight.Medium)
//            }

            // Dibuja una linea horizontal
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(String.format("$ $total"), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Navegar al checkout */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("PROCEDER AL PAGO", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun EmptyCartView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.shopping_cart),
            contentDescription = "Carrito Vacío",
            modifier = Modifier.size(200.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu carrito está vacío",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "¡Añade algunos postres para empezar!",
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}