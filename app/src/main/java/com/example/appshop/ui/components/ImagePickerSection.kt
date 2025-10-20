package com.example.appshop.ui.components

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.appshop.utils.saveBitmapToGallery
import com.example.appshop.utils.getGalleryPermission
import com.example.appshop.utils.isPermissionGranted
import com.example.appshop.utils.handlePermissionDenied

@Composable
fun ImagePickerSection(
    fotoUri: MutableState<Uri?>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted)
            Toast.makeText(context, "Permiso de cámara requerido", Toast.LENGTH_SHORT).show()
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            fotoUri.value = it
            //Toast.makeText(context, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val savedUri = saveBitmapToGallery(context, it)
            fotoUri.value = savedUri
            Toast.makeText(context, "Foto guardada", Toast.LENGTH_SHORT).show()
        }
    }

    val readStoragePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galeriaLauncher.launch("image/*")
        } else {
            handlePermissionDenied(context, "Se necesita permiso para acceder a la galería")
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Vista previa circular
        Box(
            modifier = modifier
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
                    Icons.Filled.AccountCircle,
                    contentDescription = "Sin foto",
                    modifier = Modifier.size(92.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (isPermissionGranted(context, Manifest.permission.CAMERA)) {
                    camaraLauncher.launch(null)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Icon(Icons.Filled.CameraAlt, null)
                Spacer(Modifier.width(8.dp))
                Text("Cámara")
            }

            Button(onClick = {
                val permission = getGalleryPermission()

                if (isPermissionGranted(context, permission)) {
                    galeriaLauncher.launch("image/*")
                } else {
                    readStoragePermissionLauncher.launch(permission)
                }
            }) {
                Icon(Icons.Filled.PhotoLibrary, null)
                Spacer(Modifier.width(8.dp))
                Text("Galería")
            }
        }
    }
}