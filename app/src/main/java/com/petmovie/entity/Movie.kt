package com.petmovie.entity

import android.os.Parcel
import android.os.Parcelable
import com.example.petmovie.BuildConfig
import com.petmovie.network.MovieNetworkModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val name: String,
    val thumbnail: String?,
    val description: String?,
    val rating: Double?,
    val budget: Int?,
    val pubDate: String?,
    var similarMovies: List<Movie> = emptyList()) : Parcelable {

}


fun MovieNetworkModel.asMovie(): Movie {
    return Movie(
        id,
        title,
        getPosterUrl(posterPath),
        overview,
        voteAverage,
        budget,
        releasedDate
    )
}

private fun getPosterUrl(posterPath: String?) = "${BuildConfig.BASE_IMAGE_URL}${posterPath}"