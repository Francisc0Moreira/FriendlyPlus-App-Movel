package estg.ipp.pt.friendly.ui.Reservas

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import estg.ipp.pt.friendly.ui.Database.Recintos
import estg.ipp.pt.friendly.ui.Database.Reserva
import estg.ipp.pt.friendly.ui.Firebase.FirestoreDataViewModel
import estg.ipp.pt.friendly.ui.Historico.HistoricoViewModel
import estg.ipp.pt.friendly.ui.RecintosShow.RecintoImage


@Composable
fun ReserveHistoryCard(
    navController: NavController,
    reserva: Reserva,
    recintosList: List<Recintos>,
    viewModel: HistoricoViewModel
) {
    val context = LocalContext.current

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

    var showDialog by remember { mutableStateOf(false) }
    var dialogResult by remember { mutableStateOf<String>("") }

}



@Composable
fun DisplayReserva(reserva:Reserva, navController: NavController, showDialog: Boolean, viewModel: HistoricoViewModel) {

    var localShowDialog by remember { mutableStateOf(showDialog) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ){Column (

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Preço: ${reserva.preco} €",
            style = TextStyle(
                color = Color.Gray,
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Data: ${reserva.dataInicial} €",
            style = TextStyle(
                color = Color.Gray,
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Estado: ${reserva.estado} €",
            style = TextStyle(
                color = Color.Gray,
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
       /* Button(
            onClick = {
                if (recinto != null) {
                    navController.navigate("recintosShow/${recinto.recintoID}")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(text = "Ver mais")
        }*/

        Spacer(modifier = Modifier.height(8.dp))

        if (reserva.estado == "Pendente") {
            Button(
                onClick = { localShowDialog = true
                    navController.navigate("pagamentos/${reserva.reservaID}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(text = "Ir para pagamento")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (reserva.estado == "Pendente") {
            Button(
                onClick = { localShowDialog = true

                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(text = "Cancelar Reserva")
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { localShowDialog = false },
                text = {
                    Text("Reserva cancelada")
                },
                confirmButton = {
                    Button(onClick = {
                        //showDialog = false
                        //dialogResult = "Reserva cancelada"
                        navController.navigate("menuprincipal")
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
    }
}
