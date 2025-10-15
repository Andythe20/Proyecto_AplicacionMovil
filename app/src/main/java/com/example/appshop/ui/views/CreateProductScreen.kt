@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.appshop.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun CreateProductScreen() {

    val context = LocalContext.current
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Se necesita permiso de cámara para tomar fotos", Toast.LENGTH_LONG).show()
        }
    }

    // Estados para los campos de texto y la imagen
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }

    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Lanzadores
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> fotoUri = uri }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap -> fotoBitmap = bitmap }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Producto") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // === VISUALIZAR FOTO ===
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                when {
                    fotoBitmap != null -> Image(
                        bitmap = fotoBitmap!!.asImageBitmap(),
                        contentDescription = "Foto del producto (cámara)",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    fotoUri != null -> Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(fotoUri)
                                .build()
                        ),
                        contentDescription = "Foto del producto (galería)",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    else -> Text("Sin foto", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // === BOTONES FOTO ===
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        camaraLauncher.launch()
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text("Tomar Foto")
                }
                Button(onClick = { galeriaLauncher.launch("image/*") }) {
                    Text("Abrir galería")
                }
            }

            // === CAMPOS DE TEXTO ===
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = {
                    if (it.matches(Regex("^\\d*\\.?\\d*\$"))) precio = it
                },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // === BOTÓN GUARDAR ===
            Button(
                onClick = {
                    // TODO: Guardar en base de datos más adelante
                    // val producto = Producto(nombre, descripcion, precio.toDouble(), codigo, fotoUri o fotoBitmap)
                    // viewModel.guardarProducto(producto)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar producto")
            }
        }
    }
}
