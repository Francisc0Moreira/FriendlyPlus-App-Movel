package estg.ipp.pt.friendly.ui.Classes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import estg.ipp.pt.friendly.ui.Database.Reserva
import estg.ipp.pt.friendly.ui.Entity.DataAPI
import estg.ipp.pt.friendly.ui.Entity.RetrofitHelper
import estg.ipp.pt.friendly.ui.Firebase.FirestoreDataViewModel
import estg.ipp.pt.friendly.ui.Pagamento.ReservaID
import estg.ipp.pt.friendly.ui.RecintosShow.ShowReservaExistenteDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservasViewModel: ViewModel() {

    private val _reservaStatus: MutableLiveData<Boolean> = MutableLiveData()
    val reservaStatus: Boolean? get() = _reservaStatus.value

    private val _reservaId: MutableLiveData<Int?> = MutableLiveData()
    val reservaId: Int? get() = _reservaId.value

    fun createReserva(reserva: Reserva, FireStoreModel: FirestoreDataViewModel) {
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val reservaAPI = retrofit.createReserva(reserva)



        reservaAPI.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val reservaResponse = response.body()

                    _reservaStatus.value = true
                    _reservaId.value = reservaResponse
                    reserva.reservaID = _reservaId.value
                    FireStoreModel.saveReserva(reserva)
                    Log.d("API_RESPONSE", "Reserva criada com sucesso")
                } else {
                    _reservaStatus.value = false
                    Log.e("API_RESPONSE", "Erro na criação da reserva: ${response.message()}, Code: ${response.code()}")


                }
            }


            override fun onFailure(call: Call<Int>, t: Throwable) {
                _reservaStatus.value = false
                Log.e("API_RESPONSE", "Falha na chamada da API para criar reserva", t)

            }
        })
    }

    private val _reserva: MutableLiveData<Reserva> = MutableLiveData()
    val reserva: Reserva? get() = _reserva.value

    fun loadReservabyID(reservaID : Int) {
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val tourAPI = retrofit.getReservabyID(reservaID)

        tourAPI.enqueue(object : Callback<Reserva> {
            override fun onResponse(call: Call<Reserva>, response: Response<Reserva>) {
                _reserva.value = response.body()
                Log.d("API_RESPONSE", "Dados da reserva recebidos")
            }

            override fun onFailure(call: Call<Reserva>, t: Throwable) {
                Log.e("API_RESPONSE", "Falha na chamada da API para receber a reserva", t)
            }
        })
    }

}