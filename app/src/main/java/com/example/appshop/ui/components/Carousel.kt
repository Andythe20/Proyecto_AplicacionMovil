package com.example.appshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appshop.R
import kotlinx.coroutines.delay

@Composable
fun CarouselSection() {
    val images = listOf(
        R.drawable.alfajores2,
        R.drawable.cafe_y_queque,
        R.drawable.galletas_donas_brownies,
        R.drawable.tortas_variedades
    )

    val titles = listOf(
        "Alfajores Artesanales",
        "Café y Queques",
        "Galletas y Postres",
        "Tortas Variedades"
    )

    val descriptions = listOf(
        "Deliciosos alfajores rellenos de dulce de leche",
        "Disfruta nuestro café premium con queques caseros",
        "Galletas, donas y brownies recién horneados",
        "Tortas personalizadas para toda ocasión"
    )

    // Estado del pager
    val pagerState = rememberPagerState(pageCount = { images.size })

    // Efecto para cambiar de página automáticamente
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // 3 segundos por imagen
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ){
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 8.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    Image(
                        painter = painterResource(id = images[page]),
                        contentDescription = "Imagen ${titles[page]}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        androidx.compose.ui.graphics.Color.Transparent,
                                        androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.6f)
                                    ),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )

                    // Texto que cambia con cada imagen
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = titles[page],
                            style = MaterialTheme.typography.headlineMedium,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = descriptions[page],
                            style = MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

            }
        }

    }

}
