package estg.ipp.pt.friendly.ui.Firebase

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import estg.ipp.pt.friendly.ui.Database.Reserva
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreDataViewModel(application: Application) : AndroidViewModel(application) {

    val db: FirebaseFirestore
    val collectionName: String
    val userReservas: MutableLiveData<List<Reserva>> = MutableLiveData()
    val reserva : MutableLiveData<Reserva?> = MutableLiveData()

    init {
        db = FirebaseFirestore.getInstance()
        collectionName = "RESERVAS"
    }

    fun saveReserva(reserva: Reserva) {
        viewModelScope.launch {
            try {
                db.collection(collectionName).add(reserva)
                    .addOnSuccessListener {
                        Log.d("FirestoreDataViewModel", "Reserva criada com sucesso")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreDataViewModel", "Erro ao guardar a reserva", e)
                    }
            } catch (e: Exception) {
                Log.e("FirestoreDataViewModel", "Exceção ao guardar a reserva", e)
            }
        }
    }

    fun getReservasByUserID(userID: Int) {
        viewModelScope.launch {
            try {
                val query = db.collection(collectionName)
                    .whereEqualTo("userID", userID)

                try {
                    val documents = query.get().await()

                    val reservasList = mutableListOf<Reserva>()
                    for (document in documents) {
                        val reserva = document.toObject(Reserva::class.java)
                        reservasList.add(reserva)
                    }

                    // Update userReservas on the main thread
                    withContext(Dispatchers.Main) {
                        userReservas.postValue(reservasList)
                        Log.d(
                            "Log_userReservas",
                            "Sucesso a obter as reservas ${userReservas.value}"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("FirestoreDataViewModel", "Erro ao obter as reservas do utilizador", e)
                }
            } catch (e: Exception) {
                Log.e("FirestoreDataViewModel", "Exceção ao obter as reservas do utilizador", e)
            }
        }
    }

    fun getReservasByID(reservaID: Int) {
        viewModelScope.launch {
            try {
                val query = db.collection(collectionName)
                    .whereEqualTo("reservaID", reservaID)

                query.get()
                    .addOnSuccessListener { documents ->
                        var reservabyID: Reserva? = null // Inicialize com null
                        for (document in documents) {
                            val reserva = document.toObject(Reserva::class.java)
                            reservabyID = reserva
                        }
                        reserva.value = reservabyID
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreDataViewModel", "Erro ao obter as reservas do utilizador", e)
                    }
            } catch (e: Exception) {
                Log.e("FirestoreDataViewModel", "Exceção ao obter as reservas do utilizador", e)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun saveReservaConfirmed(reserva: MutableLiveData<Reserva?>) {
        viewModelScope.launch {
            try {
                val reservaData = reserva.value
                if (reservaData != null) {
                    val reservaID = reservaData.reservaID
                    if (reservaID != null) {
                        val query = db.collection(collectionName)
                            .whereEqualTo("reservaID", reservaID)

                            query.get()
                                .addOnSuccessListener { documents ->
                                    for(document in documents){
                                        document.reference.set(reservaData)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirestoreDataViewModel", "Erro ao obter as reservas do utilizador", e)
                                }
                    } else {
                        Log.e("FirestoreDataViewModel", "A reserva não possui um ID")
                    }
                } else {
                    Log.e("FirestoreDataViewModel", "A reserva é nula")
                }
            } catch (e: Exception) {
                Log.e("FirestoreDataViewModel", "Exceção ao salvar/atualizar a reserva", e)
            }
        }
    }


}
