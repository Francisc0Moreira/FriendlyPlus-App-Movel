package estg.ipp.pt.friendly.ui.Navigations

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Historico.HistoricoScreen
import estg.ipp.pt.friendly.ui.MenuPrincipal.MenuScreen
import estg.ipp.pt.friendly.ui.Profile.ProfileContent
import estg.ipp.pt.friendly.ui.RecintosShow.RecintosShowScreen
import kotlinx.coroutines.launch
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldNew(
    navController: NavController,
    drawerState: DrawerState,
    title: String,
    screen: String
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Image(painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Menu",
                            modifier = Modifier.fillMaxSize())
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("perfil") }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {},
        content = {
            // Condição para escolher a tela com base no título
            if (screen == "menu") {
                MenuScreen(navController)
            } else if (screen == "perfil") {
                ProfileContent(navController)
            } else if (screen == "recinto"){
                RecintosShowScreen(navController)
            }else if(screen =="reserveHistory"){
                HistoricoScreen(navController)
            }
        },
    )
}
