package estg.ipp.pt.friendly.ui.Login_Registo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Database.LoginModule
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


class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: User? get() = _user.value

    private val _loginStatus: MutableLiveData<Boolean> = MutableLiveData()
    val loginStatus: Boolean? get() = _loginStatus.value

    private val firebaseAuthViewModel: LoginViewModel = LoginViewModel(application)


    fun login(loginModule: LoginModule, ctx: Context) {
        val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
        val tourAPI = retrofit.login(loginModule)
        val db = AppDatabase.getDatabase(ctx)

        firebaseAuthViewModel.login(loginModule.email, loginModule.password)

        firebaseAuthViewModel.authState.observeForever { authStatus ->
            when (authStatus) {
                LoginViewModel.AuthStatus.LOGGED -> {
                    // Login bem-sucedido, você pode adicionar lógica adicional se necessário
                    _loginStatus.postValue(true)
                    Log.d("Firebase_Login", "Login efetuado com sucesso")
                }

                LoginViewModel.AuthStatus.NOLOGGIN -> {
                    // Login falhou, você pode adicionar lógica adicional se necessário
                    _loginStatus.postValue(false)
                    Log.d("Firebase_Login", "Erro no login")
                }
            }
        }


        tourAPI.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    CoroutineScope(Dispatchers.IO).launch {
                        _user.postValue(response.body())
                        _loginStatus.postValue(true)
                        user?.let { db.userDao().insertUser(it) }
                    }

                    Log.d("API_RESPONSE", "Login efetuado com sucesso")
                }else{
                    _loginStatus.value = false
                    Log.d("API_RESPONSE", "Erro no login,${response.message()}, Code: ${response.code()}\"")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _loginStatus.value = false
                Log.e("API_RESPONSE", "Falha na chamada da API para login", t)
            }
        })
    }

    }

    @Composable
    fun LoginPage(navController: NavController) {
        Column {
            LoginScreen(navController)
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
        var ctx = LocalContext.current

        val viewModel: MainViewModel = viewModel()
        var loginStatus: Boolean? by remember { mutableStateOf(null) }

        val innerPadding = 16.dp
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
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

            // Caixa branca com o conteúdo de login
            Box(
                modifier = Modifier
                    .width(850.dp) // Ajusta a largura da caixa
                    .height(700.dp) // Ajusta a altura da caixa
                    .padding(32.dp) // Aumenta o padding interno
                    .background(color = Color.White)  // Cor da caixa branca
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center
                ) {

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
                        text = "Login",
                        modifier = Modifier
                            .padding(8.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp) // Ajusta a altura do campo de texto
                            .padding(vertical = 16.dp, horizontal = 32.dp), // Ajusta o padding
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    TextField(
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
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp) // Ajusta a altura do campo de texto
                            .padding(vertical = 16.dp, horizontal = 32.dp), // Ajusta o padding
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Text(
                        text = "Não tem uma conta?\nCrie uma aqui.",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            navController.navigate("registo")
                        }.padding(8.dp)
                    )

                    // Botão (mais alto e menos comprido, centralizado e embaixo)
                    Button(
                        onClick = {
                            val loginModule = LoginModule(email, password)
                            viewModel.login(loginModule, ctx)
                            loginStatus = viewModel.loginStatus

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp)
                            .background(color = Color.White)
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle, contentDescription = "Login",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
        if (loginStatus == true) {
            navController.navigate("menuprincipal")
        }
        if (loginStatus == false) {
            AlertDialog(
                onDismissRequest = {
                    loginStatus = null
                },
                title = {
                    Text("Erro no login!")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            loginStatus = null
                        },
                    ) {
                        Text("Tente Novamente")
                    }
                },
            )
        }
    }


