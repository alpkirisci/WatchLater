package com.ahmetselimalpkirisci.watchlater.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ahmetselimalpkirisci.watchlater.model.Rating
import com.ahmetselimalpkirisci.watchlater.model.Recommendation

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromRatingList(ratings: List<Rating>?): String? {
        return gson.toJson(ratings)
    }

    @TypeConverter
    fun toRatingList(data: String?): List<Rating>? {
        if (data == null) return emptyList()
        val listType = object : TypeToken<List<Rating>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromRecommendationList(recommendations: List<Recommendation>?): String? {
        return gson.toJson(recommendations)
    }

    @TypeConverter
    fun toRecommendationList(data: String?): List<Recommendation>? {
        if (data == null) return emptyList()
        val listType = object : TypeToken<List<Recommendation>>() {}.type
        return gson.fromJson(data, listType)
    }
}
