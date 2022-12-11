package com.example.seeragroup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seeragroup.adapters.MovieAdapter
import com.example.seeragroup.models.Movie
import com.example.seeragroup.models.MoviesModel
import com.example.seeragroup.utils.visible
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private lateinit var popularAdapter: MovieAdapter
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var upcomingAdapter: MovieAdapter
    var popularPage = 1
    var topRatedPage = 1
    var upcomingPage = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupRV()

        rvPopular.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollHorizontally(1)) {
                    viewModel.refreshPopularData(popularPage = ++popularPage)
                }
            }
        })

        rvTopRated.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollHorizontally(1)) {
                    viewModel.refreshTopRatedData(topRatedPage = ++topRatedPage)
                }
            }
        })

        rvUpcoming.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollHorizontally(1)) {
                    viewModel.refreshUpcomingData(upcomingPage = ++upcomingPage)
                }
            }
        })


        viewModel.resultLiveData.observe(this@MainActivity) { result ->
            when (result) {
                is Result.Failure -> {
                    movieslayout.visible(false)
                    progress_circular_Loading.visible(false)
                    txtErrorMessage.visible(true)
                }
                Result.Loading -> {
                    progress_circular_Loading.visible(true)
                    movieslayout.visible(false)
                    txtErrorMessage.visible(false)
                }
                is Result.Success -> {
                    movieslayout.visible(true)
                    txtErrorMessage.visible(false)
                    progress_circular_Loading.visible(false)

                    val popularResults = result.movies.popularMutableLiveData
                    popularAdapter = MovieAdapter(popularResults as ArrayList<Movie>)
                    rvPopular.adapter = popularAdapter

                    observePopularLiveData()
                    popularAdapter.onItemClick = { movie ->
                        sendObject(movie)
                    }

                    val topRatedResults = result.movies.topRatedMutableLiveData
                    topRatedAdapter = MovieAdapter(topRatedResults as ArrayList<Movie>)
                    rvTopRated.adapter = topRatedAdapter

                    observeTopRatedLiveData()
                    topRatedAdapter.onItemClick = { movie ->
                        sendObject(movie)
                    }


                    val upcomingResults = result.movies.upcomingMutableLiveData
                    upcomingAdapter = MovieAdapter(upcomingResults as ArrayList<Movie>)
                    rvUpcoming.adapter = upcomingAdapter

                    observeUpcomingLiveData()
                    upcomingAdapter.onItemClick = { movie ->
                        sendObject(movie)
                    }

                }
                else -> {}
            }
        }


    }


    private fun observePopularLiveData() {
        viewModel.popularLiveData.observe(this@MainActivity) { result1 ->
            when (result1) {
                is Result.Success -> {
                    progress_circular_popular.visible(false)
                    popularAdapter.updateData(result1.movies.popularMutableLiveData)
                }
                is Result.Loading -> {
                    progress_circular_popular.visible(true)
                    progress_circular_popular.background = null
                }
                else -> {}
            }
        }
    }

    private fun observeTopRatedLiveData() {
        viewModel.topRatedLiveData.observe(this@MainActivity) { result1 ->
            when (result1) {
                is Result.Success -> {
                    progress_circular_topRated.visible(false)
                    topRatedAdapter.updateData(result1.movies.topRatedMutableLiveData)
                }
                is Result.Loading -> {
                    progress_circular_topRated.visible(true)
                    progress_circular_topRated.background = null
                }
                else -> {}
            }
        }
    }

    private fun observeUpcomingLiveData() {
        viewModel.upcomingLiveData.observe(this@MainActivity) { result1 ->
            when (result1) {
                is Result.Success -> {
                    progress_circular_upcoming.visible(false)
                    upcomingAdapter.updateData(result1.movies.upcomingMutableLiveData)
                }
                is Result.Loading -> {
                    progress_circular_upcoming.visible(true)
                    progress_circular_upcoming.background = null
                }
                else -> {}
            }
        }
    }


    private fun sendObject(movie: Movie) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        val myJson = Gson().toJson(movie)
        intent.putExtra("movieObject", myJson)
        startActivity(intent)
    }


    @SuppressLint("SuspiciousIndentation")
    private fun setupRV() {

        rvPopular.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPopular.setHasFixedSize(true)

        rvTopRated.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTopRated.setHasFixedSize(true)

        rvUpcoming.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvUpcoming.setHasFixedSize(true)

    }

}

sealed class Result {
    data class Success(val movies: MoviesModel) : Result()
    data class Failure(val exception: Throwable) : Result()
    object Loading : Result()
}