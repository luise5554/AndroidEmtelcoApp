package com.suarez.luis.emtelcoapp.infrastructure.network
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NetworkUtils {

    fun createNetworkChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "network_status_channel",
                "Estado de Red",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifica cuando cambia la conexión a Internet"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNetworkNotification(context: Context, isConnected: Boolean) {
        val message = if (isConnected) {
            "Conectado a Internet ✅"
        } else {
            "Sin conexión ❌"
        }

        val notification = NotificationCompat.Builder(context, "network_status_channel")
            .setSmallIcon(android.R.drawable.stat_notify_sync_noanim)
            .setContentTitle("Estado de la red")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(1001, notification)
    }



}