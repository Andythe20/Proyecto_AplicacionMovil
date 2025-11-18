// C:/Users/lamdo/Desktop/Proyecto_AplicacionMovil/app/src/main/java/com/example/appshop/ui/views/CartScreen.kt

package com.example.appshop.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.appshop.R // Asegúrate de importar tu R

// Modelo de datos simple para ejemplo de la vista
data class CartItem(
    val idProd: Int,
    val nombre: String,
    val precio: Int,
    var cantidad: Int,
    val imageResId: Int // Usando ID de recurso para el ejemplo
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen() {
    // --- DATOS DE EJEMPLO ---
    // En una app real, esto vendría de un *ViewModel*
    val listaProductos = remember {
        mutableStateListOf(
            CartItem(1, "Flan de Vainilla", 3200, 1, R.drawable.flan_vainilla),
            CartItem(2, "Flan de Chocolate", 1400, 2, R.drawable.flan_chocolate),
            CartItem(3, "Flan de Caramelo", 2500, 1, R.drawable.flan_caramelo)
        )
    }

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
        if (listaProductos.isEmpty()) {
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
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Recorremos cada
                    listaProductos.forEach{ item ->
                        CartItemCard(
                            item = item,
                            onQuantityChange = { newQuantity ->
                                // Lógica para actualizar la cantidad
                                val index = listaProductos.indexOf(item)
                                if (index != -1) {
                                    listaProductos[index] = item.copy(cantidad = newQuantity.coerceAtLeast(1))
                                }
                            },
                            onDelete = {
                                // Lógica para eliminar el item
                                listaProductos.remove(item)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Resumen y botón de pago
                OrderSummary(items = listaProductos)
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
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
            Image(
                painter = painterResource(id = item.imageResId),
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
                    quantity = item.cantidad,
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
fun OrderSummary(items: List<CartItem>) {
    val subtotal = items.sumOf { it.precio * it.cantidad }
    val costoEnvio = if (subtotal > 0) 5000 else 0
    val total = subtotal + costoEnvio

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

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", color = Color.Gray)
                Text(String.format("$ $subtotal"), fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Envío", color = Color.Gray)
                Text(String.format("$ $costoEnvio"), fontWeight = FontWeight.Medium)
            }

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