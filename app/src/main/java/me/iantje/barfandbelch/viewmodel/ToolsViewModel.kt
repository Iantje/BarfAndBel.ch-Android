package me.iantje.barfandbelch.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem
import me.iantje.barfandbelch.notifications.NotificationScheduler
import me.iantje.barfandbelch.repository.WidgetRepository

class ToolsViewModel(private val app: Application): AndroidViewModel(app) {

    private val widgetRepository = WidgetRepository(app.applicationContext)

    val notificationItems = MutableLiveData<List<NotificationScheduleItem>>()

    fun refreshNotificationItems() {
        GlobalScope.launch {
            notificationItems.postValue(widgetRepository.getAllNotificationItems())
        }
    }

    fun addNotificationItem(item: NotificationScheduleItem) {
        GlobalScope.launch {
            val newItemId = widgetRepository.addNotificationItem(item)

            NotificationScheduler().scheduleNotification(newItemId, app.applicationContext)

            refreshNotificationItems()
        }
    }

    fun removeNotificationItem(item: NotificationScheduleItem) {
        GlobalScope.launch {
            widgetRepository.removeNotificationItem(item)

            refreshNotificationItems()
        }
    }

    fun updateNotificationItem(item: NotificationScheduleItem) {
        GlobalScope.launch {
            widgetRepository.updateNotificationItem(item)

            refreshNotificationItems()
        }
    }

}