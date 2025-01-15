// User.kt
package com.ahmetselimalpkirisci.watchlater.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ahmetselimalpkirisci.watchlater.db.Converters
import com.ahmetselimalpkirisci.watchlater.util.Utils
import com.google.gson.annotations.SerializedName

@Entity(tableName = Utils.TABLENAME_USER)
@TypeConverters(Converters::class) // Ensure TypeConverters are applied
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("username")
    var username: String?,
    @SerializedName("ratings")
    var ratings: List<Rating>? = emptyList(),
    @SerializedName("recommendations")
    var recommendations: List<Recommendation>? = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.createTypedArrayList(Rating.CREATOR),
        parcel.createTypedArrayList(Recommendation.CREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(username)
        parcel.writeTypedList(ratings)
        parcel.writeTypedList(recommendations)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
