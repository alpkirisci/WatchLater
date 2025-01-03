package com.ahmetselimalpkirisci.watchlater.model

import android.os.Parcel
import android.os.Parcelable

class User (
    var id: Int,
    var username: String?,
    var ratings: List<Rating>?,
    var recommendations: List<Recommendation>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.createTypedArrayList(Rating),
        parcel.createTypedArrayList(Recommendation)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(username)
        parcel.writeTypedList(ratings)
        parcel.writeTypedList(recommendations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

