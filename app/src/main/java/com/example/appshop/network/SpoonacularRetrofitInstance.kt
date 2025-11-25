package com.example.appshop.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Objeto singleton para crear y configurar una instancia de Retrofit para la API de Spoonacular.
object SpoonacularRetrofitInstance {

    // URL base de la API de Spoonacular.
    private const val BASE_URL = "https://api.spoonacular.com/"

    // Clave de API para acceder a la API de Spoonacular.
    private const val API_KEY = "8ea918db1ac2451ca4840de0baff7186"

    /**
     * Interceptor de logging para OkHttp.
     *
     * Este interceptor registra en la consola los detalles de las peticiones y respuestas HTTP.
     * El nivel `BODY` indica que se registrará el cuerpo completo de la petición y la respuesta.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Interceptor de OkHttp para añadir la clave de API a todas las peticiones.
     *
     * Este interceptor se ejecuta para cada petición que se realiza a través del cliente OkHttp.
     * Su función es añadir el parámetro `apiKey` a la URL de la petición de forma automática.
     * Esto evita tener que añadir la clave de API manualmente en cada una de las llamadas a la API.
     */
    private val apiKeyInterceptor = { chain: Interceptor.Chain ->
        val original = chain.request()
        val originalUrl = original.url

        // Construye una nueva URL añadiendo el parámetro "apiKey" con su valor.
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("apiKey", API_KEY)
            .build()

        // Crea una nueva petición con la nueva URL.
        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()

        // Procede con la nueva petición.
        chain.proceed(newRequest)
    }

    /**
     * Cliente OkHttp personalizado.
     *
     * Este cliente se configura con los interceptores de logging y de la clave de API.
     * Todos los interceptores añadidos aquí se aplicarán a todas las peticiones realizadas por Retrofit.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(apiKeyInterceptor)
        .build()

    /**
     * Instancia de la interfaz de servicio de Retrofit (`SpoonacularService`).
     *
     * Se utiliza la delegación `lazy` para que la instancia de Retrofit (y por lo tanto `SpoonacularService`)
     * solo se cree la primera vez que se accede a ella. Esto es eficiente y seguro para hilos.
     *
     * La configuración de Retrofit incluye:
     * - `baseUrl`: La URL base de la API.
     * - `client`: El cliente OkHttp personalizado con los interceptores.
     * - `addConverterFactory`: El conversor de JSON (Gson) para convertir las respuestas JSON en objetos Kotlin.
     */
    val api: SpoonacularService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpoonacularService::class.java)
    }

}
