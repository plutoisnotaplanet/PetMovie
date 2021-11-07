package com.petmovie.network

import com.google.gson.annotations.SerializedName

data class MovieNetworkModel(

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("budget")
    val budget: Int?,

    @SerializedName("release_date")
    val releasedDate: String?
)