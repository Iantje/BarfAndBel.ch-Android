package me.iantje.barfandbelch.retrofit.pojos

import android.os.Parcel
import android.os.Parcelable

data class Quote(
    val acceptDate: String,
    var bgURL: String,
    val character: String,
    val episode: String,
    val id: String,
    val imgurURL: String,
    val linkedQuoteID: String,
    val quote: String,
    val source: String,
    val submitDate: String,
    val timestamp: String,
    val tumblrURL: String,
    val userID: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(acceptDate)
        writeString(bgURL)
        writeString(character)
        writeString(episode)
        writeString(id)
        writeString(imgurURL)
        writeString(linkedQuoteID)
        writeString(quote)
        writeString(source)
        writeString(submitDate)
        writeString(timestamp)
        writeString(tumblrURL)
        writeString(userID)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Quote> = object : Parcelable.Creator<Quote> {
            override fun createFromParcel(source: Parcel): Quote = Quote(source)
            override fun newArray(size: Int): Array<Quote?> = arrayOfNulls(size)
        }
    }
}