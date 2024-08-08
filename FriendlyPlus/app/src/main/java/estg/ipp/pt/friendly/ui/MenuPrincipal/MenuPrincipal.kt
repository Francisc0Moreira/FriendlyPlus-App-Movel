package estg.ipp.pt.friendly.ui.MenuPrincipal

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Classes.RecintosViewModel
import estg.ipp.pt.friendly.ui.Database.Recintos
import estg.ipp.pt.friendly.ui.Entity.DataAPI
import estg.ipp.pt.friendly.ui.Entity.RetrofitHelper
import estg.ipp.pt.friendly.ui.Navigations.MyNavigationDrawer
import estg.ipp.pt.friendly.ui.Recintos.RecintoCard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun MenuPrincipal(navController: NavController){
    Column {
        MyNavigationDrawer(navController,"Recintos Desportivos","menu")
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MenuScreen(navController: NavController) {
    val viewModel: RecintosViewModel = viewModel()

    // Observe state using State
    val recintosList by remember { viewModel.pointsList }


    // Trigger API call when the screen is first created
    DisposableEffect(Unit) {
        viewModel.loadRecintos()
        onDispose { /* cleanup logic */ }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 60.dp)
    ) {

        // Item 1: Imagem "futebol"
        item {
            Image(
                painter = painterResource(id = R.drawable.futebol),
                contentDescription = "Futebol",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Aqui vais encontrar todos os recintos disponíveis para poderes reservar!",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp, top = 10.dp)
            )
        }

        items(recintosList.size) { index ->
            val recinto = recintosList[index]
            RecintoCard(navController, recinto)
        }
    }
}
