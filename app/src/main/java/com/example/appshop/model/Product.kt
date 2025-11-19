package com.example.appshop.model

data class Product(
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val url: String
)