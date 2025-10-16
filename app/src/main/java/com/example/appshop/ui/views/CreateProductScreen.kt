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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
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


@Composable
fun CreateProductScreen() {

    // Contexto de la aplicación
    val context = LocalContext.current

    // === Estados de los campos ===
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    // === Estados de error ===
    var nombreError by remember { mutableStateOf<String?>(null)}
    var precioError by remember { mutableStateOf<String?>(null)}

    // === Permisos y Lauchers ===
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Se necesita permiso de cámara para tomar fotos", Toast.LENGTH_LONG).show()
        }
    }

    // === Selector de galería ===
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        fotoUri = uri // reemplaza lo que haya
    }

    // === Cámara ===
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            // Guardar en galería y obtener su Uri
            val savedUri = saveBitmapToGallery(context, bitmap)
            if (savedUri != null) {
                fotoUri = savedUri // reemplaza lo que haya
                Toast.makeText(context, "Foto guardada en galería", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

        Scaffold(
            topBar = { TopAppBar(title = { Text("Crear Producto") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            // === Vista previa ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
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
                    //Text("Sin foto", style = MaterialTheme.typography.bodyMedium)
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
                    val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        camaraLauncher.launch()
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Cámara",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tomar Foto")
                }
                Button(onClick = { galeriaLauncher.launch("image/*") })
                {
                    Icon(
                        imageVector = Icons.Filled.PhotoLibrary,
                        contentDescription = "Galería",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Abrir Galería")
                }
            }

            // === Campo: Nombre ===
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = validateInputText("Nombre", it, 3)
                },
                label = { Text("Nombre") },
                isError = nombreError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Mensaje al usuario en caso de no cumplir validación
            if(nombreError != null){
                Text(
                    text = nombreError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                },
                label = { Text("Descripción") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = {
                    input ->
                    if (input.matches(Regex("^\\d*\$"))){
                        precio = input
                        precioError = validateIntField("Precio", input, 0)
                    }
                },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // Mensaje en caso de no cumplir validación
            if(precioError != null){
                Text(
                    text = precioError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Formulario válido solo si no hay errores y los campos obligatorios no están vacíos
            var formValid: Boolean = nombreError == null &&
                    precioError == null &&
                    nombre.trim().isNotEmpty() &&
                    precio.trim().isNotEmpty()

            // Botón de guardar producto
            Button(
                onClick = {
                    Toast.makeText(context, "Producto guardado (solo UI)", Toast.LENGTH_LONG).show()
                    // TODO: Guardar en base de datos
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = formValid // Habilitado solo si el formulario es válido
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Guardar",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Producto")
            }
        }
    }
}

/**
 * Guarda un [Bitmap] en la galería y devuelve el [Uri] resultante.
 */
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateProductScreenPreview() {
    CreateProductScreen()
}