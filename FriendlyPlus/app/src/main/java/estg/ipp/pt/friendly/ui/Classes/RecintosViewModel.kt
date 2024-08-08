package estg.ipp.pt.friendly.ui.Classes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import estg.ipp.pt.friendly.ui.Database.Recintos
import estg.ipp.pt.friendly.ui.Entity.DataAPI
import estg.ipp.pt.friendly.ui.Entity.RetrofitHelper
import estg.ipp.pt.friendly.ui.RecintosShow.RecintoID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecintosViewModel: ViewModel() {

    private val _recinto: MutableLiveData<Recintos> = MutableLiveData()
    val recinto: Recintos? get() = _recinto.value

    fun loadRecintoById() {
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val tourAPI = retrofit.getRecintobyID(RecintoID)

        tourAPI.enqueue(object : Callback<Recintos> {
            override fun onResponse(call: Call<Recintos>, response: Response<Recintos>) {
                _recinto.value = response.body()
                Log.d("API_RESPONSE", "Dados do recinto recebidos")
            }

            override fun onFailure(call: Call<Recintos>, t: Throwable) {
                Log.e("API_RESPONSE", "Falha na chamada da API para recinto por ID", t)
            }
        })
    }

    private val _pointsList = mutableStateOf<List<Recintos>>(emptyList())
    val pointsList: State<List<Recintos>> = _pointsList

    fun loadRecintos() {
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val tourAPI = retrofit.getRecintos()

        tourAPI.enqueue(object : Callback<List<Recintos>> {
            override fun onResponse(call: Call<List<Recintos>>, response: Response<List<Recintos>>) {
                _pointsList.value = response.body() ?: emptyList()
                Log.d("API_RESPONSE", "Dados recebidos")
            }

            override fun onFailure(call: Call<List<Recintos>>, t: Throwable) {
                Log.e("API_RESPONSE", "Falha na chamada da API", t)
            }
        })
    }

}