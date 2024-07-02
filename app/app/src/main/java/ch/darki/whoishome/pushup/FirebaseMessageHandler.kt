package ch.darki.whoishome.pushup

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessageHandler : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("MESSAGE", message.data.toString())

        val title = message.notification?.title
        val text = message.notification?.body

        if(message.notification == null){
            throw IllegalArgumentException("Notification must have a value")
        }

        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "Heads Up Notification",
                NotificationManager.IMPORTANCE_HIGH
            )

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(1, notification.build())
        }
        else {
            Log.i("MESSAGE", "No Notification Permissions")
        }
    }

    override fun onNewToken(token: String) {
        Log.d("MESSAGING", "Refreshed token: $token")
    }

    companion object{
        const val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
    }
}