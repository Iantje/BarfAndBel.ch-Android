package me.iantje.barfandbelch.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.iantje.barfandbelch.MainActivity
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.repository.QuoteRepository

class NotificationScheduledService : IntentService("NotificationScheduledService") {

    companion object {
        const val NOTIFICATION_ID = 1412
        const val REQUEST_CODE = 1412

        const val CHANNEL_ID = "je.iant.barfandbelch.schedulednotifications"
        const val CHANNEL_NAME = "Scheduled Notifications"
        val NOTIFICATION_IMPORTANCE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager.IMPORTANCE_HIGH
        } else {
            0
        }
    }

    private lateinit var quoteRepository: QuoteRepository

    // Make sure that any Android device under API level 26 can't run this
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        // Set a notification channel and its properties
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NOTIFICATION_IMPORTANCE)
        notificationChannel.enableVibration(false)
        notificationChannel.setShowBadge(true)
        notificationChannel.description = getString(R.string.scheduled_notification_description)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onHandleIntent(intent: Intent?) {
        val context = applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // From API level 26 and on notifications need a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        quoteRepository = QuoteRepository(context)
        val quote = quoteRepository.getFreshQuote()

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        notificationIntent.putExtra("quote", quote)

        val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, notificationIntent, 0)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.thisisberk)
            .setContentTitle(getString(R.string.scheduled_notification_title, quote?.character))
            .setContentText(getString(R.string.scheduled_notification_unfold))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(quote?.quote))
            .setContentIntent(pendingIntent)
            .setPriority(NOTIFICATION_IMPORTANCE)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification)
        }
    }
}