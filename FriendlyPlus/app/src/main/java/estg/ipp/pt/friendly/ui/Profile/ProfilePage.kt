package estg.ipp.pt.friendly.ui.Profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Database.User
import estg.ipp.pt.friendly.ui.Entity.DataAPI
import estg.ipp.pt.friendly.ui.Entity.RetrofitHelper
import estg.ipp.pt.friendly.ui.Navigations.MyNavigationDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun ProfileMain(navController: NavController){
    Column {
        MyNavigationDrawer(navController,"Perfil", "perfil")    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileField(
    label: String,
    text: String,
    onValueChanged: (String) -> Unit,
    onSaveClicked: () -> Unit
) {
    var enabled by remember { mutableStateOf(false) }
    var fieldValue by remember { mutableStateOf(text) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(text = label, fontWeight = FontWeight.Bold)
            if (enabled) {
                TextField(
                    value = fieldValue,
                    onValueChange = {
                        fieldValue = it
                        onValueChanged(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = fieldValue,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        IconButton(
            onClick = { enabled = !enabled },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    if (enabled) {
        TextButton(
            onClick = {
                enabled = false // Disable editing mode
                onSaveClicked() // Call the save changes function
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Guardar Alterações")
        }
    }
}

fun saveChanges(user: User, db: AppDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        // Atualiza o utilizador na base de dados local (Room)
        db.userDao().updateUser(user)

        // Atualiza o utilizador na API Retrofit
        try {
            val retrofit = RetrofitHelper.getInstance().create(DataAPI::class.java)
            val tourAPI = user.userID?.let { retrofit.updateUserAPI(user, it) }
            val response = tourAPI?.execute()

            if (response != null) {
                if (response.isSuccessful) {
                    // Atualização bem-sucedida na API
                    Log.d("API_RESPONSE", "Usuário atualizado na API")
                } else {
                    // Falha na atualização na API
                    val errorBody = response.errorBody()?.string()
                    Log.d("API_RESPONSE", "Falha na atualização na API. Código: ${response.code()}, Mensagem: ${response.message()}, Corpo do Erro: $errorBody")
                }
            }
        } catch (e: Exception) {
            // Exceção ao fazer a chamada à API
            Log.e("API_RESPONSE", "Erro ao fazer a chamada à API", e)
        }
    }
}

@Composable
fun ProfileContent(navController: NavController) {
    var ctx = LocalContext.current
    val db = AppDatabase.getDatabase(ctx)

    // Utilizando um estado para o usuário
    var user by remember { mutableStateOf<User?>(null) }

    // Utilizando um estado para controlar se os dados estão sendo carregados
    var isLoading by remember { mutableStateOf(true) }

    // Usando lifecycleScope para lançar a corrotina
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Dentro da corrotina, pode-se atualizar o estado do usuário e isLoading
        coroutineScope.launch(Dispatchers.IO) {
            user = db.userDao().getAnyUser()
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .padding(top = 60.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.boneco),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (user != null) {
            ProfileField(
                label = "Nome",
                text = user!!.nome,
                onValueChanged = { user!!.nome = it },
                onSaveClicked = { coroutineScope.launch { saveChanges(user!!, db) } }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (user != null) {
            ProfileField(
                label = "Email",
                text = user!!.email,
                onValueChanged = { user!!.email = it },
                onSaveClicked = {  saveChanges(user!!, db) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (user != null) {
            ProfileField(
                label = "Genero",
                text = user!!.genero,
                onValueChanged = { user!!.genero = it },
                onSaveClicked = {  saveChanges(user!!, db)}
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (user != null) {
            ProfileField(
                label = "NIF",
                text = user!!.nif,
                onValueChanged = { user!!.nif = it },
                onSaveClicked = {  saveChanges(user!!, db) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (user != null) {
            ProfileField(
                label = "Morada",
                text = user!!.morada,
                onValueChanged = { user!!.morada = it },
                onSaveClicked = {  saveChanges(user!!, db)}
            )
        }

        if (user != null) {
            ProfileField(
                label = "Nascimento",
                text = user!!.nascimento,
                onValueChanged = { user!!.nascimento = it },
                onSaveClicked = {  saveChanges(user!!, db) }
            )
        }

        if (user != null) {
            ProfileField(
                label = "Telemóvel",
                text = user!!.telemovel,
                onValueChanged = { user!!.telemovel = it },
                onSaveClicked = { saveChanges(user!!, db) }
            )
        }

        if (user != null) {
            Text(
                text = buildAnnotatedString {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("Pontos:")
                    pop()
                    append(" ${user!!.pontos}")
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
}


