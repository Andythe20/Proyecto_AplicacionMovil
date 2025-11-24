package com.example.appshop.db.repository

import com.example.appshop.model.Product
import com.example.appshop.network.IApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import retrofit2.Response


@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryJUnitTest {

    // Mockeamos IApiService
    val apiService = mockk<IApiService>();

    @Test
    fun `getProducts should return list of products when API is successful`() = runTest {

        val fakeProducts = listOf(
            Product(
                codigo = "TC001",
                categoria = "Tortas Cuadradas",
                nombre = "Torta Cuadrada de Chocolate",
                descripcion = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales",
                precio = 45000,
                url = "https://brigams.pe/wp-content/uploads/chocolate-2.jpg"
            ),
            Product(
                codigo = "TC002",
                categoria = "Tortas Cuadradas",
                nombre = "Torta Cuadrada de Frutas",
                descripcion = "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.",
                precio = 50000,
                url = "https://brigams.pe/wp-content/uploads/tutifruti-2-1000x667.jpg"
            )
        )

        // Mockeamos la respuesta de Retrofit usamos coEvery para funciones suspend
        coEvery { apiService.getProducts() } returns Response.success(fakeProducts)

        val repository = ProductRepository(apiService)
        val result = repository.getProducts()

        assertEquals(fakeProducts, result)
    }

    @Test
    fun `getProducts should return null when API responds with error`() = runTest {

        // Mockeamos una respuesta de error HTTP
        coEvery { apiService.getProducts() } returns Response.error(
            500,
            "Internal Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val repository = ProductRepository(apiService)
        val result = repository.getProducts()

        assertEquals(null, result)
    }

    @Test
    fun `getProducts should return null when API throws exception`() = runTest {

        coEvery { apiService.getProducts() } throws RuntimeException("Network error")

        val repository = ProductRepository(apiService)
        val result = repository.getProducts()

        assertEquals(null, result)
    }
}