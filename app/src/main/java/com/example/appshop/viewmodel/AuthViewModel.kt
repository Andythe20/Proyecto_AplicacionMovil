package com.example.appshop.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appshop.db.entities.User
import com.example.appshop.db.repository.UserRepository // <-- 1. IMPORTAMOS EL REPOSITORIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel para la lógica de autenticación (Registro e Inicio de Sesión).
 *
 * El ViewModel es parte del patrón MVVM y su responsabilidad es:
 * 1.  Gestionar y almacenar los datos relacionados con la UI de una manera consciente del ciclo de vida.
 * 2.  Comunicarse con la capa de datos (el Repositorio) para obtener o guardar información.
 * 3.  Exponer los datos y las operaciones a la UI (las pantallas Composable).
 *
 * @param repository El Repositorio de usuarios. Se inyecta a través del constructor (Inyección de Dependencias),
 *                   lo que hace que el ViewModel sea más fácil de probar y más desacoplado.
 */
class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // --- ESTADO PARA GUARDAR AL USUARIO LOGUEADO ---
    // Usamos 'by mutableStateOf' para que la UI de Compose reaccione a los cambios.
    // Es de tipo User? (puede ser nulo) porque al principio nadie ha iniciado sesión.
    var loggedInUser by mutableStateOf<User?>(null)
        private set // 'private set' significa que solo el ViewModel puede cambiar su valor,
    // pero cualquiera puede leerlo.


    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param name El nombre del usuario.
     * @param email El correo del usuario.
     * @param password La contraseña del usuario (en texto plano, ¡inseguro!).
     * @param onResult Un callback (función lambda) que se ejecuta cuando la operación termina.
     *                 Devuelve un `Boolean` (true si fue exitoso) y un `String` con un mensaje para el usuario.
     */
    fun registerUser(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit,
    ) {
        // `viewModelScope.launch` inicia una corrutina en el hilo principal por defecto.
        // La corrutina se cancelará automáticamente si el ViewModel se destruye.
        viewModelScope.launch(Dispatchers.IO) { // Usamos Dispatchers.IO para operaciones de base de datos
            val existingUser = repository.getUserByEmail(email)

            if (existingUser != null) {
                // Si el usuario ya existe, cambiamos al hilo principal para notificar a la UI.
                withContext(Dispatchers.Main) {
                    onResult(false, "El correo ya está registrado.")
                }
            } else {
                // ADVERTENCIA: Aquí es donde deberías "hashear" la contraseña antes de guardarla.
                val user = User(name = name, email = email, password = password)
                repository.insertUser(user)

                // Cambiamos al hilo principal para notificar a la UI que todo fue bien.
                withContext(Dispatchers.Main) {
                    onResult(true, "Usuario registrado exitosamente.")
                }
            }
        }
    }

    /**
     * Valida las credenciales de un usuario para iniciar sesión.
     */
    fun loginUser(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.login(email, password)

            withContext(Dispatchers.Main) {
                if (user != null) {
                    // Guardamos el usuaruio en el estado 'loggedInUser'.
                    loggedInUser = user
                    onResult(true, "Inicio de sesión exitoso.")
                } else {
                    onResult(false, "Correo o contraseña incorrectos.")
                }
            }
        }
    }
}
