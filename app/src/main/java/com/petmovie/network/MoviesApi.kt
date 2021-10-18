package com.petmovie.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): SearchMovieResponse
}