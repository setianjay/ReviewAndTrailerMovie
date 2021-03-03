package com.setianjay.movieapp.model

data class MovieResponse(
    val results: List<MovieModel>,
    val total_pages: Int
)