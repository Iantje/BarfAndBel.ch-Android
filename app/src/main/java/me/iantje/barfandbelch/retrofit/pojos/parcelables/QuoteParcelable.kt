package me.iantje.barfandbelch.retrofit.pojos.parcelables

import android.os.Parcel
import android.os.Parcelable

class QuoteParcelable(
    val acceptDate: String?,
    val bgURL: String?,
    val character: String?,
    val episode: String?,
    val id: String?,
    val imgurURL: String?,
    val linkedQuoteID: String?,
    val quote: String?,
    val source: String?,
    val submitDate: String?,
    val timestamp: String?,
    val tumblrURL: String?,
    val userID: String?
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
        val CREATOR: Parcelable.Creator<QuoteParcelable> = object : Parcelable.Creator<QuoteParcelable> {
            override fun createFromParcel(source: Parcel): QuoteParcelable = QuoteParcelable(source)
            override fun newArray(size: Int): Array<QuoteParcelable?> = arrayOfNulls(size)
        }
    }
}