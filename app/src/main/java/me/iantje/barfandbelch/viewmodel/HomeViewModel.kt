package me.iantje.barfandbelch.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.iantje.barfandbelch.repository.QuoteRepository
import me.iantje.barfandbelch.retrofit.pojos.Quote
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val quoteRepository: QuoteRepository = QuoteRepository(getApplication())

    val quoteLiveData: MutableLiveData<Quote> = MutableLiveData()

    fun getFreshQuote() {
        GlobalScope.launch {
            var newQuote = quoteRepository.getFreshQuote()

            while(newQuote == null) {
                delay(1000)
                newQuote = quoteRepository.getFreshQuote()
            }

            quoteLiveData.postValue(newQuote)
        }
    }

}