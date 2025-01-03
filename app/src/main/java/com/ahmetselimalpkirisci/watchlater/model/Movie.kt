package com.ahmetselimalpkirisci.watchlater.model

import android.os.Parcel
import android.os.Parcelable


class Movie (
    var id: Int,
    var title: String?,
    var director: String?,
    var actors: List<String>?,
    var image: Int,
    var writers: List<String>?,
    var synopsis: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readInt(),
        parcel.createStringArrayList(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(director)
        parcel.writeStringList(actors)
        parcel.writeInt(image)
        parcel.writeStringList(writers)
        parcel.writeString(synopsis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}