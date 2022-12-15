package com.example.seeragroup.services

import com.example.seeragroup.Constants.API_KEY
import com.example.seeragroup.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieApiInterface {

    @GET("/3/discover/movie?api_key=$API_KEY")
    suspend fun getMovies(
        @Query("sort_by") sort_by:String,
        @Query("page") page:Int
    ):Response<MovieResponse>

}
