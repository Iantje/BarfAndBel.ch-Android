package me.iantje.barfandbelch.fragments


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder
import com.bumptech.glide.manager.TargetTracker
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_home.*
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.retrofit.pojos.Quote
import me.iantje.barfandbelch.retrofit.services.BarfAndBelchService
import me.iantje.barfandbelch.utils.glide.GlideImageLoader
import me.iantje.barfandbelch.viewmodel.HomeViewModel
import me.iantje.barfandbelch.widgets.StaticNotification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : androidx.fragment.app.Fragment() {

    private var retrofit: Retrofit? = null
    private val TAG = HomeFragment::class.java.simpleName

    val notification: StaticNotification = StaticNotification()

    private lateinit var viewmodel: HomeViewModel
    private lateinit var quoteLiveData: LiveData<Quote>

    private val newQuoteObserver: Observer<Quote> = object: Observer<Quote> {
        override fun onChanged(newQuote: Quote?) {
            if(view == null) return

            newQuote?.let {
                homeQuoteText.text = getString(R.string.quote_text_home, it.quote)
                homeQuoteCharacter.text = it.character
                homeQuoteSource.text = getString(R.string.quote_source_home, it.source)

                Glide
                    .with(view!!)
                    .load(it.bgURL)
                    .apply(RequestOptions()
                        .override(Target.SIZE_ORIGINAL))
                    .addListener(object:RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Show the quote when image is loaded
                            homeLoadContainer.visibility = View.GONE
                            homeQuoteContainer.visibility = View.VISIBLE

                            return false
                        }
                    })
                    .into(homeQuoteImage)

                context?.let {immutableContext ->
                    notification.pushNotification(immutableContext, it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        quoteLiveData = viewmodel.quoteLiveData

        setupObserver()
    }

    private fun setupObserver() {
        quoteLiveData.observe(this, newQuoteObserver)

        viewmodel.getFreshQuote()
    }

    fun showNewQuote() {
        view?.let {
            viewmodel.getFreshQuote()
        }
    }

}
