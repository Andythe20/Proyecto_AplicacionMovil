package com.example.appshop.db.repository

import android.util.Log
import com.example.appshop.model.Product
import com.example.appshop.network.IApiService

class ProductRepository(private val apiService: IApiService) { // Ahora usa la clase correcta
    suspend fun getProducts(): List<Product>? {
        return try {
            // Llama a la API. `response`
            val response = apiService.getProducts()

            // Comprueba si la llamada fue exitosa
            if (response.isSuccessful) {
                //Si fue exitosa, extrae la lista de productos
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            // Hubo un error de red o conexión
            null
        }
    }
}