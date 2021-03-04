package com.setianjay.movieapp.model

data class DetailResponse(
    val id: Int?,
    val genres: List<GenreModel>?,
    val backdrop_path: String?,
    val poster_path: String?,
    val title: String?,
    val overview: String?,
    val release_date: String?,
    val vote_average: Double?

) {
}