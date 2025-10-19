package com.example.appshop.ui.views

import PromotionsSection
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appshop.ui.components.AppDropdownMenu
import com.example.appshop.ui.components.CarouselSection
import com.example.appshop.ui.components.FooterSection
import com.example.appshop.ui.components.HeaderSection
import com.example.appshop.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AuthViewModel
){
    // --- OBTENER EL CONTEXTO Y CREAR LA FUNCIÓN AUXILIAR ---
    val context = LocalContext.current // Obtiene el contexto para usar componentes del sistema Android
    val openUrl = { url: String -> // Funcion lambda para abrir URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    //comportamiento del scroll al topBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Creamos una variable de estado para saber si el menú está abierto o cerrado.
    var menuAbierto by remember { mutableStateOf(false) }

    // --- OBTENEMOS EL USUARIO Y SU NOMBRE ---
    val currentUser = viewModel.loggedInUser
    // Si algo falla, muestra "Usuario".
    val username = currentUser?.name ?: "Usuario"

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
                actions = {
                    // Este IconButton ahora solo abre el menú.
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Abrir menú de opciones"
                        )
                    }

                    // Aquí llamamos a nuestro componente de menú personalizado.
                    AppDropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false },
                        navController = navController
                    )
                },
                modifier = Modifier.height(100.dp)
            )
        }
    )
    { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // HEADER
                    HeaderSection(username)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    CarouselSection()
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Promociones disponibles!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(25.dp))
                    PromotionsSection()
                }

                item {
                    FooterSection(openUrl)
                }


            }
        }
}

