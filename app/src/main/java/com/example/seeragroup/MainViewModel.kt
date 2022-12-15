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

    val moviesLiveData = MutableLiveData<Result>()

    var popularLiveData = MutableLiveData<Result>()
    var topRatedLiveData = MutableLiveData<Result>()
    var revenueLiveData = MutableLiveData<Result>()

    var popularPage = 2
    var loadingPopular = true
    var previousTotalPopular = 0

    var topRatedPage = 2
    var loadingTopRated = true
    var previousTotalTopRated = 0

    var revenuePage = 2
    var loadingRevenue = true
    var previousTotalRevenue = 0


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("Exception handled:", throwable.localizedMessage as String)
    }

    init {
        getAllData()
    }

    @SuppressLint("SuspiciousIndentation")
    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            moviesLiveData.postValue(Result.Loading)

            try {
                val popularResponse = MovieApiService.getInstance().getMovies(sort_by = "popularity.desc", page = 1)
                val topRatedResponse = MovieApiService.getInstance().getMovies(sort_by = "vote_average.desc", page = 1)
                val revenueResponse = MovieApiService.getInstance().getMovies(sort_by = "revenue.desc", page = 1)
                moviesLiveData.postValue(
                    Result.Success(
                        MoviesModel(
                            popularMutableLiveData = popularResponse.body()!!.movies,
                            topRatedMutableLiveData = topRatedResponse.body()!!.movies,
                            revenueMutableLiveData = revenueResponse.body()!!.movies
                        )
                    )
                )

            } catch (e: Exception) {
                moviesLiveData.postValue(
                    Result.Failure(
                        Throwable(
                            "Error sorry"
                        )
                    )
                )
            }
        }
    }

    fun getNewPopularMovies(popularPage: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            popularLiveData.postValue(Result.Loading)
            try {
                val popularResponse = MovieApiService.getInstance().getMovies(sort_by = "popularity.desc", page = popularPage)
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

    fun getNewTopRatedMovies(topRatedPage: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            topRatedLiveData.postValue(Result.Loading)
            try {
                val topRatedResponse = MovieApiService.getInstance().getMovies(sort_by = "vote_average.desc", page = topRatedPage)
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

    fun getNewRevenueMovies(revenuePage: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            revenueLiveData.postValue(Result.Loading)
            try {
                val revenueResponse = MovieApiService.getInstance().getMovies(sort_by = "revenue.desc", page = revenuePage)
                revenueLiveData.postValue(
                    Result.Success(MoviesModel(revenueMutableLiveData = revenueResponse.body()!!.movies))
                )
            } catch (ex: Exception) {
                revenueLiveData.postValue(
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