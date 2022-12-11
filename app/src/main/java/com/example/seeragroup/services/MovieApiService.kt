package com.example.seeragroup.services

import com.example.seeragroup.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object MovieApiService {
    private var movieApiInterface: MovieApiInterface? = null

    fun getInstance(): MovieApiInterface {
        if (movieApiInterface == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            movieApiInterface = retrofit.create(MovieApiInterface::class.java)
        }
        return movieApiInterface!!
    }

}
