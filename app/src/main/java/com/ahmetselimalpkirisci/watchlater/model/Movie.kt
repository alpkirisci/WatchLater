package com.ahmetselimalpkirisci.watchlater.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


class Movie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String?,
    @SerializedName("director")
    val director: String?,
    @SerializedName("actors")
    val actors: List<String>?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("writers")
    val writers: List<String>?,
    @SerializedName("synopsis")
    val synopsis: String?,
    @SerializedName("similars")
    val similars: IntArray?,
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.createIntArray()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(director)
        parcel.writeStringList(actors)
        parcel.writeString(imageUrl)
        parcel.writeStringList(writers)
        parcel.writeString(synopsis)
        parcel.writeIntArray(similars)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Movie(id=$id, title=$title, director=$director, actors=$actors, imageUrl=$imageUrl, writers=$writers, synopsis=$synopsis, similars=${similars?.contentToString()})"
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