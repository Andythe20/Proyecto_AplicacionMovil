package com.example.appshop.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.appshop.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    // Lista temporal de productos (en memoria)
    private val _productos = MutableStateFlow<List<Product>>(emptyList())
    val productos: StateFlow<List<Product>> = _productos

    // Función para "guardar" producto (por ahora sólo en memoria)
    fun saveProduct(nombre: String, descripcion: String, precio: Double, codigo: String, fotoUri: Uri?) {
        val nuevo = Product(
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            codigo = codigo,
            fotoUri = fotoUri
        )

        _productos.value = _productos.value + nuevo

        // TODO: Aquí agregar lógica para guardar en base de datos (Room o Firebase)
        // Ejemplo (más adelante):
        // viewModelScope.launch {
        //     productoRepository.insertarProducto(nuevo)
        // }
    }
}
