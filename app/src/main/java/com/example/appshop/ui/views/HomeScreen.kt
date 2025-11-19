package com.example.appshop.ui.views

import com.example.appshop.ui.components.PromotionsSection
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appshop.ui.components.CarouselSection
import com.example.appshop.ui.components.FooterSection
import com.example.appshop.ui.components.HeaderSection
import com.example.appshop.ui.components.PatternBackground
import com.example.appshop.ui.theme.white
import com.example.appshop.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    navController: NavController
){
    val context = LocalContext.current
    val openUrl = { url: String ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
    
    val currentUser = viewModel.loggedInUser
    val username = currentUser?.name ?: "Usuario"

    // 1. El Box raíz recibe el modifier para respetar el padding del Scaffold.
    Box(modifier = modifier) {
        PatternBackground() // Fondo

        // 2. Un segundo Box crea la pila para el fondo y el contenido.
        Box(modifier = Modifier.fillMaxSize()) {

            // 3. BoxWithConstraints ahora está en el contexto correcto para medir.
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                if (maxWidth < 600.dp) {
                    // Diseño para pantallas pequeñas (teléfonos en vertical)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item { HeaderSection(username) }
                        item { 
                            Spacer(modifier = Modifier.height(16.dp)) 
                            CarouselSection(navController = navController)
                        }
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .height(70.dp)
                                    .border(3.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Promociones disponibles!",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        item { 
                            Spacer(modifier = Modifier.height(25.dp)) 
                            PromotionsSection() 
                        }
                        item { 
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .border(3.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.medium)
                            ) {
                                FooterSection(openUrl) 
                            }
                        }
                    }
                } else {
                    // Diseño para pantallas grandes (tablets o teléfonos en horizontal)
                    LazyColumn(Modifier.fillMaxSize()) {
                        item { HeaderSection(username) }
                        item {
                            Row(Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    CarouselSection(navController = navController)
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                            .height(70.dp)
                                            .border(3.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                                            .background(white)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalOffer,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = "Promociones!",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(25.dp))
                                    PromotionsSection()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .border(3.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                                            .background(MaterialTheme.colorScheme.background)
                                    ){
                                        FooterSection(openUrl)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}