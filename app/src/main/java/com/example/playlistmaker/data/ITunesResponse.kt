package com.example.playlistmaker.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ITunesResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val tracks: List<TracksFields>
)

data class TracksFields(
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Int,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String,

    @SerializedName("collectionName")
    val collectionName: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String,
    @SerializedName("country")
    val country: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(trackName)
        dest.writeString(artistName)
        dest.writeInt(trackTimeMillis)
        dest.writeString(artworkUrl100)
        dest.writeString(collectionName)
        dest.writeString(releaseDate)
        dest.writeString(primaryGenreName)
        dest.writeString(country)
    }

    companion object CREATOR : Parcelable.Creator<TracksFields> {
        override fun createFromParcel(parcel: Parcel): TracksFields {
            return TracksFields(parcel)
        }

        override fun newArray(size: Int): Array<TracksFields?> {
            return arrayOfNulls(size)
        }
    }

    //для замены конца ссылки обложки
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}
