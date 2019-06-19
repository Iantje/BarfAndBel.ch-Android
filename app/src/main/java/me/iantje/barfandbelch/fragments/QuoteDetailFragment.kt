package me.iantje.barfandbelch.fragments

import android.app.WallpaperManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.fragment_detail_quote.*
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.retrofit.pojos.Quote
import java.io.File

class QuoteDetailFragment: Fragment() {

    val STORAGE_PERMISSION_REQUESTCODE: Int = 2902
    lateinit var backgroundImage: Bitmap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_quote, container, false)
    }

    fun fillData(quoteData: Quote) {
        quoteTabletDetailReadyLayout?.visibility = View.GONE
        quoteTabletDetailLoadingLayout?.visibility = View.VISIBLE

        val quoteTabletDetailImage = view?.findViewById<ImageView>(R.id.quoteTabletDetailImage)

        Glide.with(context!!)
            .asBitmap()
            .load("https://barfandbel.ch/img/bg/" + quoteData.bgURL)
            .into(object: CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    // meh
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    quoteTabletDetailLoadingLayout.visibility = View.GONE
                    quoteTabletDetailImage!!.setImageBitmap(resource)

                    backgroundImage = resource
                }

            })

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

        // WallpaperManager only works on API level 23 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val wallpaperManager = WallpaperManager.getInstance(context)
            if(wallpaperManager.isSetWallpaperAllowed && wallpaperManager.isWallpaperSupported) {
                quoteTabletDetailSetWallpaper.visibility = View.VISIBLE

                quoteTabletDetailSetWallpaper.setOnClickListener {
                    fireChangeBackgroundIntent()
                }
            }
        }
    }

    fun fireChangeBackgroundIntent() {
        // Can't set background if we have no context
        context?.let {context ->
            val permissionCheck =
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUESTCODE)

                return
            }

            val uriPath = MediaStore.Images.Media.insertImage(context.contentResolver, backgroundImage,
                "tmpBgFile", "")
            val uri = Uri.parse(uriPath)

            val changeBgIntent = Intent(Intent.ACTION_ATTACH_DATA)
            changeBgIntent.addCategory(Intent.CATEGORY_DEFAULT)
            changeBgIntent.setDataAndType(uri, "image/*")
            changeBgIntent.putExtra("mimeType", "image/*")
            startActivity(changeBgIntent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_REQUESTCODE) {
            context?.let {context ->
                val permissionCheck =
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    fireChangeBackgroundIntent()
                } else {
                    Toast.makeText(context, R.string.storage_permission_denied, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
