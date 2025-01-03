package com.ahmetselimalpkirisci.watchlater.model

import android.os.Parcel
import android.os.Parcelable

class Recommendation (
    var movieId: Int,
    var calcPoint: Double,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(movieId)
        parcel.writeDouble(calcPoint)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recommendation> {
        override fun createFromParcel(parcel: Parcel): Recommendation {
            return Recommendation(parcel)
        }

        override fun newArray(size: Int): Array<Recommendation?> {
            return arrayOfNulls(size)
        }
    }
}