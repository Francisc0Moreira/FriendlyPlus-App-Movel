package estg.ipp.pt.friendly.ui.Login_Registo

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Database.User
import estg.ipp.pt.friendly.ui.Entity.DataAPI
import estg.ipp.pt.friendly.ui.Entity.RetrofitHelper
import estg.ipp.pt.friendly.ui.Firebase.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistoViewModel(application: Application) : AndroidViewModel(application) {

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: User? get() = _user.value

    private val _registoStatus: MutableLiveData<Boolean> = MutableLiveData()
    val registoStatus: Boolean? get() = _registoStatus.value

    private val firebaseAuthViewModel: LoginViewModel = LoginViewModel(application)

    fun registo(user: User, ctx: Context) {
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val tourAPI = retrofit.registo(user)
        val db = AppDatabase.getDatabase(ctx)

        tourAPI.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    CoroutineScope(Dispatchers.IO).launch {
                        _user.postValue(response.body())

                        // Registro na Firebase Authentication
                        firebaseAuthViewModel.register(user.email, user.password)

                        firebaseAuthViewModel.registerUserInFirestore(user, ctx)
                        // Login na Firebase Authentication
                        firebaseAuthViewModel.login(user.email, user.password)

                        _registoStatus.postValue(true)
                        user?.let { db.userDao().insertUser(it) }
                    }

                    Log.d("API_RESPONSE", "Registo efetuado com sucesso")
                } else {
                    _registoStatus.value = false
                    Log.d("API_RESPONSE", "Erro no registo,${response.message()}, Code: ${response.code()}\"")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _registoStatus.value = false
                Log.e("API_RESPONSE", "Falha na chamada da API para registo", t)
            }
        })
    }


}






@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegistoPage(navController: NavController) {
    Column {
        RegistoScreen(navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistoScreen(navController: NavController, modifier: Modifier = Modifier) {
    var ctx = LocalContext.current
    val viewModel: RegistoViewModel = viewModel()
    var registoStatus: Boolean? by remember { mutableStateOf(null) }

    val innerPadding = 16.dp
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var nascimento by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val generoOptions = listOf("Masculino", "Feminino", "Indeterminado")
    var genero by remember { mutableStateOf(generoOptions.first()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var telemovel by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagem de fundo
        Image(
            painter = painterResource(id = R.drawable.bola),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Caixa branca com o conteúdo de registo
        Box(
            modifier = Modifier
                .width(850.dp) // Ajusta a largura da caixa
                .height(800.dp) // Ajusta a altura da caixa
                .padding(32.dp) // Aumenta o padding interno
                .background(color = Color.White)  // Cor da caixa branca
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center
            ) {
                item {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Adiciona o texto à esquerda
                        Text(
                            text = "Olá,\nBem-vindo!",
                            modifier = Modifier
                                .padding(8.dp)
                                .align(alignment = Alignment.CenterVertically),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        // Adiciona a imagem à direita
                        Image(
                            painter = painterResource(id = R.drawable.friendly),
                            contentDescription = "Friendly",
                            modifier = Modifier
                                .size(150.dp) // Tamanho da imagem
                                .align(alignment = Alignment.CenterVertically)
                        )
                    }

                    Text(
                        text = "Registo",
                        modifier = Modifier
                            .padding(8.dp)
                            .align(alignment = Alignment.Center),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    // Campos de registo
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility = !passwordVisibility
                            }) {
                                Icon(
                                    imageVector = if (passwordVisibility) Icons.Default.KeyboardArrowLeft else Icons.Default.Lock,
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )

                    OutlinedTextField(
                        value = morada,
                        onValueChange = { morada = it },
                        label = { Text("Morada") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )

                    OutlinedTextField(
                        value = nascimento,
                        onValueChange = { nascimento = it },
                        label = { Text("Data de nascimento") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )

                    Text(text = "Gênero",
                        modifier = Modifier.align(alignment = Alignment.Center))

                    Row {
                        TextButton(onClick = { genero = "M"}){
                            Text(text = "Masculino",
                                color = Color.Black)
                        }
                        TextButton(onClick = { genero = "F"}, ) {
                            Text(text = "Feminino" ,
                                color = Color.Black)
                        }
                        TextButton(onClick = { genero = "I"}, ) {
                            Text(text = "Indeterminado",
                                color = Color.Black)
                        }
                    }

                    OutlinedTextField(
                        value = telemovel,
                        onValueChange = { telemovel = it },
                        label = { Text("Número de Telemóvel") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )

                    OutlinedTextField(
                        value = nif,
                        onValueChange = { nif = it },
                        label = { Text("NIF") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )

                    Button(
                        onClick = {
                            val user = User(null,null,nome,email,password,false,morada,nascimento,genero,telemovel,nif,0,true)
                            viewModel.registo(user, ctx)
                            registoStatus = viewModel.registoStatus

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Registar",
                            modifier = Modifier.size(30.dp))
                    }
                }
            }
        }

        if (registoStatus == true) {
            AlertDialog(
                onDismissRequest = {
                    registoStatus = null
                    navController.navigate("menuprincipal")
                },
                title = {
                    Text("Conta criada com sucesso!")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            registoStatus = null
                            navController.navigate("menuprincipal")
                        },
                    ) {
                        Text("Bem-vindo!")
                    }
                },
            )
        }


    }

}


