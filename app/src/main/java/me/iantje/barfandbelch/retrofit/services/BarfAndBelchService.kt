package me.iantje.barfandbelch.retrofit.services

import me.iantje.barfandbelch.retrofit.pojos.Quote
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BarfAndBelchService {

    companion object {
        fun retrofitBuilder(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://barfandbel.ch/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    @GET("v2/quote.php")
    fun getQuote(): Call<Quote>

    @GET("v2/quote.php?order=id")
    fun getAllQuotes(): Call<Array<Quote>>
}