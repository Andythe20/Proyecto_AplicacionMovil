package com.example.appshop.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    navController: NavHostController,
    title: String = "OnlyFlans",
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = { scope.launch { drawerState.close() } }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar menú")
                    }
                }
                Text(
                    "OnlyFlans",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate("home") }
                )
                NavigationDrawerItem(
                    label = { Text("Carrito") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate("cart") }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate("createProfile") }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    icon = { Icon(Icons.Default.Close, contentDescription = null) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; showLogoutDialog = true }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            // --- Aquí se dibuja el contenido de la pantalla (HomeScreen) ---
            content(innerPadding)

            // --- Diálogo de confirmación (se muestra sobre el contenido) ---
            if (showLogoutDialog) {
                LogoutConfirmationDialog(
                    onDismiss = { showLogoutDialog = false },
                    onConfirmLogout = {
                        showLogoutDialog = false
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    showDialog = showLogoutDialog
                )
            }
        }
    }
}