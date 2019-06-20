package me.iantje.barfandbelch.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null || intent == null) {
            return
        }

        val scheduleId = intent.getLongExtra(NotificationScheduler.ID_EXTRA, -1)
        if(scheduleId.equals(-1)) {
            return
        }

        // Schedule the next notification
        val notificationScheduler = NotificationScheduler()
        notificationScheduler.scheduleNotification(scheduleId, context)

        // Fire up the service
        val service = Intent(context, NotificationScheduledService::class.java)
        context.startService(service)
    }
}