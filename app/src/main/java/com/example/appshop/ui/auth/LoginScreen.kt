import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            UserRepository(
                AppDatabase.getDatabase(LocalContext.current).userDao()
            )
        )
//        En resumen se está creando una cadena de dependencias para que la
//    pantalla reciba un AuthViewModel completamente funcional y listo para hablar con la base de datos.

    )
) {
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }

    // --- ESTADOS PARA LOS ERRORES ---
    var emailError by remember { mutableStateOf<String?>(null) }
    var contrasenaError by remember { mutableStateOf<String?>(null) }
    // Este error es para mensajes del ViewModel (ej: "Contraseña incorrecta")
    var errorGeneral by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Inicio de sesión",
            style =
                TextStyle(
                    fontSize = 35.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.loginimg),
            contentDescription = "Login Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null // Limpia el error del campo al escribir
                errorGeneral = null // Limpia el error general
            },
            label = { Text(text = "Correo Electrónico.") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,

            isError = emailError != null || errorGeneral != null,
            supportingText = {
                if (emailError != null) {
                    Text(text = emailError!!)
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                contrasenaError = null // Limpia el error del campo al escribir
                errorGeneral = null // Limpia el error general
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = contrasenaError != null || errorGeneral != null,
            supportingText = {
                if (contrasenaError != null) {
                    Text(text = contrasenaError!!)
                }
                // Mostramos el error general (del ViewModel) aquí
                if (errorGeneral != null) {
                    Text(text = errorGeneral!!, color = MaterialTheme.colorScheme.error)
                }
            },
            visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (contrasenaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (contrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // Limpiamos errores antiguos
                emailError = null
                contrasenaError = null
                errorGeneral = null

                // Validaciones de la UI (campos vacíos)
                var esValidoEnUI = true

                if (email.isBlank()) {
                    emailError = "El correo no puede estar vacío"
                    esValidoEnUI = false
                }
                if (contrasena.isBlank()) {
                    contrasenaError = "La contraseña no puede estar vacía"
                    esValidoEnUI = false
                }

                // Si los campos no están vacíos, llamamos al ViewModel
                if (esValidoEnUI) {
                    viewModel.loginUser(email, contrasena) { success, message ->
                        if (success) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            // Navega a tu pantalla principal (asegúrate de que "home" es la ruta correcta)
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true } // Limpia la pila para no volver atrás
                            }
                        } else {
                            // El ViewModel devolvió un error (ej: credenciales incorrectas)
                            // Lo mostramos como un error general
                            errorGeneral = message
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Text(text = "Iniciar Sesión", fontSize = 18.sp)
        }
    }
}
