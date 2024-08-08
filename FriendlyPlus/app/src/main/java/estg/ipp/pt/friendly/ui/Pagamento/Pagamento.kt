package estg.ipp.pt.friendly.ui.Pagamento


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Classes.PagamentoViewModel
import estg.ipp.pt.friendly.ui.Classes.ReservasViewModel
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Database.Pagamento
import estg.ipp.pt.friendly.ui.Database.Reserva
import estg.ipp.pt.friendly.ui.Firebase.FirestoreDataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

var ReservaID: Int = 0
var isPagamentoSelected: Boolean = false



@Composable
fun PagamentosPage(navController: NavController, ID:Int) {
    Column {
        ReservaID = ID
        PagamentoScreen(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagamentoScreen(navController: NavController){
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val viewModel: PagamentoViewModel = viewModel()
    val ReservaviewModel: ReservasViewModel = viewModel()
    val FireStoreModel : FirestoreDataViewModel = viewModel()

    val user = runBlocking(Dispatchers.IO) {
        db.userDao().getAnyUser()
    }

    var reserva: Reserva? by remember { mutableStateOf(null) }

    reserva = ReservaviewModel.reserva

    var firestoreReserva = FireStoreModel.reserva

    var pagamentoStatus: Boolean? by remember {
        mutableStateOf(null)
    }

    DisposableEffect(Unit) {
        ReservaviewModel.loadReservabyID(ReservaID)
        FireStoreModel.getReservasByID(ReservaID)
        onDispose {  }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Pagamento")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        bottomBar = {

        },
    )
        { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                reserva?.let { DetalhesJogoSection(it) }
                Spacer(modifier = Modifier.height(16.dp))


                    PagamentoSection()
                    // Adicione o FloatingActionButton aqui
                    FloatingActionButton(
                        onClick = {
                            // Lógica para efetuar o pagamento
                            val pagamento = user?.userID?.let {
                                reserva?.let { it1 ->
                                    Pagamento(
                                        null,
                                        it,
                                        2,
                                        it1.preco
                                    )
                                }
                            }
                            Log.d("API Pagamento", "Teste: ${ReservaID}")
                            user?.pontos?.let {
                                if (pagamento != null) {
                                    viewModel.createPagamento(ReservaID,it, pagamento, navController, firestoreReserva, FireStoreModel)
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(56.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Pay")
                    }

            }
        }


}


@Composable
fun DetalhesJogoSection(reserva: Reserva) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(vertical = 16.dp)
    ) {
        // Adiciona uma imagem de fundo (substitua a URL pela sua imagem)
        Image(
            painter = painterResource(id = R.drawable.bola),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(400.dp),
            contentScale = ContentScale.Crop
        )

        // Adiciona uma caixa branca sobre a imagem
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White.copy(alpha = 0.8f))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Adiciona o conteúdo dentro da caixa branca
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Data do Jogo: ${reserva.dataInicial}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hora do Jogo: ${reserva.horaJogo}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Preço: ${reserva.preco} €",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagamentoSection() {
    var numeroCartao by remember { mutableStateOf("") }
    var dataValidade by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        TextField(
            value = numeroCartao,
            onValueChange = { numeroCartao = it },
            label = { Text("Número do Cartão") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Linha para a data de validade e o CVC
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Campo para a data de validade
            TextField(
                value = dataValidade,
                onValueChange = { dataValidade = it },
                label = { Text("Mes/Ano") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Campo para o CVC
            TextField(
                value = cvc,
                onValueChange = { cvc = it },
                label = { Text("CVC") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(vertical = 8.dp)
            )
        }
    }
}



