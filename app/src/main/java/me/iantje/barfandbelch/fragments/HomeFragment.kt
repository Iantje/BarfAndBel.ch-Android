package me.iantje.barfandbelch.fragments


import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder
import com.bumptech.glide.manager.TargetTracker
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_home.*
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.retrofit.pojos.Quote
import me.iantje.barfandbelch.retrofit.services.BarfAndBelchService
import me.iantje.barfandbelch.widgets.StaticNotification
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

    val notification: StaticNotification = StaticNotification()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayNewQuote()
    }

    fun displayNewQuote() {
        if(retrofit == null) {
            retrofit = BarfAndBelchService.retrofitBuilder()
        }

        val babService = retrofit?.create(BarfAndBelchService::class.java)
        val quote = babService?.getQuote()

        quote?.enqueue(object: Callback<Quote> {
            override fun onFailure(call: Call<Quote>, t: Throwable) {
                Log.e(TAG, "Failed to get quote")
            }

            override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                if(view == null) return

                Log.d(TAG, response.body().toString())

                if(response.body() != null) {
                    homeQuoteText.text = getString(R.string.quote_text_home, response.body()?.quote)
                    homeQuoteCharacter.text = response.body()?.character
                    homeQuoteSource.text = getString(R.string.quote_source_home, response.body()?.source)

                    Glide
                        .with(view!!)
                        .load("https://barfandbel.ch/img/bg/" + response.body()?.bgURL)
                        .apply(RequestOptions()
                            .override(Target.SIZE_ORIGINAL))
                        .into(homeQuoteImage)

                    context?.let {
                        notification.pushNotification(it, response.body()!!)
                    }
                }
            }
        })
    }

}
