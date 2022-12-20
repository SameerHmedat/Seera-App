package com.example.seeragroup

import android.content.Intent
import android.graphics.Color
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        refresh_layout.setProgressBackgroundColorSchemeColor(Color.WHITE)
        refresh_layout.setColorSchemeColors(Color.RED)
        refresh_layout.setOnRefreshListener(this@MainActivity::tryAgainButton)


        recyclerViewFull.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = largeAdapter

        }

        empty_view.error().setOnClickListener {
            empty_view.showLoading()
            viewModel.getAllData()
        }

        viewModel.moviesLiveData.observe(this@MainActivity) { result ->
            when (result) {
                is Result.Failure -> {
                    empty_view.error().show()
                }
                Result.Loading -> {
                    empty_view.loading().show()
                }
                is Result.Success -> {
                    empty_view.content().show()
                    largeAdapter.moviesList.clear()
                    largeAdapter.moviesList.add(result.movies.popularMutableLiveData)
                    largeAdapter.moviesList.add(result.movies.topRatedMutableLiveData)
                    largeAdapter.moviesList.add(result.movies.revenueMutableLiveData)
                    largeAdapter.notifyItemRangeChanged(0,3)
                }
            }
        }

        largeAdapter.onClick = { movie ->
            sendObject(movie)
        }

        largeAdapter.emptyError = {
            empty_view.error().show()

            empty_view.error().setOnClickListener {
                empty_view.showLoading()
                empty_view.postDelayed({ empty_view.showContent() }, 2000)
            }
        }

    }


    private fun sendObject(movie: Movie) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        val myJson = Gson().toJson(movie)
        intent.putExtra("movieObject", myJson)
        startActivity(intent)
    }

    private fun tryAgainButton() {
        refresh_layout.isRefreshing = false
        viewModel.resetScrolling()
        viewModel.getAllData()
    }

}

sealed class Result {
    data class Success(val movies: MoviesModel) : Result()
    data class Failure(val exception: Throwable) : Result()
    object Loading : Result()
}