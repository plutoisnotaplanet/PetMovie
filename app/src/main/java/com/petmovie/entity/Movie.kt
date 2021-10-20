package com.petmovie.entity

data class Movie(
    val id: Int,
    val name: String,
    val thumbnail: String?,
    val ageRate: Int?,
    val Genre: String?,
    val releasedDate: Long?,
    val description: String?,
    val rate: Double?)