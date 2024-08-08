package estg.ipp.pt.friendly.ui.Recintos

import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Database.Recintos
import estg.ipp.pt.friendly.ui.RecintosShow.RecintoImage
import java.io.File


@Composable
fun RecintoCard(
    navController: NavController,
    recinto: Recintos
) {
    val context = LocalContext.current

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            RecintoImage(recinto.imagem)

            Spacer(modifier = Modifier.height(16.dp))

            // Nome do Recinto
            Text(
                text = recinto.name,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preço: ${recinto.preco} €",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { navController.navigate("recintosShow/${recinto.recintoID}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(text = "Ver mais")
            }
        }
    }
}