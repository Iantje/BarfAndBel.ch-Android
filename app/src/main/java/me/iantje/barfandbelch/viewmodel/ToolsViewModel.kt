package me.iantje.barfandbelch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem
import me.iantje.barfandbelch.repository.WidgetRepository

class ToolsViewModel(application: Application): AndroidViewModel(application) {

    val widgetRepository = WidgetRepository(application.applicationContext)

    val notificationItems = MutableLiveData<List<NotificationScheduleItem>>()

    fun refreshNotificationItems() {
        GlobalScope.launch {
            notificationItems.postValue(widgetRepository.getAllNotificationItems())
        }
    }

    fun addNotificationItem(item: NotificationScheduleItem) {
        GlobalScope.launch {
            widgetRepository.addNotificaationItem(item)

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