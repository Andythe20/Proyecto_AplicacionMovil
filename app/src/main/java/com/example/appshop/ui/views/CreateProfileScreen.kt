@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.appshop.ui.views

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appshop.ui.components.DatePickerField
import com.example.appshop.ui.components.ImagePickerSection
import com.example.appshop.utils.rememberProfileImage
import com.example.appshop.utils.validateInputText
import com.example.appshop.viewmodel.AuthViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateProfileView(viewModel: AuthViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") }
            )
        }
    ) { innerPadding ->
        CreateProfileScreen(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    val user = viewModel.loggedInUser

    // === Estados del formulario ===
    var username by remember { mutableStateOf(user?.name ?: "") }
    var usernameError by remember { mutableStateOf<String?>(null) }

    var birthdate by remember {
        mutableStateOf(user?.birthdate ?: LocalDate.now())
    }

    // Imagen del perfil
    val fotoUri = rememberProfileImage(user?.profileImageUri)

    // === UI ===
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Imagen del perfil ---
        ImagePickerSection(fotoUri)

        // --- Campo Nombre ---
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = validateInputText("Nombre", it)
            },
            label = { Text("Nombre") },
            isError = usernameError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        usernameError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        // --- Campo Fecha de Nacimiento (modularizado) ---
        DatePickerField(
            label = "Fecha de nacimiento",
            selectedDate = birthdate,
            onDateSelected = { birthdate = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Botón Guardar ---
        Button(
            onClick = {
                if (usernameError == null && username.isNotBlank()) {
                    viewModel.updateUserProfile(
                        name = username,
                        imageProfileUri = fotoUri.value?.toString(),
                        birthdate = birthdate.toString()
                    ) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Revisa los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Save, contentDescription = "Guardar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar cambios")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateProfilePreview() {
    CreateProfileView()
}
