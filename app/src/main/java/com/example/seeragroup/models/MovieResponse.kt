package com.example.seeragroup.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val total_pages: Int?,
    val total_results: Int?,
    val page: Int?
)