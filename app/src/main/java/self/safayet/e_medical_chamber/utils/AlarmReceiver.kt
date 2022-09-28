package self.safayet.e_medical_chamber.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import self.safayet.e_medical_chamber.R


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationCompatBuilder =
            NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)

        notificationCompatBuilder
            .setContentTitle("Prescript")
            .setContentText("Medicine time.")
            .setSmallIcon(R.mipmap.ic_launcher).priority = NotificationCompat.PRIORITY_DEFAULT

        notificationManager.notify(666, notificationCompatBuilder.build())

        Log.d("TAG", "onReceive: $notificationCompatBuilder")
//        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
    }
}