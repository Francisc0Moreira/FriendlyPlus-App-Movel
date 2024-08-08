package estg.ipp.pt.friendly.ui.Historico

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Database.Recintos
import estg.ipp.pt.friendly.ui.Database.Reserva
import estg.ipp.pt.friendly.ui.Database.User
import estg.ipp.pt.friendly.ui.Entity.DataAPI
import estg.ipp.pt.friendly.ui.Entity.RetrofitHelper
import estg.ipp.pt.friendly.ui.Firebase.FirestoreDataViewModel
import estg.ipp.pt.friendly.ui.Navigations.MyNavigationDrawer
import estg.ipp.pt.friendly.ui.Reservas.DisplayReserva
import estg.ipp.pt.friendly.ui.Reservas.ReserveHistoryCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoricoViewModel : ViewModel() {

    private val _reservasList = mutableStateOf<List<Reserva>>(emptyList())
    val reservasList: State<List<Reserva>> = _reservasList

    private val _recintosList = mutableStateOf<List<Recintos>>(emptyList())
    val recintosList: State<List<Recintos>> = _recintosList

    /*fun loadReservas(context: Context) {
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val db = AppDatabase.getDatabase(context)
        val user: User? = runBlocking(Dispatchers.IO) {
            db.userDao().getAnyUser()
        }

        val tourAPI = user?.let { retrofit.getReserveHistory(user.userID) }

        if (tourAPI != null) {
            tourAPI.enqueue(object : Callback<List<Reserva>> {
                override fun onResponse(
                    call: Call<List<Reserva>>,
                    response: Response<List<Reserva>>
                ) {
                    _reservasList.value = response.body() ?: emptyList()
                    Log.d("API_Response", "Dados das reservas recebidos${_reservasList.value}")
                }

                override fun onFailure(call: Call<List<Reserva>>, t: Throwable) {
                    Log.e("API_RESPONSE", "Falha na chamada da API", t)
                }
            })
        }

    }*/
    private val _reservaId: MutableLiveData<Int?> = MutableLiveData()
    val reservaId: Int? get() = _reservaId.value
    fun cancelarReserva(reservaId: Int) {
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val reservaAPI = retrofit.cancelarReserva(reservaId)

        reservaAPI.enqueue(object : Callback<Reserva> {
            override fun onResponse(call: Call<Reserva>, response: Response<Reserva>) {
                if (response.isSuccessful) {
                    Log.d("API_RESPONSE", "Reserva cancelada com sucesso")
                } else {
                    Log.e("API_RESPONSE", "Erro ao cancelar a reserva: ${response.message()}, Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Reserva>, t: Throwable) {
                Log.e("API_RESPONSE", "Falha ao cancelar a reserva: ${t.message}")
            }
        })
    }
}

@Composable
fun Historico(navController: NavController) {
    Column {
        MyNavigationDrawer(navController, "Histórico de reservas", "reserveHistory")
    }
}

@Composable
fun HistoricoScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val user: User? = runBlocking(Dispatchers.IO) {
        db.userDao().getAnyUser()
    }
    var showDialog by remember { mutableStateOf(false) }
    var dialogResult by remember { mutableStateOf<String?>("") }
    val firestoreModel: FirestoreDataViewModel = viewModel()
    var historicoViewModel: HistoricoViewModel = viewModel()


    val userReservas by firestoreModel.userReservas.observeAsState(emptyList())
    LaunchedEffect(userReservas) {
        user?.userID?.let { firestoreModel.getReservasByUserID(it) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 60.dp)
    ) {
        items(userReservas.size) { index ->
            val reserva = userReservas[index]
            Log.d(
                "Log_reserva",
                "Sucesso a obter cada reserva ${reserva}"
            )

            Column {
                        DisplayReserva(
                            reserva,
                            navController,
                            showDialog,
                            historicoViewModel
                        )
                }

            }
        }
    }






