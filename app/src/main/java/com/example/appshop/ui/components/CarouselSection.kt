package com.example.appshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Imagen $page",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
