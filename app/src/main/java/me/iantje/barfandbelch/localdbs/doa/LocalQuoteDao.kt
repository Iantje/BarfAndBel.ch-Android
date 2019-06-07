package me.iantje.barfandbelch.localdbs.doa

import androidx.lifecycle.LiveData
import androidx.room.*
import me.iantje.barfandbelch.localdbs.objects.LocalQuote

@Dao
interface LocalQuoteDao {
    @Query("SELECT * FROM local_quotes WHERE isViewed = 0 LIMIT 1") fun getFreshQuote(): LocalQuote
    @Query("SELECT * FROM local_quotes WHERE isViewed = 1") fun getAllViewedQuotes(): List<LocalQuote>

    @Query("SELECT * FROM local_quotes ORDER BY random() LIMIT 1") fun getRandomQuote(): LocalQuote

    @Query("SELECT count(isViewed) FROM local_quotes WHERE isViewed = 0") fun getNFreshQuotes(): Int
    @Query("SELECT count(isViewed) FROM local_quotes WHERE isViewed = 1") fun getNViewedQuotes(): Int

    @Query("DELETE FROM local_quotes WHERE isViewed") fun cleanViewedQuotes()

    @Insert fun insertLocalQuote(localQuote: LocalQuote): Long
    @Insert fun insertLocalQuotes(localQuote: List<LocalQuote>)
    @Delete fun deleteLocalQuote(localQuote: LocalQuote)
    @Delete fun deleteLocalQuotes(localQuotes: List<LocalQuote>)

    @Update fun updateLocalQuote(localQuote: LocalQuote)
}