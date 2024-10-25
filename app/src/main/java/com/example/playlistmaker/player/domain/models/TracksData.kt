package com.example.playlistmaker.player.domain.models

import android.os.Parcel
import android.os.Parcelable

data class TracksData(

    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,

    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()?: ""
    )

    override fun describeContents(): Int {
        return 0
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
        dest.writeString(previewUrl)
    }

    companion object CREATOR : Parcelable.Creator<TracksData> {
        override fun createFromParcel(parcel: Parcel): TracksData {
            return TracksData(parcel)
        }

        override fun newArray(size: Int): Array<TracksData?> {
            return arrayOfNulls(size)
        }
    }
}