package estg.ipp.pt.friendly.ui.Navigations

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import estg.ipp.pt.friendly.ui.Database.AppDatabase
import estg.ipp.pt.friendly.ui.Historico.Historico
import estg.ipp.pt.friendly.ui.Login_Registo.LoginPage
import estg.ipp.pt.friendly.ui.Login_Registo.RegistoPage
import estg.ipp.pt.friendly.ui.Login_Registo.RegistoScreen
import estg.ipp.pt.friendly.ui.MenuPrincipal.MenuPrincipal
import estg.ipp.pt.friendly.ui.Pagamento.PagamentosPage
import estg.ipp.pt.friendly.ui.Profile.ProfileMain
import estg.ipp.pt.friendly.ui.RecintosShow.RecintosShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "loginpage"
    ){
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val userCount = runBlocking(Dispatchers.IO) {
        db.userDao().getUserCount()
    }

    val startDestination = if (userCount > 0) {
        "menuprincipal"
    } else {
        "loginpage"
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ){
        composable("loginpage") {
            LoginPage(navController = navController) // Passar o contexto para a LoginPage
        }
        composable("menuprincipal"){
            MenuPrincipal(navController)
        }
        composable("perfil"){
            ProfileMain(navController)
        }
        composable("recintosShow/{recintoID}",
            arguments = listOf(navArgument("recintoID"){type = NavType.IntType})
        ){
            backStackEntry ->
            backStackEntry.arguments?.getInt("recintoID")?.let { RecintosShow(navController, it) }
        }
        composable("reserveHistory"){
            Historico(navController)
        }
        composable("pagamentos/{reservaID}",
            arguments = listOf(navArgument("reservaID"){type = NavType.IntType})
        ){
            backStackEntry ->
            backStackEntry.arguments?.getInt("reservaID")?.let{ PagamentosPage(navController, it)}
        }
        composable("logout"){
            LoginPage(navController)
        }
        composable("registo"){
            RegistoPage(navController)
        }
    }

}