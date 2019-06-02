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
class AllQuotesFragment : Fragment() {

    private val TAG: String = AllQuotesFragment::class.java.simpleName

    private var loadedQuotes: Array<Quote>? = null
    private var retrofit: Retrofit? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_quotes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadQuotes()
    }

    fun loadQuotes() {
        if(loadedQuotes == null) {
            if(retrofit == null) {
                retrofit = BarfAndBelchService.retrofitBuilder()
            }

            val babService = retrofit?.create(BarfAndBelchService::class.java)
            val quotesRequest = babService?.getAllQuotes()

            quotesRequest?.enqueue(object: Callback<Array<Quote>> {
                override fun onFailure(call: Call<Array<Quote>>, t: Throwable) {
                    Log.e(TAG, "Failed to get quotes")
                }

                override fun onResponse(call: Call<Array<Quote>>, response: Response<Array<Quote>>) {
                    Log.d(TAG, response.body()!![1].toString())

                    loadedQuotes = response.body()

                    if(response.body() != null) {
                        displayAllQuotes(response.body()!!)
                    }
                }
            })

            return
        } else if (loadedQuotes != null) {
            displayAllQuotes(loadedQuotes!!)
        }
    }

    fun displayAllQuotes(quotes: Array<Quote>) {

    }

}
