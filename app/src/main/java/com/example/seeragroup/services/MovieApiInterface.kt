package com.example.seeragroup.services

import com.example.seeragroup.Constants
import com.example.seeragroup.Constants.BASE_URL
import com.example.seeragroup.models.MovieResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieApiInterface {
    @GET("/3/discover/movie?api_key=114fe6670282f6a632638661e5e86dee")
    suspend fun getMoviePopularList(
        @Query("page") page:Int,
        @Query("language") language:String="en"
    ):Response<MovieResponse>

    @GET("/3/movie/top_rated?api_key=114fe6670282f6a632638661e5e86dee")
    suspend fun getMovieTopRatedList(
        @Query("page") page:Int,
        @Query("language") language:String="en"
    ):Response<MovieResponse>

    @GET("/3/movie/upcoming?api_key=114fe6670282f6a632638661e5e86dee")
    suspend fun getMovieUpcomingList(
        @Query("page") page:Int,
        @Query("language") language:String="en"
    ):Response<MovieResponse>


}
