package com.example.appshop.ui.views

import PromotionsSection
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appshop.db.AppDatabase
import com.example.appshop.db.repository.UserRepository
import com.example.appshop.ui.components.CarouselSection
import com.example.appshop.ui.components.HeaderSection
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

    //comportamiento del scroll al topBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text( text = "Repostería OnlyFlans",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 30.sp
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("auth") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    )
    { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp, top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // HEADER
                HeaderSection()
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                CarouselSection()
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                PromotionsSection()
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {navController.navigate("createProduct") }
                ) {
                    Text("Crear Producto", fontSize = 22.sp)
                }
            }
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