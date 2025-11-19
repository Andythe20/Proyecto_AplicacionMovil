package com.example.appshop.db.repository

import android.util.Log
import com.example.appshop.model.Product
import com.example.appshop.network.IApiService

class ProductRepository(private val apiService: IApiService) { // Ahora usa la clase correcta
    suspend fun getProducts(): List<Product>? {
        return try {
            // Llama a la API. `response`
            val response = apiService.getProducts()

            Log.d("ProductRepository", "Response code: ${response.code()}")
            Log.d("ProductRepository", "Response message: ${response.message()}")
            Log.d("ProductRepository", "Response isSuccessful: ${response.isSuccessful}")

            // Comprueba si la llamada fue exitosa
            if (response.isSuccessful) {
                //Si fue exitosa, extrae la lista de productos
                response.body()
            } else {
                // El servidor respondió con un error
                Log.e("ProductRepository", "ERROR HTTP: ${response.code()} - ${response.message()}")
                Log.e("ProductRepository", "Error body: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            // Hubo un error de red o conexión
            Log.e("ProductRepository", "ERROR DE CONEXIÓN: ${e.message}", e)
            null
        }
    }
}