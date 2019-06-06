package me.iantje.barfandbelch.widgets

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import me.iantje.barfandbelch.MainActivity
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.retrofit.pojos.Quote

class StaticNotification {

    companion object {
        val ONGOING_QUOTE_NOTIFICATION_ID: Int = 2902
        val QUOTE_NOTIFICATION_CHANNEL_ID: String = "BaBQuotes"
        val QUOTE_NOTIFICATION_CHANNEL_NAME: String = "BarfAndBel.ch Quotes"
        val QUOTE_NOTIFICATION_CHANNEL_DSC: String = "Notifications to deliver dragon quotes."
    }

    fun registerNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(QUOTE_NOTIFICATION_CHANNEL_ID, QUOTE_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = QUOTE_NOTIFICATION_CHANNEL_DSC
            }

            val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifManager.createNotificationChannel(channel)
        }
    }

    fun pushNotification(context: Context, quote: Quote) {
        registerNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra("quote", quote)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(context, QUOTE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.thisisberk)
            .setContentTitle("Quote by " + quote.character)
            .setContentText("Unfold to view quote")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(quote.quote))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(ONGOING_QUOTE_NOTIFICATION_ID, notificationBuilder.build())
        }
    }

}