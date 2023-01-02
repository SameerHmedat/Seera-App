package com.example.seeragroup.models

data class Model (
    val sections: List<Section> = mutableListOf()
)

data class Section(
    val id: String,
    val movies: ArrayList<Movie>
)