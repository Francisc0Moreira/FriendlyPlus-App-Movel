package estg.ipp.pt.friendly.ui.Notifications

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import estg.ipp.pt.friendly.R
import estg.ipp.pt.friendly.ui.Database.Reserva
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun showNotification(context: Context, title: String, content: String, triggerTimeMillis: Long) {
    val channelId = "default_channel_id"
    val notificationId = 1

    createNotificationChannel(context, channelId)

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.bola) // Substitua pelo ícone desejado
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(notificationId, builder.build())
    }

    // Agendar a notificação para o tempo especificado
    scheduleExactAlarm(context, triggerTimeMillis)
}

private fun createNotificationChannel(context: Context, channelId: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Default Notification Channel"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}
@RequiresApi(Build.VERSION_CODES.S)
fun scheduleExactAlarm(context: Context, triggerTimeMillis: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                alarmIntent
            )
        }
    } else {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            alarmIntent
        )
    }
}