package com.petmovie.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): SearchMovieResponse

    @GET("movie/top_rated")
    suspend fun topMovie(
        @Query("api_key") api_key: String,
        @Query("page") page: Int = 1
    ): SearchMovieResponse

    @GET("movie/{movie_id}")
    suspend fun detailMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
    ): MovieNetworkModel

    @GET("movie/{movie_id}/similar")
    suspend fun similarMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("page") page: Int = 1
    ) : SearchMovieResponse
}