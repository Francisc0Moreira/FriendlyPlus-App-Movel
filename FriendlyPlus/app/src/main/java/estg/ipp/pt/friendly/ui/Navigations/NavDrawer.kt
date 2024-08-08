package estg.ipp.pt.friendly.ui.Navigations

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Firebase.LoginViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNavigationDrawer(navController: NavController, title: String, screen: String) {
    val scope = rememberCoroutineScope()
    var ctx = LocalContext.current
    val db = AppDatabase.getDatabase(ctx)
    val application = LocalContext.current.applicationContext as Application
    val selectedItem = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )

                }
                NavigationDrawerItem(
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,

                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Icon"
                            )
                            Spacer(modifier = Modifier.width(16.dp)) // Espaçamento entre o ícone e o texto
                            Text(text = "Menu")
                        }
                    },
                    selected = selectedItem.value,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("menuprincipal")
                        }

                    }
                )
                NavigationDrawerItem(
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,

                        ) {
                            Icon(
                                imageVector = Icons.Filled.List,
                                contentDescription = "Icon"
                            )
                            Spacer(modifier = Modifier.width(16.dp)) // Espaçamento entre o ícone e o texto
                            Text(text = "Histórico de Reservas")
                        }
                    },
                    selected = selectedItem.value,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("reserveHistory")
                        }

                    }
                )
                NavigationDrawerItem(
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ExitToApp,
                                contentDescription = "Icon"
                            )
                            Spacer(modifier = Modifier.width(16.dp)) // Espaçamento entre o ícone e o texto
                            Text(text = "Logout")
                        }
                    },
                    selected = selectedItem.value,
                    onClick = {
                        scope.launch {
                            db.userDao().deleteAllUsers()
                            LoginViewModel(application).logout()
                            drawerState.close()
                            navController.navigate("logout")
                        }

                    }
                )
            }
        },
        content = {
            ScaffoldNew(navController,drawerState,title,screen)
        }
    )
}

