package com.example.appshop.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    navController: NavHostController,
    title: String = "OnlyFlans", // Título predeterminado
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // --- Encabezado del Drawer ---
                Text(
                    "OnlyFlans",
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider(Modifier.padding(8.dp), DividerDefaults.Thickness, DividerDefaults.color)


                // --- Inicio de los ítems del Drawer ---
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Default.Home , contentDescription = null ) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("home")
                    }
                )

                HorizontalDivider(
                    Modifier.padding(vertical = 8.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color)

                // --- Carrito ---
                NavigationDrawerItem(
                    label = { Text("Carrito") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null ) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("cart")
                    }
                )

                // --- Historial de compras ---
                // Futura implementación

                // ---Separador ---
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )

                // --- Perfil ---
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null )},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("createProfile")
                    }
                )

                // --- Separador ---
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )

                // --- Cerrar sesión ---
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    icon = { Icon(Icons.Default.Close, contentDescription = null) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showLogoutDialog = true
                    },
                    //modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
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
                            style = MaterialTheme.typography.displayMedium,
                            fontSize = 24.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                        }
                        AppDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            navController = navController
                        )
                    }
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
        // --- Diálogo de confirmación ---
        LogoutConfirmationDialog(
            showDialog = showLogoutDialog,
            onDismiss = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
    }
}