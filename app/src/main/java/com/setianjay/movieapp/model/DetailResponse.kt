package com.setianjay.movieapp.model

data class DetailResponse(
    val id: Int?,
    val genres: List<GenreModel>?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val vote_average: Double?

) {
}