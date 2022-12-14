package com.example.seeragroup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seeragroup.adapters.MovieAdapter
import com.example.seeragroup.adapters.LargeAdapter
import com.example.seeragroup.models.Movie
import com.example.seeragroup.models.MoviesModel
import com.example.seeragroup.utils.visible
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    private lateinit var popularAdapter: MovieAdapter
    var popularPage = 1
    val popularLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    private var previousTotalPopular = 0
    private var loadingPopular = true


    private lateinit var topRatedAdapter: MovieAdapter
    var topRatedPage = 1
    val topRatedLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    private var previousTotalTopRated = 0
    private var loadingTopRated = true

    private lateinit var upcomingAdapter: MovieAdapter
    var upcomingPage = 1
    val upcomingLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    private var previousTotalUpcoming = 0
    private var loadingUpcoming = true


    private val visibleThreshold = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerViews()

        refresh_layout.setOnRefreshListener(this@MainActivity::tryAgainButton)

        rvPopular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = popularLayoutManager.childCount
                val totalItemCount = popularLayoutManager.itemCount
                val firstVisibleItem = popularLayoutManager.findFirstVisibleItemPosition()
                if (loadingPopular) {
                    if (totalItemCount > previousTotalPopular) {
                        loadingPopular = false
                        previousTotalPopular = totalItemCount
                    }
                }
                if (!loadingPopular && (totalItemCount - visibleItemCount - 4) <= (firstVisibleItem + visibleThreshold)) {
                    viewModel.refreshPopularData(popularPage = ++popularPage)
                    Log.d("waled", popularPage.toString())
                    Log.d("waled", popularLayoutManager.itemCount.toString())
                    loadingPopular = true
                }
            }
        })


        rvTopRated.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = topRatedLayoutManager.childCount
                val totalItemCount = topRatedLayoutManager.itemCount
                val firstVisibleItem = topRatedLayoutManager.findFirstVisibleItemPosition()
                if (loadingTopRated) {
                    if (totalItemCount > previousTotalTopRated) {
                        loadingTopRated = false
                        previousTotalTopRated = totalItemCount
                    }
                }
                if (!loadingTopRated && (totalItemCount - visibleItemCount - 4) <= (firstVisibleItem + visibleThreshold)) {
                    viewModel.refreshTopRatedData(topRatedPage = ++topRatedPage)
                    loadingTopRated = true
                }
            }
        })


        rvUpcoming.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = upcomingLayoutManager.childCount
                val totalItemCount = upcomingLayoutManager.itemCount
                val firstVisibleItem = upcomingLayoutManager.findFirstVisibleItemPosition()
                if (loadingUpcoming) {
                    if (totalItemCount > previousTotalUpcoming) {
                        loadingUpcoming = false
                        previousTotalUpcoming = totalItemCount
                    }
                }
                if (!loadingUpcoming && (totalItemCount - visibleItemCount - 4) <= (firstVisibleItem + visibleThreshold)) {
                    viewModel.refreshUpcomingData(upcomingPage = ++upcomingPage)
                    loadingUpcoming = true
                }

            }
        })



        empty_view.error().setOnClickListener {
            empty_view.showLoading()
            viewModel.getAllData(1)
            empty_view.postDelayed({ empty_view.showContent() }, 2000)
        }

        viewModel.resultLiveData.observe(this@MainActivity) { result ->
            when (result) {
                is Result.Failure -> {
                    empty_view.error().show()
                }
                Result.Loading -> {
                    empty_view.loading().show()
                }
                is Result.Success -> {
                    empty_view.content().show()
                    setDataOnViews(result)
                }
            }
        }


    }


        private fun setDataOnViews(result: Result.Success) {
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
              is Result.Failure->{
                  progress_circular_popular.visible(false)
                  empty_view.error().show()
                  loadingPopular=false
              }
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
                is Result.Failure->{
                    progress_circular_topRated.visible(false)
                    empty_view.error().show()
                    loadingTopRated=false
                }
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
                is Result.Failure->{
                    progress_circular_upcoming.visible(false)
                    empty_view.error().show()
                    loadingUpcoming=false
                }
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
    private fun setupRecyclerViews() {

        rvPopular.layoutManager = popularLayoutManager
        rvPopular.setHasFixedSize(true)

        rvTopRated.layoutManager = topRatedLayoutManager
        rvTopRated.setHasFixedSize(true)

        rvUpcoming.layoutManager = upcomingLayoutManager
        rvUpcoming.setHasFixedSize(true)

    }

    private fun tryAgainButton() {
        refresh_layout.isRefreshing = false
        empty_view.showLoading()
        viewModel.getAllData(1)
        empty_view.postDelayed({ empty_view.showContent() }, 2000)
    }
}

sealed class Result {
    data class Success(val movies: MoviesModel) : Result()
    data class Failure(val exception: Throwable) : Result()
    object Loading : Result()
}