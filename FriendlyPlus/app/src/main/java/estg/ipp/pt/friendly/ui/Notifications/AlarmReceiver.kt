package estg.ipp.pt.friendly.ui.Notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "Notificação" && context != null) {
            // Aqui você pode realizar as ações desejadas ao receber o alarme
            Toast.makeText(context, "Alarme disparado!", Toast.LENGTH_SHORT).show()
        }
    }
}