package com.example.appshop.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appshop.R
import com.example.appshop.db.AppDatabase
import com.example.appshop.db.repository.UserRepository
import com.example.appshop.viewmodel.AuthViewModel
import com.example.appshop.viewmodel.AuthViewModelFactory

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

    // --- DISEÑO DE LA PANTALLA (LAYOUT) ---

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(32.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Bienvenido!",
            style =
                TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Registro nuevo usuario",
            style =
                TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                ),
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
            onValueChange = { email = it },
            label = { Text(text = "Email address") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = contrasenna,
            onValueChange = { contrasenna = it },
            label = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // Oculta la contraseña
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = contrasennaValidar,
            onValueChange = { contrasennaValidar = it },
            label = { Text(text = "Repeat password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Botón que desencadena la lógica de registro.
        Button(
            onClick = {
                // Validación básica de los datos en la UI antes de llamar al ViewModel.
                if (contrasenna != contrasennaValidar) {
                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                        .show()
                    return@Button // Detiene la ejecución.
                }

                if (email.isBlank() || nombre.isBlank() || contrasenna.isBlank()) {
                    Toast.makeText(
                        context,
                        "Por favor, rellena todos los campos",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@Button
                }

                // Llama a la función del ViewModel, delegando la lógica de negocio.
                viewModel.registerUser(nombre, email, contrasenna) { isSuccess, message ->
                    // El ViewModel nos devuelve el resultado a través de este callback.
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                    if (isSuccess) {
                        // Si el registro fue exitoso, usamos el NavController para navegar.
                        navController.navigate("login") {
                            // Limpiamos el historial para que el usuario no pueda volver a esta pantalla de registro.
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(60.dp),
        ) {
            Text(text = "Sign Up", fontSize = 22.sp)
        }
    }
}
