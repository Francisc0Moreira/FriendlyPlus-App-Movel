package estg.ipp.pt.friendly.ui.Classes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import estg.ipp.pt.friendly.ui.Database.Pagamento
import estg.ipp.pt.friendly.ui.Database.Reserva
import estg.ipp.pt.friendly.ui.Entity.DataAPI
import estg.ipp.pt.friendly.ui.Entity.RetrofitHelper
import estg.ipp.pt.friendly.ui.Firebase.FirestoreDataViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagamentoViewModel: ViewModel() {


    private val _pagamentoID: MutableLiveData<Int> = MutableLiveData()
    val pagamento: Int? get() = _pagamentoID.value


    fun createPagamento(
        reservaID: Int,
        pontos: Int,
        pagamento: Pagamento,
        navController: NavController,
        firestoreReserva: MutableLiveData<Reserva?>,
        FireStoreModel: FirestoreDataViewModel
    ){
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val tourAPI = retrofit.createPagamento(reservaID, pontos, pagamento)

        tourAPI.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                _pagamentoID.value = response.body()
                firestoreReserva.value?.estado  = "Confirmada"
                FireStoreModel.saveReservaConfirmed(firestoreReserva)

                Log.d("API Pagamento", "Pagamento ${_pagamentoID.value}")
                navController.navigate("menuprincipal")
                Log.d("API_RESPONSE", "Pagamento bem sucedido")
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e("API_RESPONSE", "Falha na chamada da API efetuar o pagamento", t)
            }
        })

    }

}