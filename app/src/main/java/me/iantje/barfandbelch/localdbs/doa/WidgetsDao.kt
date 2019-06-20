package me.iantje.barfandbelch.localdbs.doa

import androidx.room.*
import me.iantje.barfandbelch.localdbs.objects.NotificationNextTime
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem

@Dao
interface WidgetsDao {

    @Query("SELECT * FROM notification_schedule") fun getAllNotificationItems(): List<NotificationScheduleItem>
    @Query("SELECT * FROM notification_schedule WHERE id = :id") fun getNotificationItem(id: Long): NotificationScheduleItem?

    @Insert fun insertNotificationItem(notification: NotificationScheduleItem): Long
    @Delete fun deleteNotificationItem(notification: NotificationScheduleItem)
    @Update fun updateNotificationItem(notification: NotificationScheduleItem)

    @Query("SELECT * FROM notification_next_time WHERE notification_id = :id") fun getNotificationNextTime(id: Long): NotificationNextTime?

    @Insert fun insertNotificationNextTime(notificationTime: NotificationNextTime)
    @Delete fun deleteNotificationNextTime(notificationTime: NotificationNextTime)
    @Update fun updateNotificationNextTime(notificationTime: NotificationNextTime)
}