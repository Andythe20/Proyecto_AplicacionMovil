package com.example.appshop.ui.views

import android.content.Intent
import android.net.Uri
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.appshop.R

data class TeamMember(
    val name: String,
    val photoUrl: String,
    val github: String,
    val linkedin: String
)

@Composable
fun TeamMemberScreen(
    team: List<TeamMember>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Equipo de Desarrollo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(team.size) { index ->
            val member = team[index]

            // --- Animación de aparición ---
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        // --- Animación ligera en la imagen (zoom-in) ---
                        var imageLoaded by remember { mutableStateOf(false) }

                        val imageScale by animateFloatAsState(
                            targetValue = if (imageLoaded) 1f else 0.85f,
                            animationSpec = tween(600)
                        )

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = member.photoUrl,
                                onSuccess = { imageLoaded = true }
                            ),
                            contentDescription = member.name,
                            modifier = Modifier
                                .size(90.dp)
                                .graphicsLayer(scaleX = imageScale, scaleY = imageScale)
                                .clip(CircleShape)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {

                            Text(
                                member.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // --- Animación para iconos (escala al tocar) ---
                            AnimatedLink(
                                label = "GitHub",
                                iconUrl = "https://cdn-icons-png.flaticon.com/512/25/25231.png"
                            ) {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(member.github))
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            AnimatedLink(
                                label = "LinkedIn",
                                iconUrl = "https://cdn-icons-png.flaticon.com/512/3536/3536505.png"
                            ) {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(member.linkedin))
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    "Duoc UC – Sede Viña del Mar, noviembre 2025\nDesarrollo de Aplicaciones Móviles",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // --- Aquí va tu pastel animado ---
                BouncingCake()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}


@Composable
fun AnimatedLink(label: String, iconUrl: String, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = tween(150)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                onClickLabel = label,
                onClick = {
                    pressed = true
                    onClick()
                    pressed = false
                }
            )
    ) {
        Image(
            painter = rememberAsyncImagePainter(iconUrl),
            contentDescription = "$label Icon",
            modifier = Modifier.size(20.dp)
        )

    }
}

@Composable
fun BouncingCake() {

    var pressed by remember { mutableStateOf(false) }

    // Movimiento vertical
    val offset by animateDpAsState(
        targetValue = if (pressed) (-20).dp else 0.dp,
        animationSpec = tween(
            durationMillis = 300,
            easing = {
                // efecto rebote
                OvershootInterpolator(3f).getInterpolation(it)
            }
        ),
        finishedListener = {
            // vuelve automáticamente
            pressed = false
        }
    )

    Column(
        modifier = Modifier
            .offset(y = offset)
            .clickable { pressed = true },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.cake),
            contentDescription = "Pastel Saltarín",
            modifier = Modifier.size(80.dp)
        )
    }
}
