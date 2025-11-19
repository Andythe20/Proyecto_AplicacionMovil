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

//    // Lista temporal de productos (en memoria)
//    private val _productos = MutableStateFlow<List<Product>>(emptyList())
//    val productos: StateFlow<List<Product>> = _productos
//
//    // Función para "guardar" producto (por ahora sólo en memoria)
//    fun saveProduct(nombre: String, descripcion: String, precio: Double, codigo: String, fotoUri: Uri?) {
//        val nuevo = Product(
//            nombre = nombre,
//            descripcion = descripcion,
//            precio = precio,
//            codigo = codigo,
//            fotoUri = fotoUri
//        )
//
//        _productos.value = _productos.value + nuevo
//
//        // TODO: Aquí agregar lógica para guardar en base de datos (Room o Firebase)
//        // Ejemplo (más adelante):
//        // viewModelScope.launch {
//        //     productoRepository.insertarProducto(nuevo)
//        // }
//    }


    // CREA UNA INSTANCIA DEL REPOSITORIO
    // El repositorio necesita el servicio de la API, que lo obtenemos de RetrofitInstance.
    private val repository: ProductRepository

    // ESTADO PARA LA LISTA DE PRODUCTOS
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // ESTADO PARA SABER SI ESTÁ CARGANDO
    // Útil para mostrar un círculo de progreso en la UI.
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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

}
