package me.iantje.barfandbelch.repository

import android.content.Context
import me.iantje.barfandbelch.localdbs.dbs.WidgetsDb
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem

class WidgetRepository(context: Context) {

    private val widgetsDb = WidgetsDb.getInstance(context)

    fun getAllNotificationItems(): List<NotificationScheduleItem> {
        return widgetsDb.WidgetsDao().getAllNotificationItems()
    }

    fun addNotificaationItem(item: NotificationScheduleItem) {
        widgetsDb.WidgetsDao().insertNotificationItem(item)
    }

    fun removeNotificationItem(item: NotificationScheduleItem) {
        widgetsDb.WidgetsDao().deleteNotificationItem(item)
    }

    fun updateNotificationItem(item: NotificationScheduleItem) {
        widgetsDb.WidgetsDao().updateNotificationItem(item)
    }

}