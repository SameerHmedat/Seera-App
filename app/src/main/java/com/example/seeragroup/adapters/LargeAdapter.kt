package com.example.seeragroup.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seeragroup.Constants.TYPE_LIST_MOVIES
import com.example.seeragroup.MainActivity
import com.example.seeragroup.MainViewModel
import com.example.seeragroup.Model
import com.example.seeragroup.databinding.MoviesBinding
import com.example.seeragroup.models.Movie
import com.example.seeragroup.utils.visible


class LargeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClick: ((Movie) -> Unit)? = null
    var emptyError: (() -> Unit)? = null


    inner class ListMoviesViewHolder(private val itemBinding: MoviesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val moviesLayoutManager = LinearLayoutManager(
            itemBinding.rvMovies.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        private var movieAdapter: MovieAdapter? = null
        private val viewModel: MainViewModel by lazy { ViewModelProvider(itemBinding.rvMovies.context as MainActivity)[MainViewModel::class.java] }


        init {
            itemBinding.rvMovies.layoutManager = moviesLayoutManager
            itemBinding.rvMovies.setHasFixedSize(true)


        }

        fun bind(movies: Model) {

            when (movies.name) {
                "Popular" -> {
                    observePopularLiveData()
                }
                "Top Rated" -> {
                    observeTopRatedLiveData()
                }
                "Revenue" -> {
                    observeRevenueLiveData()
                }
            }
            itemBinding.txtCategory.text = movies.name
            movieAdapter = MovieAdapter(ArrayList(movies.moviesMutableLiveData))

            itemBinding.rvMovies.adapter = movieAdapter

            movieAdapter?.onItemClick = {
                onClick?.invoke(it)
            }


            itemBinding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = moviesLayoutManager.childCount
                    val totalItemCount = moviesLayoutManager.itemCount
                    val firstVisibleItem = moviesLayoutManager.findFirstVisibleItemPosition()


                    when (movies.name) {
                        "Popular" -> {
                            if (viewModel.loadingPopular) {
                                if (totalItemCount > viewModel.previousTotalPopular) {
                                    viewModel.loadingPopular = false
                                    viewModel.previousTotalPopular = totalItemCount
                                }
                            }
                            if (!viewModel.loadingPopular && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 9) ) {
                                Log.d("popularSameer ", (viewModel.popularPage - 1).toString())
                                viewModel.getNewPopularMovies(popularPage = viewModel.popularPage++)
                                Log.d("popularSameer ", moviesLayoutManager.itemCount.toString())
                                viewModel.loadingPopular = true
                            }
                        }
                        "Top Rated" -> {
                            if (viewModel.loadingTopRated) {
                                if (totalItemCount > viewModel.previousTotalTopRated) {
                                    viewModel.loadingTopRated = false
                                    viewModel.previousTotalTopRated = totalItemCount
                                }
                            }
                            if (!viewModel.loadingTopRated && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 9)) {
                                Log.d("TopRated ", (viewModel.topRatedPage - 1).toString())
                                viewModel.getNewTopRatedMovies(topRatedPage = viewModel.topRatedPage++)
                                Log.d("TopRated ", moviesLayoutManager.itemCount.toString())
                                viewModel.loadingTopRated = true
                            }
                        }

                        "Revenue" -> {

                            if (viewModel.loadingRevenue) {
                                if (totalItemCount > viewModel.previousTotalRevenue) {
                                    viewModel.loadingRevenue = false
                                    viewModel.previousTotalRevenue = totalItemCount
                                }
                            }
                            if (!viewModel.loadingRevenue && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 9)) {
                                Log.d("Revenue ", (viewModel.revenuePage - 1).toString())
                                viewModel.getNewRevenueMovies(revenuePage = viewModel.revenuePage++)
                                Log.d("Revenue ", moviesLayoutManager.itemCount.toString())
                                viewModel.loadingRevenue = true
                            }
                        }
                    }
                }
            })
        }


        private fun observePopularLiveData() {
            viewModel.popularLiveData.observe(itemBinding.rvMovies.context as MainActivity) { result1 ->
                when (result1) {
                    is com.example.seeragroup.Result.Success -> {
                        itemBinding.progressCircularMovies.visible(false)
                        movieAdapter?.updateData(result1.movies.popularMutableLiveData)
                    }
                    is com.example.seeragroup.Result.Loading -> {
                        itemBinding.progressCircularMovies.visible(true)
                        itemBinding.progressCircularMovies.background = null
                    }
                    is com.example.seeragroup.Result.Failure -> {
                        itemBinding.progressCircularMovies.visible(false)
                        emptyError?.invoke()
                        viewModel.loadingPopular = true
                        viewModel.previousTotalPopular = 0
                        viewModel.popularPage--
                    }
                }
            }
        }

        private fun observeTopRatedLiveData() {
            viewModel.topRatedLiveData.observe(itemBinding.rvMovies.context as MainActivity) { result1 ->
                when (result1) {
                    is com.example.seeragroup.Result.Success -> {
                        itemBinding.progressCircularMovies.visible(false)
                        movieAdapter?.updateData(result1.movies.topRatedMutableLiveData)
                    }
                    is com.example.seeragroup.Result.Loading -> {
                        itemBinding.progressCircularMovies.visible(true)
                        itemBinding.progressCircularMovies.background = null
                    }
                    is com.example.seeragroup.Result.Failure -> {
                        itemBinding.progressCircularMovies.visible(false)
                        emptyError?.invoke()
                        viewModel.loadingTopRated = true
                        viewModel.previousTotalTopRated = 0
                        viewModel.topRatedPage--
                    }
                }
            }
        }

        private fun observeRevenueLiveData() {
            viewModel.revenueLiveData.observe(itemBinding.rvMovies.context as MainActivity) { result1 ->
                when (result1) {
                    is com.example.seeragroup.Result.Success -> {
                        itemBinding.progressCircularMovies.visible(false)
                        movieAdapter?.updateData(result1.movies.revenueMutableLiveData)
                    }
                    is com.example.seeragroup.Result.Loading -> {
                        itemBinding.progressCircularMovies.visible(true)
                        itemBinding.progressCircularMovies.background = null
                    }
                    is com.example.seeragroup.Result.Failure -> {
                        itemBinding.progressCircularMovies.visible(false)
                        emptyError?.invoke()
                        viewModel.loadingRevenue = true
                        viewModel.previousTotalRevenue = 0
                        viewModel.revenuePage--
                    }
                }
            }
        }

    }


    var moviesList = arrayListOf<Model>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LIST_MOVIES -> ListMoviesViewHolder(
                MoviesBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListMoviesViewHolder -> holder.bind(moviesList[position])
        }
    }

    override fun getItemCount(): Int = moviesList.size

    override fun getItemViewType(position: Int): Int {
        return when (moviesList[position].name) {
            "Popular" -> TYPE_LIST_MOVIES
            "Top Rated" -> TYPE_LIST_MOVIES
            "Revenue" -> TYPE_LIST_MOVIES

            else -> throw IllegalArgumentException("Invalid Item")
        }
    }


}