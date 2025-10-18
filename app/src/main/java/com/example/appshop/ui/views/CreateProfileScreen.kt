@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.appshop.ui.views

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.appshop.utils.validateInputText
import com.example.appshop.viewmodel.AuthViewModel


// === Scaffold con TopAppBar incluido ===
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateProfileView(viewModel: AuthViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") }
            )
        }
    ) { innerPadding -> // padding del Scaffold
        CreateProfileScreen(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel
) {
    // Creación del contexto
    val context = LocalContext.current
    val user = viewModel.loggedInUser

    // === Estados de los campos del formulario ===
    var username by remember { mutableStateOf(user?.name ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }
    val today = java.time.LocalDate.now()
    var birthdate by remember { mutableStateOf(user?.birthdate ?: today) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = birthdate.atStartOfDay(java.time.ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    )
    //var fotoUri by remember { mutableStateOf<Uri?>(user?.profileImageUri?.let { Uri.parse(it) }) }
    var fotoUri = loadProfileImage(user?.profileImageUri)

    var usernameError by remember { mutableStateOf<String?>(null) }

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
    ) { uri ->
        uri?.let { selectedUri ->
            fotoUri.value = selectedUri
            Toast.makeText(context, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val savedUri = saveBitmapToGallery(context, bitmap)
            if (savedUri != null) {
                fotoUri.value = savedUri
                Toast.makeText(context, "Foto guardada en galería", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Permisos runtime según versión
    val readStoragePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galeriaLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Se necesita permiso para acceder a la galería", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp) //
            .padding(top = 16.dp, bottom = 32.dp),
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
            if (fotoUri.value != null) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUri.value),
                    contentDescription = "Foto del perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Sin foto",
                    modifier = Modifier.size(92.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
                Icon(
                    Icons.Filled.CameraAlt,
                    contentDescription = "Cámara",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tomar Foto")
            }
            Button(onClick = {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.READ_MEDIA_IMAGES
                else Manifest.permission.READ_EXTERNAL_STORAGE

                val granted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                if (granted) {
                    galeriaLauncher.launch("image/*")
                } else {
                    readStoragePermissionLauncher.launch(permission)
                }
            }) {
                Icon(Icons.Filled.PhotoLibrary, contentDescription = "Galería")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Galería")
            }
        }

        Spacer(modifier = Modifier.height(24.dp)) //

        // === Campo Nombre ===
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = validateInputText("Nombre", it, 3)
            },
            label = { Text("Nombre") },
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

        // === Campo Fecha Nacimiento ===
        OutlinedTextField(
            value = birthdate.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de nacimiento") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Filled.CalendarToday, contentDescription = "Seleccionar fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            birthdate = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneOffset.UTC)
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }) {
                        Text("Seleccionar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        // === Guardar perfil ===
        Button(
            onClick = {
                if (usernameError == null && username.isNotBlank()) {
                    viewModel.updateUserProfile(
                        name = username,
                        imageProfileUri = fotoUri.value?.toString(),
                        birthdate = birthdate.toString()
                    ) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Revisa los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Save, contentDescription = "Guardar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar cambios")
        }
    }
}

// === Función auxiliar para guardar la imagen ===
fun saveBitmapToGallery(context: android.content.Context, bitmap: Bitmap): Uri? {
    val filename = "perfil_${System.currentTimeMillis()}.jpg"
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

@Composable
fun loadProfileImage(userUri: String?): MutableState<Uri?>{
    val fotoUri = remember { mutableStateOf<Uri?>(null) }
    LaunchedEffect(Unit) {
        userUri?.let { uriString ->
            fotoUri.value = Uri.parse(uriString)
        }
    }
    return fotoUri
}

// === Preview ===
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateProductScreenPreview() {
    CreateProfileView() // Previsualiza con Scaffold incluido
}
