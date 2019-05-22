package me.iantje.barfandbelch.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.retrofit.pojos.Quote
import me.iantje.barfandbelch.retrofit.services.BarfAndBelchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    private var retrofit: Retrofit? = null
    private val TAG = HomeFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    fun displayNewQuote() {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://barfandbel.ch/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val babService = retrofit?.create(BarfAndBelchService::class.java)
        val quote = babService?.getQuote()

        quote?.enqueue(object: Callback<Quote> {
            override fun onFailure(call: Call<Quote>, t: Throwable) {
                Log.e(TAG, "Failed to get quote")
            }

            override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                // TODO: Replace viewbinding
            }

        })
    }

}
