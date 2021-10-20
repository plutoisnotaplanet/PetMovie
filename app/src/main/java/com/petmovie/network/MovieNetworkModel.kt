package com.petmovie.network

import com.example.petmovie.BuildConfig
import com.google.gson.annotations.SerializedName
import com.petmovie.entity.Movie

data class MovieNetworkModel(

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("release_date")
    val releasedDate: Long,

    @SerializedName("vote_average")
    val movieRate: Double,

    @SerializedName("overview")
    val movieDescription: String,

    @SerializedName("genres")
    val movieAgeRate: Int,
    val movieGenre: String
)

fun MovieNetworkModel.asMovie() : Movie {
    return Movie(
        id,
        title,
        getPosterUrl(this),
        movieAgeRate,
        movieGenre,
        releasedDate,
        movieDescription,
        movieRate
    )
}

private fun getPosterUrl(it: MovieNetworkModel) = "${BuildConfig.BASE_IMAGE_URL}${it.posterPath}"