package me.iantje.barfandbelch.localdbs.doa

import android.arch.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.iantje.barfandbelch.localdbs.objects.LocalQuote

@Dao
interface LocalQuoteDao {
    @Query("SELECT * FROM local_quotes WHERE NOT isViewed LIMIT 1") fun getUnviewedQuote(): LocalQuote
    @Query("SELECT * FROM local_quotes WHERE isViewed") fun getAllViewedQuotes(): LiveData<List<LocalQuote>>

    @Insert fun insertLocalQuote(localQuote: LocalQuote)
    @Insert fun insertLocalQuotes(localQuote: List<LocalQuote>)
    @Delete fun deleteLocalQuote(localQuote: LocalQuote)
    @Delete fun deleteLocalQuotes(localQuotes: List<LocalQuote>)
}