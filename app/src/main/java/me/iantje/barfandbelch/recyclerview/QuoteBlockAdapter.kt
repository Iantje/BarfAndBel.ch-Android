package me.iantje.barfandbelch.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.iantje.barfandbelch.R
import me.iantje.barfandbelch.recyclerview.viewholder.QuoteBlockViewholder
import me.iantje.barfandbelch.retrofit.pojos.Quote

class QuoteBlockAdapter(var quotes: List<Quote>): RecyclerView.Adapter<QuoteBlockViewholder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): QuoteBlockViewholder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.viewholder_quote_block, viewGroup, false)

        return QuoteBlockViewholder(view)
    }

    override fun getItemCount(): Int {
        return quotes.count()
    }

    override fun onBindViewHolder(view: QuoteBlockViewholder, position: Int) {
        view.fillBlock(quotes[position])
    }

    fun swapQuotesList(newQuotes: ArrayList<Quote>) {
        quotes = newQuotes

        notifyDataSetChanged()
    }
}