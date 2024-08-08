package estg.ipp.pt.friendly.ui.Firebase


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Database.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(application:Application): AndroidViewModel(application) {

    val authState : MutableLiveData<AuthStatus>
    val fAuth : FirebaseAuth

    init {
        authState = MutableLiveData(AuthStatus.NOLOGGIN)
        fAuth = Firebase.auth
    }

    fun register(email:String, password:String){
        viewModelScope.launch {
            try{
                val result = fAuth.createUserWithEmailAndPassword(email, password).await()
                if (result != null && result.user != null){
                    authState.postValue(AuthStatus.LOGGED)
                    Log.d("Register","logged in")
                    return@launch
                }
                Log.d("Register","anonymous")
                authState.postValue(AuthStatus.NOLOGGIN)
                return@launch
            } catch( e:Exception) {}
        }
    }

    fun login(email:String, password:String){
        viewModelScope.launch {
            try{
                val result = fAuth.signInWithEmailAndPassword(email, password).await()
                if (result != null && result.user != null){
                    authState.postValue(AuthStatus.LOGGED)
                    Log.d("Login","logged in")
                    return@launch
                }
                Log.d("Login","anonymous")
                authState.postValue(AuthStatus.NOLOGGIN)
                return@launch
            } catch( e:Exception) {}
        }
    }

     fun registerUserInFirestore(user: User, ctx: Context) {


        val db = FirebaseFirestore.getInstance()



        db.collection("users")
            .document(user.email)
            .set(user)
            .addOnSuccessListener {

                Log.d("Registo", "DocumentSnapshot added with ID: ${user.email}")
            }
            .addOnFailureListener { e ->
                Log.w("Registo", "Error adding document", e)
            }
    }

    fun logout(){
        viewModelScope.launch {
            fAuth.signOut()
            authState.postValue(AuthStatus.NOLOGGIN)
            Log.d("Login","logout")
        }
    }

    enum class AuthStatus {
        LOGGED, NOLOGGIN
    }






}
