package me.iantje.barfandbelch.notifications

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import me.iantje.barfandbelch.localdbs.objects.NotificationFrequency
import me.iantje.barfandbelch.localdbs.objects.NotificationNextTime
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem
import me.iantje.barfandbelch.repository.WidgetRepository
import org.json.JSONObject
import java.util.*

class NotificationScheduler {

    private val tag: String = NotificationScheduler::class.java.simpleName

    companion object {
        const val TIME_DATA = "time"
        const val MINUTE_DATA = "minute"
        const val INTERVAL_DATA = "interval"

        const val ID_EXTRA = "extra_id"
    }

    fun scheduleNotification(notificationId: Long, context: Context) {
        val widgetRepository = WidgetRepository(context)
        val notificationScheduleItem = widgetRepository.getNotificationItem(notificationId)

        if(notificationScheduleItem?.id == null) {
            Log.e(tag, "Notification Schedule Item is null!")
            return
        }

        var notificationNextTime = widgetRepository.getNotificationNextTime(notificationScheduleItem)
        val updatedTime = calculateNextTime(notificationScheduleItem)

        if(notificationNextTime == null) {
            notificationNextTime = NotificationNextTime(notificationScheduleItem.id, updatedTime)
            widgetRepository.addNotificationNextTime(notificationNextTime)
        } else {
            notificationNextTime.nextScheduledTime = updatedTime
            widgetRepository.updateNotificationNextTime(notificationNextTime)
        }

        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationBroadcastReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, NotificationScheduledService.REQUEST_CODE, alarmIntent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, updatedTime, pendingIntent)

        Log.d(tag, "Notification scheduled")
    }

    private fun calculateNextTime(notificationScheduleItem: NotificationScheduleItem): Long {
        val data = JSONObject(notificationScheduleItem.notificationJsonData)
        var calendar = Calendar.getInstance()

        when (notificationScheduleItem.frequency) {
            NotificationFrequency.Hour -> {
                val intervalData = data.getInt(INTERVAL_DATA)
                val minuteData = data.getInt(MINUTE_DATA)

                calendar.add(Calendar.HOUR_OF_DAY, intervalData)
                calendar.set(Calendar.MINUTE, minuteData)
            }
            NotificationFrequency.Day -> {
                val intervalData = data.getInt(INTERVAL_DATA)
                val timeData = data.getInt(TIME_DATA)

                calendar.add(Calendar.DAY_OF_WEEK, intervalData)
                calendar.set(Calendar.HOUR, 0); calendar.set(Calendar.MINUTE, 0)
                calendar.add(Calendar.MILLISECOND, timeData)
            }
            else -> {
                Log.e(tag, "Notification frequency is not set up in scheduler")
                return 0
            }
        }

        return calendar.timeInMillis
    }

}