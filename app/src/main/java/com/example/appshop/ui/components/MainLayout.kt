package com.example.appshop.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "OnlyFlans",
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("home")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Crear Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("createProfile")
                    }
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
    }
}