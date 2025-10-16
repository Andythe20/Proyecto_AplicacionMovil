package com.example.appshop.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appshop.R
import com.example.appshop.db.AppDatabase
import com.example.appshop.db.repository.UserRepository
import com.example.appshop.viewmodel.AuthViewModel
import com.example.appshop.viewmodel.AuthViewModelFactory
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.lint.kotlin.metadata.Visibility

/**
 * Composable que representa la pantalla de registro de nuevos usuarios.
 *
 * Esta pantalla es una "UI tonta" (Dumb UI) en el sentido de que su única responsabilidad
 * es mostrar la interfaz y delegar todas las acciones del usuario (como clics de botón)
 * al ViewModel. No contiene lógica de negocio.
 *
 * @param modifier Modificador para personalizar el layout.
 * @param onSignupSuccess Un callback que se ejecuta cuando el registro es exitoso,
 *                        permitiendo la navegación a otra pantalla (ej. Login).
 */
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    // --- INICIALIZACIÓN DE LA ARQUITECTURA MVVM ---

    // 1. Obtenemos el Context, necesario para crear la base de datos.
    val context = LocalContext.current

    // 2. Creamos las instancias de la base de datos y el repositorio.
    //    Usamos 'remember' para que estas instancias no se recreen en cada recomposición,
    //    lo cual sería muy ineficiente.
    val db = remember { AppDatabase.getDatabase(context) }
    val repo = remember { UserRepository(db.userDao()) }

    // 3. Obtenemos el ViewModel usando la Factory.
    //    Le pasamos la fábrica que sabe cómo construir nuestro AuthViewModel (dándole el repositorio).
    //    Jetpack Compose se encarga de que este ViewModel sobreviva a cambios de configuración
    //    (como rotar la pantalla).
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repo))

    // --- ESTADOS DE LA UI ---

    // `remember` y `mutableStateOf` crean estados que, al cambiar, provocan que la UI se "recomponga" (redibuje).
    var email by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var contrasenna by remember { mutableStateOf("") }
    var contrasennaValidar by remember { mutableStateOf("") }

// Si el valor es 'null' -> no hay error.
// Si tiene un texto -> hay un error y ese es el mensaje a mostrar.
    var nombreError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var contrasennaError by remember { mutableStateOf<String?>(null) }
    var contrasennaValidarError by remember { mutableStateOf<String?>(null) }

    var contrasennaVisible by remember { mutableStateOf(false) }
    var contrasennaValidarVisible by remember { mutableStateOf(false) }

    // --- DISEÑO DE LA PANTALLA (LAYOUT) ---
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Registro de usuario",
            style =
                TextStyle(
                    fontSize = 35.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.loginimg),
            contentDescription = "Login Banner",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campos de texto para la entrada de datos del usuario.
        OutlinedTextField(
            value = email,
            // Cuando el usuario escribe, limpiamos el error para que desaparezca al corregirlo.
            onValueChange = {
                email = it
                emailError = null    },
            label = { Text(text = "Dirección correo electrónico") },
            modifier = Modifier.fillMaxWidth(),

            isError = emailError != null,
            // Muestra el mensaje de error debajo si existe.
            supportingText = {
                if (emailError != null) {
                    Text(text = emailError!!)
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,onValueChange = {
                nombre = it
                nombreError = null
            },
            label = { Text(text = "Nombre usuario") },
            modifier = Modifier.fillMaxWidth(),
            isError = nombreError != null,
            supportingText = {
                if (nombreError != null) {
                    Text(text = nombreError!!)
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = contrasenna,
            onValueChange = {
                contrasenna = it
                contrasennaError = null
            },
            label = { Text(text = "Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = contrasennaError != null,
            supportingText = {
                if (contrasennaError != null) {
                    Text(text = contrasennaError!!)
                }
            },
            // --- Lógica de visibilidad ---
            visualTransformation = if (contrasennaVisible) {
                // Si contrasennaVisible es true, no aplicamos ninguna transformación (texto visible).
                VisualTransformation.None
            } else {
                // Si es false, aplicamos la transformación de contraseña (oculta el texto).
                PasswordVisualTransformation()
            },
            // --- Icono para cambiar la visibilidad ---
            trailingIcon = {
                val image = if (contrasennaVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Descripción para accesibilidad
                val description = if (contrasennaVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { contrasennaVisible = !contrasennaVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )


        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = contrasennaValidar,
            onValueChange = {
                contrasennaValidar = it
                contrasennaValidarError = null},
            label = { Text(text = "Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = contrasennaValidarError != null,
            supportingText = {
                if (contrasennaValidarError != null) {
                    Text(text = contrasennaValidarError!!)
                }
            },
            // --- Lógica de visibilidad ---
            visualTransformation = if (contrasennaValidarVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            // --- Icono para cambiar la visibilidad ---
            trailingIcon = {
                val image = if (contrasennaValidarVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (contrasennaValidarVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { contrasennaValidarVisible = !contrasennaValidarVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // Limpiamos todos los errores anteriores antes de empezar una nueva validación
                nombreError = null
                emailError = null
                contrasennaError = null
                contrasennaValidarError = null

                // Validación del Nombre
                if (nombre.isBlank()) {
                    nombreError = "El nombre no puede estar vacío"
                }

                // Validación del Email
                if (email.isBlank()) {
                    emailError = "El correo no puede estar vacío"
                } else if (!formatoCorreo(email)) {
                    emailError = "El formato del correo no es válido"
                }

                // Validación de la Contraseña
                if (contrasenna.isBlank()) {
                    contrasennaError = "La contraseña no puede estar vacía"
                } else if (!largoContrasena(contrasenna)) {
                    contrasennaError = "La contraseña debe tener al menos 6 caracteres"
                }

                // Validación de la Confirmación de Contraseña
                if (contrasennaValidar.isBlank()) {
                    contrasennaValidarError = "Debes confirmar la contraseña"
                } else if (!validarContrasena(contrasenna, contrasennaValidar)) {
                    contrasennaValidarError = "Las contraseñas no coinciden"
                }

                // Si después de todas las validaciones, NINGUNA variable de error
                // tiene un mensaje, significa que todo es válido.
                val esValido = nombreError == null &&
                        emailError == null &&
                        contrasennaError == null &&
                        contrasennaValidarError == null

                if (esValido) {
//                     Si todo esta correcto, se registra el usuario.
                    viewModel.registerUser(nombre, email, contrasenna) { isSuccess, message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        if (isSuccess) {
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    }
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(60.dp),
        ) {
            Text(text = "Registrarse", fontSize = 22.sp)
        }

    }
}
