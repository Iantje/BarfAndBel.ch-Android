package me.iantje.barfandbelch.localdbs.doa

import androidx.room.*
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem

@Dao
interface WidgetsDao {

    @Query("SELECT * FROM notification_schedule") fun getAllNotificationItems(): List<NotificationScheduleItem>

    @Insert fun insertNotificationItem(notification: NotificationScheduleItem)
    @Delete fun deleteNotificationItem(notification: NotificationScheduleItem)
    @Update fun updateNotificationItem(notification: NotificationScheduleItem)
}