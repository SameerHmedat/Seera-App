package com.example.seeragroup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seeragroup.adapters.LargeAdapter
import com.example.seeragroup.models.Movie
import com.example.seeragroup.models.MoviesModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val largeAdapter by lazy { LargeAdapter() }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refresh_layout.setOnRefreshListener(this@MainActivity::tryAgainButton)



        recyclerViewFull.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = largeAdapter
        }



        empty_view.error().setOnClickListener {
            empty_view.showLoading()
            viewModel.getAllData()
            empty_view.postDelayed({ empty_view.showContent() }, 2000)
        }

        viewModel.moviesLiveData.observe(this@MainActivity) { result ->
            when (result) {
                is Result.Failure -> {
                    largeAdapter.moviesList.clear()
                    empty_view.error().show()
                }
                Result.Loading -> {
                    empty_view.loading().show()
                }
                is Result.Success -> {
                    largeAdapter.moviesList.clear()
                    empty_view.content().show()
                    val popularResults = result.movies.popularMutableLiveData
                    val topRatedResults = result.movies.topRatedMutableLiveData
                    val revenueResults = result.movies.revenueMutableLiveData
                    largeAdapter.moviesList.add(popularResults)
                    largeAdapter.moviesList.add(topRatedResults)
                    largeAdapter.moviesList.add(revenueResults)
                    largeAdapter.notifyDataSetChanged()
                }
            }
        }

        largeAdapter.onClick = { movie ->
            sendObject(movie)
        }

        largeAdapter.emptyError ={
            largeAdapter.moviesList.clear()
            empty_view.error().show()
        }

    }


    private fun sendObject(movie: Movie) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        val myJson = Gson().toJson(movie)
        intent.putExtra("movieObject", myJson)
        startActivity(intent)
    }

    private fun tryAgainButton() {
        largeAdapter.moviesList.clear()
        refresh_layout.isRefreshing = false
        empty_view.showLoading()
        resetValuesScrolling(viewModel)
        viewModel.getAllData()
        empty_view.postDelayed({ empty_view.showContent() }, 2000)
    }

    private fun resetValuesScrolling(viewModel: MainViewModel) {
        viewModel.loadingPopular=true
        viewModel.previousTotalPopular = 0
        viewModel.popularPage = 2

        viewModel.loadingTopRated=true
        viewModel.previousTotalTopRated = 0
        viewModel.topRatedPage = 2

        viewModel.loadingRevenue=true
        viewModel.previousTotalRevenue = 0
        viewModel.revenuePage = 2
    }
}

sealed class Result {
    data class Success(val movies: MoviesModel) : Result()
    data class Failure(val exception: Throwable) : Result()
    object Loading : Result()
}