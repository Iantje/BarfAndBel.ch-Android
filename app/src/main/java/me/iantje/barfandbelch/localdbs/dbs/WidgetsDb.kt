package me.iantje.barfandbelch.localdbs.dbs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.iantje.barfandbelch.localdbs.doa.WidgetsDao
import me.iantje.barfandbelch.localdbs.objects.NotificationScheduleItem


@Database(entities = [NotificationScheduleItem::class], version = 1, exportSchema = false)
abstract class WidgetsDb: RoomDatabase() {
    abstract fun WidgetsDao(): WidgetsDao

    companion object {
        private val DATABASE_NAME = "widgets_database"

        @Volatile
        private var INSTANCE: WidgetsDb? = null

        fun getInstance(context: Context): WidgetsDb {
            if (INSTANCE == null) {
                synchronized(WidgetsDb::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, WidgetsDb::class.java, DATABASE_NAME)
                            .build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}