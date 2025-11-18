package com.example.appshop.model

import android.net.Uri

data class Product(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val categoria: String = "",
    val codigo: String = "",
    val fotoUri: Uri? = null
)