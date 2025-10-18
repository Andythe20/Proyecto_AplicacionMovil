package com.example.appshop.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appshop.R
import com.example.appshop.ui.theme.LatoFontFamily
import com.example.appshop.ui.theme.PacificoFontFamily

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.onlyflans_logo),
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(50.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Bienvenido a la aplicación oficial de OnlyFlans!!",
            style =
                TextStyle(
                    fontSize = 24.sp,
                    fontFamily = PacificoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                ),
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Mejor repostería de Chile, est. 2025",
            style =
                TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                ),
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier.fillMaxWidth().height(60.dp),
        ) {
            Text(text = "Iniciar sesión", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                navController.navigate("signup")
            },
            modifier = Modifier.fillMaxWidth().height(60.dp),

        ) {
            Text(text = "Registrarse", fontSize = 22.sp)
        }
    }
}
