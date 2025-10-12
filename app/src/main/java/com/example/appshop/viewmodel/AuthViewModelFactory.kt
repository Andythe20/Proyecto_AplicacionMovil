package com.example.appshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appshop.db.repository.UserRepository

/**
 * ViewModelProvider.Factory para el AuthViewModel.
 *
 * Una "Factory" (Fábrica) es una clase que sabe cómo crear instancias de otras clases.
 * En el contexto de Android, necesitamos una ViewModelFactory personalizada cuando nuestro
 * ViewModel tiene un constructor con parámetros (diferente al constructor vacío por defecto).
 *
 * Nuestro `AuthViewModel` requiere una instancia de `UserRepository` para funcionar,
 * y esta fábrica se encarga de proporcionársela.
 *
 * @param repository La instancia del repositorio que se pasará al `AuthViewModel` cuando se cree.
 */
class AuthViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {

    /**
     * Este método es llamado por el sistema de `ViewModel` cuando necesita crear
     * una instancia de un ViewModel.
     *
     * @param modelClass La clase del ViewModel que se solicita crear (ej. AuthViewModel::class.java).
     * @return Una instancia del ViewModel solicitado, correctamente inicializado.
     * @throws IllegalArgumentException si la fábrica no sabe cómo crear la clase de ViewModel solicitada.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // `isAssignableFrom` comprueba si `modelClass` es AuthViewModel o una subclase de él.
        // Es la forma segura de verificar que podemos crear lo que nos piden.
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {

            // Si la clase solicitada es AuthViewModel, creamos una instancia
            // pasándole el `repository` que recibimos en el constructor de esta fábrica.

            @Suppress("UNCHECKED_CAST") // Suprimimos una advertencia del compilador porque
            // ya hemos verificado el tipo de clase con `isAssignableFrom`.
            return AuthViewModel(repository) as T // CREA EL VIEWMODEL PASÁNDOLE EL REPOSITORIO.
        }

        // Si nos piden crear un ViewModel que no es `AuthViewModel`, lanzamos una excepción
        // para indicar que esta fábrica no está diseñada para manejar ese tipo.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
