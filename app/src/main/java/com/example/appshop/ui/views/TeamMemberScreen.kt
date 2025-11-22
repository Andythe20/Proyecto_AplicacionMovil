package com.example.appshop.ui.views

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

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

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(member.photoUrl),
                        contentDescription = member.name,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        // NOMBRE
                        Text(
                            member.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // ----- ICONO GITHUB -----
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val browser = Intent(Intent.ACTION_VIEW, Uri.parse(member.github))
                                context.startActivity(browser)
                            }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter("https://cdn-icons-png.flaticon.com/512/25/25231.png"),
                                contentDescription = "GitHub Icon",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("GitHub", color = MaterialTheme.colorScheme.primary)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // ----- ICONO LINKEDIN -----
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val browser = Intent(Intent.ACTION_VIEW, Uri.parse(member.linkedin))
                                context.startActivity(browser)
                            }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter("https://cdn-icons-png.flaticon.com/512/3536/3536505.png"),
                                contentDescription = "LinkedIn Icon",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("LinkedIn", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                "Duoc UC – Sede Viña del Mar, noviembre 2025\nDesarrollo de Aplicaciones Móviles",
                style = MaterialTheme.typography.bodySmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
