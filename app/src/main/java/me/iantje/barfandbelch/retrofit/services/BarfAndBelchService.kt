package me.iantje.barfandbelch.retrofit.services

import me.iantje.barfandbelch.retrofit.pojos.Quote
import retrofit2.Call
import retrofit2.http.GET

interface BarfAndBelchService {
    @GET("v2/quote.php")
    fun getQuote(): Call<Quote>
}