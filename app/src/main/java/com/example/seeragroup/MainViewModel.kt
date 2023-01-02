package com.example.seeragroup

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seeragroup.models.Movie
import com.example.seeragroup.models.Model
import com.example.seeragroup.models.Section
import com.example.seeragroup.services.MovieApiService
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    val moviesLiveData = MutableLiveData<Result>()
    val newMoviesLiveData = MutableLiveData<Result>()

    var popularPage = 1
    var loadingPopular = true
    var previousTotalPopular = 0

    var topRatedPage = 1
    var loadingTopRated = true
    var previousTotalTopRated = 0

    var revenuePage = 1
    var loadingRevenue = true
    var previousTotalRevenue = 0

    fun updateScrollingValues(error: Boolean) {
        loadingPopular = true
        loadingTopRated = true
        loadingRevenue = true
        previousTotalPopular = 0
        previousTotalTopRated = 0
        previousTotalRevenue = 0
        if (error) {
            popularPage -= 1
            topRatedPage -= 1
            revenuePage -= 1
        } else {
            popularPage = 1
            topRatedPage = 1
            revenuePage = 1
        }
    }

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
                moviesLiveData.postValue(
                    Result.Success(
                        Model(arrayListOf<Section>(
                            Section("Popular", MovieApiService.getInstance().getMovies(sort_by = "popularity.desc", page = 1).body()!!.movies as ArrayList<Movie>),
                            Section("Top Rated", MovieApiService.getInstance().getMovies(sort_by = "vote_average.desc", page = 1).body()!!.movies as ArrayList<Movie>),
                            Section("Revenue",  MovieApiService.getInstance().getMovies(sort_by = "revenue.desc", page = 1).body()!!.movies as ArrayList<Movie>),
                        )
                        )
                    )
                )
            } catch (e: Exception) {
                moviesLiveData.postValue(
                    Result.Failure(
                        Throwable(
                            e.message.toString()
                        )
                    )
                )
            }
        }
    }

    private fun getNewMovies(id: String, page: Int, sort_by: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            newMoviesLiveData.postValue(Result.Loading)
            try {
                val response =
                    MovieApiService.getInstance().getMovies(sort_by = sort_by, page = page)
                newMoviesLiveData.postValue(
                    Result.Success(Model(sections = arrayListOf(
                        Section(id, response.body()!!.movies as ArrayList<Movie>)
                    )))
                )

            } catch (ex: Exception) {
                newMoviesLiveData.postValue(
                    Result.Failure(
                        Throwable(
                            ex.message.toString()
                        )
                    )
                )
            }
        }
    }

    fun scrolling(visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int, id: String) {

        when (id) {
            "Popular" -> {
                if (loadingPopular) {
                    if (totalItemCount > previousTotalPopular) {
                        loadingPopular = false
                        previousTotalPopular = totalItemCount
                    }
                }
                if (!loadingPopular && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 9)) {
                    getNewMovies("Popular", ++popularPage, "popularity.desc")
                    Log.d("popularSameer ", (popularPage).toString())
                    loadingPopular = true
                }
            }
            "Top Rated" -> {
                if (loadingTopRated) {
                    if (totalItemCount > previousTotalTopRated) {
                        loadingTopRated = false
                        previousTotalTopRated = totalItemCount
                    }
                }
                if (!loadingTopRated && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 9)) {
                    getNewMovies("Top Rated", ++topRatedPage, "vote_average.desc")
                    Log.d("TopRated ", (topRatedPage).toString())
                    loadingTopRated = true
                }
            }
            "Revenue" -> {
                if (loadingRevenue) {
                    if (totalItemCount > previousTotalRevenue) {
                        loadingRevenue = false
                        previousTotalRevenue = totalItemCount
                    }
                }
                if (!loadingRevenue && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 9)) {
                    getNewMovies("Revenue", ++revenuePage, "revenue.desc")
                    Log.d("Revenue ", (revenuePage).toString())
                    loadingRevenue = true
                }
            }
        }
    }


}
