package me.iantje.barfandbelch.localdbs.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_quotes")
object LocalQuote {
    @PrimaryKey(autoGenerate = true)
     val id: Int? = null

    @ColumnInfo(name = "jsonString")
     val jsonString: String? = null

    @ColumnInfo(name = "bgUri")
     val bgUri: String? = null

    @ColumnInfo(name = "isViewed")
     val isViewed: Boolean = false
}
