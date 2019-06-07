package me.iantje.barfandbelch.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_all_quotes.*
import kotlinx.android.synthetic.main.popup_quote.view.*
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.recyclerview.QuoteBlockAdapter
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
class AllQuotesFragment : androidx.fragment.app.Fragment(), androidx.recyclerview.widget.RecyclerView.OnItemTouchListener {

    private val TAG: String = AllQuotesFragment::class.java.simpleName

    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var recyclerAdapter: QuoteBlockAdapter

    private lateinit var gestureDetector: GestureDetector

    private var container: ViewGroup? = null

    private var loadedQuotes: ArrayList<Quote> = ArrayList()
    private var retrofit: Retrofit? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_quotes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = allQuotesRecycler
        recyclerAdapter = QuoteBlockAdapter(loadedQuotes)

        recycler.adapter = recyclerAdapter
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )

        super.onViewCreated(view, savedInstanceState)

        loadQuotes()
        createRecyclerTouchListener()
    }

    fun loadQuotes() {
        if(loadedQuotes.count() < 1) {
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
                    Log.d(TAG, response.body()!![345].toString())

                    loadedQuotes = response.body()?.toList() as ArrayList<Quote>

                    showLoadedQuotes()
                }
            })

            return
        }

        showLoadedQuotes()
    }

    fun showLoadedQuotes() {
        recyclerAdapter.swapQuotesList(loadedQuotes)
        recyclerAdapter.notifyDataSetChanged()
    }

    fun createRecyclerTouchListener() {
        gestureDetector = GestureDetector(context, object: GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                e?.let {
                    val child = recycler.findChildViewUnder(it.x, it.y)

                    child?.let {mutableChild ->
                        val childPos = recycler.getChildAdapterPosition(mutableChild)
                        val childData = recyclerAdapter.quotes[childPos]

                        val popupView = View.inflate(context, R.layout.popup_quote, null)

                        val quotePopup = PopupWindow(popupView, androidx.viewpager.widget.ViewPager.LayoutParams.MATCH_PARENT,
                            androidx.viewpager.widget.ViewPager.LayoutParams.WRAP_CONTENT, true)

                        quotePopup.animationStyle = R.style.QuotePopupAnimation
                        quotePopup.isFocusable = true

                        Glide.with(quotePopup.contentView)
                            .load("https://barfandbel.ch/img/bg/" + childData.bgURL)
                            .into(quotePopup.contentView.quotePopupImage)
                        quotePopup.contentView.quotePopupText.text = childData.quote

                        quotePopup.contentView.quotePopupCharacter.text = childData.character
                        quotePopup.contentView.quotePopupEpisode.text = if(childData.episode.isEmpty())
                            getString(R.string.quote_popup_no_episode) else childData.episode
                        quotePopup.contentView.quotePopupSource.text = childData.source
                        quotePopup.contentView.quotePopupSubmitor.text = "Nothing cus the API is shiiiiiit"

                        quotePopup.showAtLocation(container, Gravity.BOTTOM, 0, 0)
                    }
                }

                return super.onSingleTapUp(e)
            }
        })

        recycler.addOnItemTouchListener(this)
    }

    override fun onTouchEvent(p0: androidx.recyclerview.widget.RecyclerView, p1: MotionEvent) {

    }

    override fun onInterceptTouchEvent(p0: androidx.recyclerview.widget.RecyclerView, p1: MotionEvent): Boolean {
        // Do touch event
        gestureDetector.onTouchEvent(p1)

        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(p0: Boolean) {

    }

}
