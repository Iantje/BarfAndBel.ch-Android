package me.iantje.barfandbelch.localdbs.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_quotes")
data class LocalQuote (
    @PrimaryKey(autoGenerate = true)
     val id: Long? = null,

    @ColumnInfo(name = "jsonString")
     val jsonString: String? = null,

    @ColumnInfo(name = "bgUri")
     var bgUri: String? = null,

    @ColumnInfo(name = "isViewed")
     var isViewed: Boolean = false
)
