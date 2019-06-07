package me.iantje.barfandbelch.localdbs.dbs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.iantje.barfandbelch.localdbs.doa.LocalQuoteDao
import me.iantje.barfandbelch.localdbs.objects.LocalQuote

@Database(entities = [LocalQuote::class], version = 1, exportSchema = false)
abstract class LocalQuoteDb: RoomDatabase() {
    abstract fun localQuoteDao(): LocalQuoteDao

    companion object {
        private val DATABASE_NAME = "local_quote_database"

        @Volatile
        private var INSTANCE: LocalQuoteDb? = null

        fun getInstance(context: Context): LocalQuoteDb {
            if (INSTANCE == null) {
                synchronized(LocalQuoteDb::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, LocalQuoteDb::class.java, DATABASE_NAME)
                            .build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}