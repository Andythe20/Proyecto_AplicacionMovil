@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.appshop.ui.views

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.appshop.utils.validateInputText
import com.example.appshop.utils.validateIntField

// === Scaffold con TopAppBar incluido ===
@Composable
fun CreateProfileView() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Perfil") }
            )
        }
    ) { innerPadding -> // padding del Scaffold
        CreateProfileScreen(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun CreateProfileScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }

    // === Permisos y launchers ===
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                context,
                "Se necesita permiso de cámara para tomar fotos",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> fotoUri = uri }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val savedUri = saveBitmapToGallery(context, bitmap)
            if (savedUri != null) {
                fotoUri = savedUri
                Toast.makeText(context, "Foto guardada en galería", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // === Vista previa circular ===
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (fotoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUri),
                    contentDescription = "Foto del producto",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = "Sin foto",
                    modifier = Modifier.size(92.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // === Botones de imagen ===
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val permissionStatus =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    camaraLauncher.launch()
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Icon(Icons.Filled.CameraAlt, contentDescription = "Cámara", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tomar Foto")
            }
            Button(onClick = { galeriaLauncher.launch("image/*") }) {
                Icon(Icons.Filled.PhotoLibrary, contentDescription = "Galería", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Abrir Galería")
            }
        }

        // === Campos de texto ===
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = validateInputText("Usuario", it, 3)
            },
            label = { Text("Usuario") },
            isError = usernameError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (usernameError != null) {
            Text(
                text = usernameError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { input ->
                if (input.matches(Regex("^\\d*\$"))) {
                    precio = input
                    precioError = validateIntField("Precio", input, 0)
                }
            },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        if (precioError != null) {
            Text(
                text = precioError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val formValid = usernameError == null &&
                precioError == null &&
                username.trim().isNotEmpty() &&
                precio.trim().isNotEmpty()

        Button(
            onClick = {
                Toast.makeText(context, "Producto guardado (solo UI)", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = formValid
        ) {
            Icon(Icons.Filled.Save, contentDescription = "Guardar", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar Producto")
        }
    }
}

// === Función auxiliar para guardar la imagen ===
fun saveBitmapToGallery(context: android.content.Context, bitmap: Bitmap): Uri? {
    val filename = "producto_${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it)?.use { outStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outStream)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(it, contentValues, null, null)
        }
    }
    return uri
}

// === Preview ===
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateProductScreenPreview() {
    CreateProfileView() // Previsualiza con Scaffold incluido
}
