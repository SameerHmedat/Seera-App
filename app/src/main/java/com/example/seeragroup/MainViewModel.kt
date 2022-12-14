package com.example.seeragroup

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seeragroup.models.MoviesModel
import com.example.seeragroup.services.MovieApiService
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    val resultLiveData = MutableLiveData<Result>()

    var popularLiveData = MutableLiveData<Result>()
    var topRatedLiveData = MutableLiveData<Result>()
    var upcomingLiveData = MutableLiveData<Result>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("Exception handled:", throwable.localizedMessage as String)
    }

    init {
        getAllData(1)
    }

    @SuppressLint("SuspiciousIndentation")
    fun getAllData(popularPage: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            resultLiveData.postValue(Result.Loading)

            try {
                val popularResponse = MovieApiService.getInstance().getMoviePopularList(popularPage)
                val topRatedResponse = MovieApiService.getInstance().getMovieTopRatedList(1)
                val upcomingResponse = MovieApiService.getInstance().getMovieUpcomingList(1)
                resultLiveData.postValue(
                    Result.Success(
                        MoviesModel(
                            popularResponse.body()!!.movies,
                            topRatedResponse.body()!!.movies,
                            upcomingResponse.body()!!.movies
                        )
                    )
                )

            } catch (e: Exception) {
                resultLiveData.postValue(
                    Result.Failure(
                        Throwable(
                            "Error sorry"
                        )
                    )
                )
            }
        }
    }

    fun refreshPopularData(popularPage: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            popularLiveData.postValue(Result.Loading)
            try {
                val popularResponse = MovieApiService.getInstance().getMoviePopularList(popularPage)
                popularLiveData.postValue(
                    Result.Success(MoviesModel(popularMutableLiveData = popularResponse.body()!!.movies))
                )
            } catch (ex: Exception) {
                popularLiveData.postValue(
                    Result.Failure(
                    Throwable(
                        "Error sorry"
                    )
                    )
                )
            }
        }
    }

    fun refreshTopRatedData(topRatedPage: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            topRatedLiveData.postValue(Result.Loading)
            try {
                val topRatedResponse =
                    MovieApiService.getInstance().getMovieTopRatedList(topRatedPage)
                topRatedLiveData.postValue(
                    Result.Success(MoviesModel(topRatedMutableLiveData = topRatedResponse.body()!!.movies))
                )
            } catch (ex: Exception) {
                topRatedLiveData.postValue(
                    Result.Failure(
                    Throwable(
                        "Error sorry"
                    )
                )
               )
            }
        }
    }

    fun refreshUpcomingData(upcomingPage: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            upcomingLiveData.postValue(Result.Loading)
            try {
                val upcomingResponse =
                    MovieApiService.getInstance().getMovieUpcomingList(upcomingPage)
                upcomingLiveData.postValue(
                    Result.Success(MoviesModel(upcomingMutableLiveData = upcomingResponse.body()!!.movies))
                )
            } catch (ex: Exception) {
                upcomingLiveData.postValue(
                    Result.Failure(
                    Throwable(
                        "Error sorry"
                    )
                    )
                )
            }
        }
    }


}