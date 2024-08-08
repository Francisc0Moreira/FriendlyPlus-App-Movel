package estg.ipp.pt.friendly.ui.RecintosShow

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Classes.RecintosViewModel
import estg.ipp.pt.friendly.ui.Classes.ReservasViewModel
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Database.Recintos
import estg.ipp.pt.friendly.ui.Database.Reserva
import estg.ipp.pt.friendly.ui.Entity.DataAPI
import estg.ipp.pt.friendly.ui.Entity.RetrofitHelper
import estg.ipp.pt.friendly.ui.Firebase.FirestoreDataViewModel
import estg.ipp.pt.friendly.ui.Maps.MapScreen
import estg.ipp.pt.friendly.ui.Navigations.MyNavigationDrawer
import estg.ipp.pt.friendly.ui.Notifications.showNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


var RecintoID: Int = 0




@Composable
fun RecintosShow(navController: NavController, ID: Int){
    Column {
        RecintoID = ID;
        MyNavigationDrawer(navController,"Recinto", "recinto")
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecintosShowScreen(navController: NavController) {

    val RecintosviewModel: RecintosViewModel = viewModel()
    val ReservasviewModel: ReservasViewModel = viewModel()
    val FireStoreModel: FirestoreDataViewModel = viewModel()

    var recinto: Recintos? by remember { mutableStateOf(null) }

    var reservaDialog:Boolean? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)


    val user = runBlocking(Dispatchers.IO) {
        db.userDao().getAnyUser()
    }

    recinto = RecintosviewModel.recinto

    var reservaID = ReservasviewModel.reservaId


    DisposableEffect(Unit) {
        RecintosviewModel.loadRecintoById()

        onDispose {  }
    }

    recinto?.let {recinto ->


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 60.dp)
        ) {
            item {
                Text(
                    text = recinto.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                RecintoImage(recinto.imagem, modifier = Modifier)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Desde: ${recinto.preco} €")
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "Descrição:",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = recinto.description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                val latitude: Double = recinto.latitude.toDouble()
                val longitude: Double = recinto.longitude.toDouble()

                val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(
                            color = androidx.compose.ui.graphics.Color.Gray,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    MapScreen(latitude, longitude)
                }
                OpenGoogleMapsButton(LocalContext.current, mapIntent)

                Button(
                    onClick = {
                        recinto.contacto?.let { contato ->
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:$contato")
                            ContextCompat.startActivity(context, intent, null)
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)

                ) {
                    Text(text = "Contacto: ${recinto.contacto ?: "Não disponível"}")
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "Definir horário",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Escolha a data:")
                }

                var dateSelected by remember { mutableStateOf("") }
                var timeSelected by remember { mutableStateOf("") }
                var horaReserva: String = obterHoraAtualString()


                DateAndTimeInput(
                    onDateSelected = { dateSelected = it },
                    onTimeSelected = { timeSelected = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Pode efetuar o pagamento agora ou depois!" +
                    " Caso cancele, deve de ir ao histórico de reservas para poder pagar.")
                Spacer(modifier = Modifier.height(16.dp))
                var reserva : Reserva? = null
                var isClicked by remember {mutableStateOf(false)}
                Button(
                    onClick = {
                        reserva = user?.let {
                            it.userID?.let { it1 ->
                                Reserva(null, recinto.recintoID,
                                    it1,null,null, dateSelected,horaReserva ,timeSelected, null,30.00,"Pendente" )
                            }
                        }

                        if (reserva != null) {
                            ReservasviewModel.createReserva(reserva!!,FireStoreModel)
                        }
                        reservaDialog = ReservasviewModel.reservaStatus
                        reservaID = ReservasviewModel.reservaId

                              },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Reservar")
                }

                if (isClicked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        reserva?.let { agendarNotificacaoParaReserva(it) }
                    }
                }
            }
        }
        if(reservaDialog == true){
            reservaID?.let { ShowReservaCriadaDialog(navController, it) }
        }else if(reservaDialog ==false){
            ShowReservaExistenteDialog(navController)
        } else {}


    }

}

@Composable
fun ShowReservaCriadaDialog(navController: NavController, reservaID: Int) {
    AlertDialog(
        onDismissRequest = {
            navController.navigate("menuprincipal")
        },
        title = {
            Text("Reserva criada com sucesso!")
        },
        confirmButton = {
            Button(
                onClick = {
                    navController.navigate("pagamentos/${reservaID}")
                },
            ) {
                Text("Continue")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    navController.navigate("menuprincipal")
                }
            ) {
                Text("Pagar depois!")
            }
        }
    )
}



@Composable
fun ShowReservaExistenteDialog(navController: NavController) {
    AlertDialog(
        onDismissRequest = {
                            navController.navigate("menuprincipal")
        },
        title = {
            Text("Já existe uma reserva nessa hora!")
        },
        confirmButton = {
            Button(
                onClick = {
                },
            ) {
                Text("Tente Novamente")
            }
        }
    )
}

@Composable
fun RecintoImage(recintoName: String?, modifier: Modifier = Modifier) {
    val defaultImageId = R.drawable.futebol

    val imageId = when (recintoName) {
        "xsport.png" -> R.drawable.xsport
        "campodegaia.png" -> R.drawable.campodegaia
        "campolustosa.png" -> R.drawable.campolustosa
        "padelmaia.png"-> R.drawable.padelmaia
        else -> defaultImageId
    }

    AsyncImage(
        model = imageId,
        contentDescription = "xsport",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(MaterialTheme.shapes.medium)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun obterHoraAtualString(): String {
    val zonaHorariaLocal = ZoneId.systemDefault()
    val horaAtual = LocalTime.now(zonaHorariaLocal)
    val formato = DateTimeFormatter.ofPattern("HH:mm")
    return horaAtual.format(formato)
}

@Composable
fun OpenGoogleMapsButton(context: Context, mapIntent: Intent) {
    Button(
        onClick = {
            context.startActivity(mapIntent)
        },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(text = "Abrir no Google Maps")
    }
}
@Composable
@RequiresApi(Build.VERSION_CODES.S)
fun agendarNotificacaoParaReserva(reserva: Reserva) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val dataInicial = dateFormat.parse(reserva.dataInicial)
    val horaJogo = timeFormat.parse(reserva.horaJogo)

    val calendar = Calendar.getInstance()
    calendar.time = dataInicial
    calendar.set(Calendar.HOUR_OF_DAY, horaJogo.hours)
    calendar.set(Calendar.MINUTE, horaJogo.minutes)
    calendar.add(Calendar.MINUTE, -1)

    val currentTime = Calendar.getInstance()

    if (currentTime == calendar) {

        val context = LocalContext.current
        val title = "Notificação de reserva"
        val content = "Tem uma reserva marcada, verifique o histórico de reservas"

        showNotification(
            context = context,
            title = title,
            content = content,
            triggerTimeMillis = calendar.timeInMillis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateAndTimeInput(
    onDateSelected: (String) -> Unit,
    onTimeSelected: (String) -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    // Lista para armazenar os estados de seleção de cada botão
    var selectedTimes by remember { mutableStateOf(List(6) { false }) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Campo de entrada para a data
        TextField(
            value = selectedDate,
            onValueChange = {
                selectedDate = it
                onDateSelected(it)
            },
            label = { Text("yyyy-MM-dd") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Lista de botões para seleção de hora
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOf(
                "18:00 - 19:00",
                "19:00 - 20:00",
                "20:00 - 21:00",
                "21:00 - 22:00",
                "22:00 - 23:00",
                "23:00 - 00:00"
            ).forEachIndexed { index, hora ->
                Button(
                    onClick = {
                        selectedTime = hora
                        onTimeSelected(hora)

                        // Atualizar o estado individual do botão clicado
                        selectedTimes = selectedTimes.toMutableList().also {
                            it[index] = !it[index]
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // Texto com a cor correspondente
                    Text(
                        text = hora,
                        color = if (selectedTimes[index]) Color.White else Color.Black
                    )
                }
            }
        }

    }
}


