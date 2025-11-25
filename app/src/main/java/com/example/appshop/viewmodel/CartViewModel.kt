package com.example.appshop.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appshop.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    //Mapa privado y mutable. La clave es el Producto, el valor es la Cantidad.
    private val _productosEnCarrito = MutableStateFlow<Map<Product, Int>>(emptyMap())

    //Versión pública y de solo lectura para que la UI observe los cambios.
    val productosEnCarrito: StateFlow<Map<Product, Int>> = _productosEnCarrito.asStateFlow()

    //privado para el total.
    private val _total = MutableStateFlow(0)
    //público para que la UI observe el total.
    val total: StateFlow<Int> = _total.asStateFlow()


    // Agregar producto al carrito
    fun agregarProducto(product: Product) {
        _productosEnCarrito.update { currentCart ->
            val cartMutable = currentCart.toMutableMap()
            // Si el producto ya existe, aumenta su cantidad; si no, lo añade con cantidad 1.
            cartMutable[product] = (cartMutable[product] ?: 0) + 1
            cartMutable
        }
        recalcularTotal()
    }

    // Cambiar la cantidad de un producto
    fun cambiarCantidad(product: Product, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarProducto(product)
        } else {
            _productosEnCarrito.update { currentCart ->
                val cartMutable = currentCart.toMutableMap()
                cartMutable[product] = nuevaCantidad
                cartMutable
            }
        }
        recalcularTotal()
    }


    // Eliminar completamente un producto del carrito
    fun eliminarProducto(product: Product) {
        _productosEnCarrito.update { currentCart ->
            val cartMutable = currentCart.toMutableMap()
            cartMutable.remove(product)
            cartMutable
        }
        recalcularTotal()
    }

    fun vaciarCarrito() {
        _productosEnCarrito.value = mutableMapOf()
        recalcularTotal()
    }

    // Recalcula el total y actualiza el StateFlow
    private fun recalcularTotal() {
        val nuevoTotal = _productosEnCarrito.value.entries.sumOf { (product, quantity) ->
            product.precio * quantity
        }
        _total.value = nuevoTotal
    }
}
