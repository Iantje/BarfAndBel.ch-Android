package me.iantje.barfandbelch.localdbs.objects

import androidx.room.*

enum class NotificationType {
    FullQuote,
    ImageQuote,
    CompactQuote
}

class NotificationTypeConverter {
    @TypeConverter
    fun getType(numeral: Int): NotificationType {
        return NotificationType.values()[numeral]
    }

    @TypeConverter
    fun getTypeInt(type: NotificationType): Int {
        return type.ordinal
    }
}

enum class NotificationFrequency {
    Hour,
    Day,
    Week,
    Month,
    Year,
    Century
}

class NotificationFrequencyConverter {
    @TypeConverter
    fun getType(numeral: Int): NotificationFrequency {
        return NotificationFrequency.values()[numeral]
    }

    @TypeConverter
    fun getTypeInt(type: NotificationFrequency): Int {
        return type.ordinal
    }
}

@Entity(tableName = "notification_schedule")
@TypeConverters(NotificationFrequencyConverter::class, NotificationTypeConverter::class)
data class NotificationScheduleItem (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "type")
    val type: NotificationType,

    @ColumnInfo(name = "frequency")
    val frequency: NotificationFrequency,

    @ColumnInfo(name = "notification_data")
    val notificationJsonData: String,

    @ColumnInfo(name = "enabled")
    var enabled: Boolean
)