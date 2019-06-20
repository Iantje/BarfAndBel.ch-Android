package me.iantje.barfandbelch.localdbs.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_next_time")
class NotificationNextTime (

    @PrimaryKey(autoGenerate = false)
    val notification_id: Long,

    @ColumnInfo(name = "next_scheduled_time")
    var nextScheduledTime: Long
)