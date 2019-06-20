package me.iantje.barfandbelch.repository

import android.app.Notification
import android.content.Context
import me.iantje.barfandbelch.localdbs.dbs.WidgetsDb
import me.iantje.barfandbelch.localdbs.objects.NotificationFrequency
import me.iantje.barfandbelch.localdbs.objects.NotificationNextTime
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem
import me.iantje.barfandbelch.localdbs.objects.NotificationType
import me.iantje.barfandbelch.notifications.NotificationScheduler

class WidgetRepository(context: Context) {

    private val widgetsDb = WidgetsDb.getInstance(context)
    private val notificationScheduler: NotificationScheduler = NotificationScheduler()

    fun getAllNotificationItems(): List<NotificationScheduleItem> {
        return widgetsDb.WidgetsDao().getAllNotificationItems()
    }

    fun getNotificationItem(id: Long): NotificationScheduleItem? {
        return widgetsDb.WidgetsDao().getNotificationItem(id)
    }

    fun addNotificationItem(item: NotificationScheduleItem): Long {
        return widgetsDb.WidgetsDao().insertNotificationItem(item)
    }

    fun addNotificationItem(frequency: NotificationFrequency, jsonData: String) {
        val item = NotificationScheduleItem(null, NotificationType.FullQuote, frequency, jsonData, true)

        addNotificationItem(item)
    }

    fun removeNotificationItem(item: NotificationScheduleItem) {
        widgetsDb.WidgetsDao().deleteNotificationItem(item)
    }

    fun updateNotificationItem(item: NotificationScheduleItem) {
        widgetsDb.WidgetsDao().updateNotificationItem(item)
    }

    fun getNotificationNextTime(item: NotificationScheduleItem): NotificationNextTime? {
        item.id?.let {
            return widgetsDb.WidgetsDao().getNotificationNextTime(it)
        }

        return null
    }

    fun addNotificationNextTime(item: NotificationNextTime) {
        widgetsDb.WidgetsDao().insertNotificationNextTime(item)
    }

    fun updateNotificationNextTime(item: NotificationNextTime) {
        widgetsDb.WidgetsDao().updateNotificationNextTime(item)
    }

}