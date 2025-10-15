import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appshop.R

data class Promotion(
    val title: String,
    val description: String,
    val imageRes: Int
)

@Composable
fun PromotionsSection() {
    val promos = listOf(
        Promotion("Descuento en Alfajores", "Compra 6 alfajores y obtén un 10% de descuento.", R.drawable.alfajores2),
        Promotion("Combo Café y Queque", "Disfruta un café y un queque por solo $3.000.", R.drawable.cafe_y_muffin)
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        promos.forEach { promo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    Image(
                        painter = painterResource(id = promo.imageRes),
                        contentDescription = promo.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(promo.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(promo.description, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
