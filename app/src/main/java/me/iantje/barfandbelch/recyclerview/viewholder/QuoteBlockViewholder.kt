package me.iantje.barfandbelch.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.viewholder_quote_block.view.*
import me.iantje.barfandbelch.retrofit.pojos.Quote

class QuoteBlockViewholder(private val view: View): RecyclerView.ViewHolder(view) {

    fun fillBlock(quote: Quote) {
        view.quoteBlockText.text = quote.quote
        view.quoteBlockCharacter.text = quote.character
        view.quoteBlockSource.text = quote.source
    }
}
