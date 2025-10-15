package com.example.appshop.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appshop.db.AppDatabase
import com.example.appshop.db.repository.UserRepository
import com.example.appshop.viewmodel.AuthViewModel
import com.example.appshop.viewmodel.AuthViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    // Obtenemos el context para crear la base de datos
    val context = LocalContext.current

    // instancias de la DB y el repositorio
    val db = remember { AppDatabase.getDatabase(context) }
    val repo = remember { UserRepository(db.userDao()) }

    //obtener el ViewModel
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repo))

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Home")},
                modifier = Modifier.padding(20.dp))}
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center
        ) {

        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Para satisfacer el parámetro `navController` de HomeScreen, creamos un
    // NavController de prueba usando `rememberNavController()`. No realizará
    // ninguna navegación real en la preview, pero permite que el Composable se renderice.
    val navController = rememberNavController()

    // Llamamos a nuestro Composable `HomeScreen` pasándole el navController de prueba.
    HomeScreen(navController = navController)
}