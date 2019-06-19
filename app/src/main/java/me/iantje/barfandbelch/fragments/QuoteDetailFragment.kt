package me.iantje.barfandbelch.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_detail_quote.*
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.retrofit.pojos.Quote

class QuoteDetailFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_quote, container, false)
    }

    fun fillData(quoteData: Quote) {
        quoteTabletDetailReadyLayout?.visibility = View.GONE
        quoteTabletDetailLoadingLayout?.visibility = View.VISIBLE

        quoteTabletDetailImage?.let {
            Glide.with(context!!)
                .load("https://barfandbel.ch/img/bg/" + quoteData.bgURL)
                .addListener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // TODO: Fail message
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        quoteTabletDetailLoadingLayout.visibility = View.GONE

                        return false
                    }

                })
                .into(it)
        }

        quoteTabletDetailText?.text = getString(R.string.quote_text_home, quoteData.quote)
        quoteTabletDetailCharacter?.text = quoteData.character
        quoteTabletDetailSource?.text = quoteData.source

        quoteTabletDetailEpisode?.text = getString(R.string.quote_popup_no_episode)
        quoteData.episode?.let {
            if(it.isNotEmpty()) {
                quoteTabletDetailEpisode?.text = it
            }
        }

        quoteTabletDetailSubmitor?.text = "uhu"
    }
}