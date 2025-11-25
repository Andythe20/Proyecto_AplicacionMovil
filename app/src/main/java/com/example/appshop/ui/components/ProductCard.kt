package com.example.appshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.appshop.model.Product

@Composable
fun ProductCard(
    product: Product,
    onAddCart : () -> Unit
) {
    var mostrarToast by remember { mutableStateOf(false) }

    if (mostrarToast){
        AlertDialog(
            onDismissRequest = {
                mostrarToast = false
            },
            title = {
                Text("¡Producto agregado al carrito!", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Cuando quieras, revisa tu carrito para finalizar tu compra.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarToast = false
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(product.url),
                contentDescription = product.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.nombre, fontWeight = FontWeight.Bold)
            Text(product.categoria, style = MaterialTheme.typography.bodySmall)
            Text("$${product.precio} CLP", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onAddCart()
                    mostrarToast = true
                          },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Agregar al Carrito.")
            }
        }
    }
}