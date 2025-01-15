package com.ahmetselimalpkirisci.watchlater.retrofit

import com.ahmetselimalpkirisci.watchlater.model.Movie
import retrofit2.http.GET


interface ApiService {
    @GET("b/66KA")
    suspend fun getMovies(): List<Movie>
}