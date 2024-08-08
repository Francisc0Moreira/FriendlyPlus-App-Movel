package estg.ipp.pt.friendly

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import estg.ipp.pt.friendly.ui.Firebase.LoginViewModel
import estg.ipp.pt.friendly.ui.Login_Registo.MainViewModel
import estg.ipp.pt.friendly.ui.Login_Registo.RegistoViewModel
import estg.ipp.pt.friendly.ui.Navigations.MyAppNavigationHost
import estg.ipp.pt.friendly.ui.theme.FriendlyTheme

class MainActivity : ComponentActivity() {

    var wifiConnection = false

    val networkWifiRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    val networkCallback = object : ConnectivityManager.NetworkCallback(){

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            wifiConnection = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            wifiConnection = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d("DEUS_APP", "onCreate()")

        setContent {
            FriendlyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAppNavigationHost()

                }
            }
        }
    }

}
