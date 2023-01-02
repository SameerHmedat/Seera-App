package com.example.seeragroup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seeragroup.Constants.loading
import com.example.seeragroup.adapters.LargeAdapter
import com.example.seeragroup.models.Movie
import com.example.seeragroup.models.Model
import com.example.seeragroup.models.Section
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy { MainViewModel() }
    private val largeAdapter by lazy { LargeAdapter(arrayListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refresh_layout.setProgressBackgroundColorSchemeColor(Color.WHITE)
        refresh_layout.setColorSchemeColors(Color.RED)
        refresh_layout.setOnRefreshListener(this@MainActivity::tryAgainButton)

        rvFull.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = largeAdapter
            setHasFixedSize(false)
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
                is Result.Loading -> {
                    empty_view.loading().show()
                }
                is Result.Success -> {
                    empty_view.content().show()
                    largeAdapter.moviesList.clear()
                    largeAdapter.moviesList = result.movies.sections as ArrayList<Section>
                    largeAdapter.notifyItemRangeChanged(0, 3)
                }
            }
        }


        largeAdapter.onMovieClick = { movie ->
            sendObject(movie)
        }

        largeAdapter.scrolling = { visibleItemCount, totalItemCount, firstVisibleItem, name ->
            viewModel.scrolling(visibleItemCount, totalItemCount, firstVisibleItem, name)
        }

        viewModel.newMoviesLiveData.observe(this@MainActivity) { result ->
            when (result) {
                is Result.Failure -> {
                    loading = false
                    empty_view.error().show()

                    empty_view.error().setOnClickListener {
                        empty_view.showLoading()
                        empty_view.postDelayed({ empty_view.showContent() }, 2000)
                    }
                    viewModel.updateScrollingValues(error = true)
                }
                is Result.Loading -> {
                    loading = true
                }
                is Result.Success -> {
                    loading = false
                    result.movies.sections.forEach {
                        for (i in 0 until largeAdapter.moviesList.size) {
                            if (largeAdapter.moviesList[i].id == it.id) {
                                largeAdapter.moviesList[i].movies.addAll(it.movies)
                                break
                            }
                        }
                    }
                }
            }
        }


    }

    override fun onStop() {
        super.onStop()
        this.cacheDir.deleteRecursively()
    }


    private fun sendObject(movie: Movie) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        val myJson = Gson().toJson(movie)
        intent.putExtra("movieObject", myJson)
        startActivity(intent)
    }

    private fun tryAgainButton() {
        refresh_layout.isRefreshing = false
        viewModel.updateScrollingValues(error = false)
        this.cacheDir.deleteRecursively()
        viewModel.getAllData()
    }

}

sealed class Result {
    data class Success(val movies: Model) : Result()
    data class Failure(val exception: Throwable) : Result()
    object Loading : Result()
}