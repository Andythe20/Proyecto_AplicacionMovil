package com.example.appshop.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appshop.db.repository.ProductRepository
import com.example.appshop.model.Product
import com.example.appshop.network.IApiService
import com.example.appshop.network.RetroFitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    // CREA UNA INSTANCIA DEL REPOSITORIO
    // El repositorio necesita el servicio de la API, que lo obtenemos de RetrofitInstance.
    private val repository: ProductRepository

    // ESTADOS PARA LOS PRODUCTOS Y EL ESTADO DE CARGA
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        Log.d("ProductViewModel", "INIT DEL VIEWMODEL EJECUTADO")

        // Inicializamos el repositorio aquí
        val apiService: IApiService = RetroFitInstance.api
        repository = ProductRepository(apiService)

        // Llamamos a la función para que cargue los productos desde la API
        loadProducts()
    }

    // FUNCIÓN QUE HACE LA LLAMADA A LA API A TRAVÉS DEL REPOSITORIO
    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true // Indica que la carga ha comenzado
            try {
                // Pide los productos al repositorio
                val productList = repository.getProducts()

                Log.d("ProductViewModel", "Productos desde API: $productList")
                Log.d("ProductViewModel", "Cantidad: ${productList?.size}")

                //Log.d("ProductViewModel", "Respuesta recibida: ${productList != null}")
                //Log.d("ProductViewModel", "Cantidad de productos: ${productList?.size ?: 0}")
                if (productList != null) {
                    // Actualiza la lista con los datos de la API
                    _products.value = productList
                } else {
                    // El repositorio devolvió null, lo que significa que hubo un error
                    // Log para logcat
                    Log.e("ProductViewModel", "La respuesta de la API fue nula o fallida.")
                    _products.value = emptyList() // Asegurarse de que la lista quede vacía
                }
            } catch (e: Exception) {
                // Captura cualquier otro error inesperado durante la llamada
                Log.e("ProductViewModel", "Error al cargar productos: ${e.message}")
            } finally {
                _isLoading.value = false // Indica que la carga ha terminado (sea con éxito o error)
            }
        }
    }

    // FUNCIÓN PARA PARA PULL-TO-REFRESH
    fun refreshProducts() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val productList = repository.getProducts()
                Log.d("ProductViewModel", "Refresh - Productos desde API: ${productList?.size}")
                if (productList != null) {
                    _products.value = productList
                } else {
                    Log.e("ProductViewModel", "Refresh - La respuesta de la API fue nula")
                    _products.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error al refrescar productos: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
