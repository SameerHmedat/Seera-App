package com.example.seeragroup.models



data class MoviesModel (
    val popularMutableLiveData: List<Movie> = mutableListOf(),
    val topRatedMutableLiveData: List<Movie> = mutableListOf(),
    val revenueMutableLiveData: List<Movie> = mutableListOf()
)
