package me.iantje.barfandbelch.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import me.iantje.barfandbelch.localdbs.dbs.LocalQuoteDb
import me.iantje.barfandbelch.retrofit.pojos.Quote
import android.net.ConnectivityManager
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.iantje.barfandbelch.localdbs.objects.LocalQuote
import me.iantje.barfandbelch.retrofit.services.BarfAndBelchService
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors


class QuoteRepository(private val context: Context) {

    companion object {
        val N_PRELOADED_QUOTES: Int = 5
    }

    private var localQuoteDb: LocalQuoteDb = LocalQuoteDb.getInstance(context)

    private var retrofit: Retrofit = BarfAndBelchService.retrofitBuilder()
    private var babService: BarfAndBelchService

    private var refreshBusy: Boolean = false

    private val databaseExecutor = Executors.newSingleThreadExecutor()

    init {
        babService = retrofit.create(BarfAndBelchService::class.java)
    }

    fun getFreshQuote(): Quote? {
        val nFreshQuotes = localQuoteDb.localQuoteDao().getNFreshQuotes()

        nFreshQuotes?.let {
            if(it <= 0) {
                // Get more quotes
                refreshLocalQuotes()

                // TODO: Throw error letting user know there is no quote left
                val randomQuote = localQuoteDb.localQuoteDao().getRandomQuote()
                randomQuote?.jsonString?.let {
                    val gson = Gson()
                    val quote = gson.fromJson<Quote>(it, Quote::class.java)

                    quote.bgURL = "https://barfandbel.ch/img/bg/" + quote.bgURL

                    randomQuote.bgUri?.let {
                        quote.bgURL = it
                    }

                    return quote
                }
            } else {
                // If we still got some, do that
                val freshQuoteDb = localQuoteDb.localQuoteDao().getFreshQuote()
                val jsonString = freshQuoteDb?.jsonString

                jsonString?.let {
                    val gson = Gson()

                    // Check for quotes here, since we want to replace this one
                    setQuoteAsViewed(freshQuoteDb)
                    refreshLocalQuotes()
                    val quote = gson.fromJson<Quote>(it, Quote::class.java)

                    quote.bgURL = "https://barfandbel.ch/img/bg/" + quote.bgURL

                    freshQuoteDb.bgUri?.let {
                        quote.bgURL = it
                    }

                    return quote
                }
            }
        }
        if(nFreshQuotes == null) refreshLocalQuotes()

        return null
    }

    private fun refreshLocalQuotes() {
        if (refreshBusy) return
        refreshBusy = true

        val freshLeft = localQuoteDb.localQuoteDao().getNFreshQuotes() ?: 0

        // If we have less quotes then we'd want preloaded
        if(freshLeft < N_PRELOADED_QUOTES && isInternetConnected()) {
            val nNewQuotes = N_PRELOADED_QUOTES - freshLeft

            // Because the API is a bit dumb (my bad), if there is only one quote requested, it's just one object
            // TODO: Fix the API (V3?)
            if(nNewQuotes == 1) {
                // Single quote getting
                val newQuoteRequest = babService.getQuote()
                return newQuoteRequest.enqueue(object: Callback<Quote> {
                    override fun onFailure(call: Call<Quote>, t: Throwable) {
                        // TODO: Throw on fail (try again?)

                        refreshBusy = false
                    }

                    override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                        response.body()?.let {
                            addToLocal(it)
                        }

                        databaseExecutor.submit {
                            localQuoteDb.localQuoteDao().cleanViewedQuotes()
                        }
                        refreshBusy = false
                    }
                })
            } else {
                // Multiple quote getting
                val newQuotesRequest = babService.getNQuotes(nNewQuotes)
                return newQuotesRequest.enqueue(object: Callback<Array<Quote>> {
                    override fun onFailure(call: Call<Array<Quote>>, t: Throwable) {
                        // TODO: Implement on fail

                        refreshBusy = false
                    }

                    override fun onResponse(call: Call<Array<Quote>>, response: Response<Array<Quote>>) {
                        response.body()?.let {
                            if(!it.isEmpty()) {
                                for(i in 0 until it.count()) {
                                    addToLocal(it[i])
                                }
                            }
                        }

                        databaseExecutor.submit {
                            localQuoteDb.localQuoteDao().cleanViewedQuotes()
                        }
                        refreshBusy = false
                    }

                })
            }
        }

        refreshBusy = false
    }

    private fun addToLocal(quote: Quote) {
        val gson = Gson()
        val jsonString = gson.toJson(quote)

        databaseExecutor.submit {
            val localQuote = LocalQuote(null, jsonString, null)

            val localQuoteId = localQuoteDb.localQuoteDao().insertLocalQuote(localQuote)

            val httpClient = OkHttpClient()
            val httpRequest = Request.Builder()
                .url("https://barfandbel.ch/img/bg/" + quote.bgURL)
                .build()

            httpClient.newCall(httpRequest).enqueue(object: okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    // TODO: Something with error
                    Log.e("Error calling image", e.message)
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.body()?.let {
                        val cacheDir = context.cacheDir
                        val filePath = File(cacheDir, quote.bgURL)

                        if(filePath.exists()) {
                            filePath.delete()
                        }

                        try {
                            val outputStream = FileOutputStream(filePath.path)

                            outputStream.write(it.bytes())
                            outputStream.close()

                            val quoteWithUri = LocalQuote(localQuoteId, localQuote.jsonString, filePath.toURI().path)

                            localQuoteDb.localQuoteDao().updateLocalQuote(quoteWithUri)
                        } catch (e: IOException) {
                            Log.e("Stream went wrong", e.toString())
                        }
                    }
                }

            })
        }
    }

    private fun setQuoteAsViewed(localQuote: LocalQuote?) {
        localQuote?.let {
            it.isViewed = true

            databaseExecutor.submit {
                localQuoteDb.localQuoteDao().updateLocalQuote(it)
            }
        }
    }

    private fun isInternetConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
