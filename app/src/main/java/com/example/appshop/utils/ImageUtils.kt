package com.example.appshop.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.*

fun saveBitmapToGallery(context: Context, bitmap: Bitmap): Uri?{
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
fun rememberProfileImage(userUri: String?): MutableState<Uri?> {
    val fotoUri = remember { mutableStateOf<Uri?>(null)}
    LaunchedEffect(userUri){
        userUri?.let { fotoUri.value = Uri.parse(it)}
    }
    return fotoUri
}