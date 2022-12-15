package com.example.seeragroup.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seeragroup.Constants.TYPE_LIST_MOVIES_POPULAR
import com.example.seeragroup.Constants.TYPE_LIST_MOVIES_TOP_RATED
import com.example.seeragroup.Constants.TYPE_LIST_MOVIES_REVENUE
import com.example.seeragroup.MainActivity
import com.example.seeragroup.MainViewModel
import com.example.seeragroup.databinding.MoviesPopularBinding
import com.example.seeragroup.databinding.MoviesRevenueBinding
import com.example.seeragroup.databinding.MoviesTopRatedBinding
import com.example.seeragroup.models.Movie
import com.example.seeragroup.utils.visible


class LargeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClick: ((Movie) -> Unit)? = null
    var emptyError: (() -> Unit)? = null


    inner class ListOfPopularMoviesViewHolder(private val itemBinding: MoviesPopularBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val popularLayoutManager = LinearLayoutManager(
            itemBinding.rvPopular.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        private var popularAdapter: MovieAdapter? = null
        private val viewModel: MainViewModel by lazy { ViewModelProvider(itemBinding.rvPopular.context as MainActivity)[MainViewModel::class.java] }


        init {
            itemBinding.rvPopular.layoutManager = popularLayoutManager
            itemBinding.rvPopular.setHasFixedSize(true)

            itemBinding.rvPopular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = popularLayoutManager.childCount
                    val totalItemCount = popularLayoutManager.itemCount
                    val firstVisibleItem = popularLayoutManager.findFirstVisibleItemPosition()
                    if (viewModel.loadingPopular) {
                        if (totalItemCount > viewModel.previousTotalPopular) {
                            viewModel.loadingPopular = false
                            viewModel.previousTotalPopular = totalItemCount
                        }
                    }
                    if (!viewModel.loadingPopular && (totalItemCount - visibleItemCount ) <= (firstVisibleItem + 9)) {
                        Log.d("popular ", (viewModel.popularPage).toString())
                        viewModel.getNewPopularMovies(popularPage = viewModel.popularPage++)
                        Log.d("popular ", popularLayoutManager.itemCount.toString())
                        viewModel.loadingPopular = true
                    }
                }
            })
            observePopularLiveData()

        }

        @SuppressLint("NotifyDataSetChanged")
        fun bind(movies: List<Movie>) {

            popularAdapter = MovieAdapter(ArrayList(movies))

            itemBinding.rvPopular.adapter = popularAdapter

            popularAdapter?.onItemClick = {
                onClick?.invoke(it)
            }

        }

        private fun observePopularLiveData() {
            viewModel.popularLiveData.observe(itemBinding.rvPopular.context as MainActivity) { result1 ->
                when (result1) {
                    is com.example.seeragroup.Result.Success -> {
                        itemBinding.progressCircularPopular.visible(false)
                        popularAdapter?.updateData(result1.movies.popularMutableLiveData)
                    }
                    is com.example.seeragroup.Result.Loading -> {
                        itemBinding.progressCircularPopular.visible(true)
                        itemBinding.progressCircularPopular.background = null
                    }
                    is com.example.seeragroup.Result.Failure -> {
                        itemBinding.progressCircularPopular.visible(false)
                        emptyError?.invoke()
                        resetValuesScrolling(viewModel)
                    }
                }
            }
        }
    }

    private fun resetValuesScrolling(viewModel: MainViewModel) {
        viewModel.loadingPopular = true
        viewModel.previousTotalPopular = 0
        viewModel.popularPage = 2

        viewModel.loadingTopRated = true
        viewModel.previousTotalTopRated = 0
        viewModel.topRatedPage = 2

        viewModel.loadingRevenue = true
        viewModel.previousTotalRevenue = 0
        viewModel.revenuePage = 2
    }


    inner class ListOfTopRatedMoviesViewHolder(private val itemBinding: MoviesTopRatedBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val topRatedLayoutManager = LinearLayoutManager(
            itemBinding.rvTopRated.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        private var topRatedAdapter: MovieAdapter? = null
        private val viewModel: MainViewModel by lazy { ViewModelProvider(itemBinding.rvTopRated.context as MainActivity)[MainViewModel::class.java] }


        init {
            itemBinding.rvTopRated.layoutManager = topRatedLayoutManager
            itemBinding.rvTopRated.setHasFixedSize(true)

            itemBinding.rvTopRated.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = topRatedLayoutManager.childCount
                    val totalItemCount = topRatedLayoutManager.itemCount
                    val firstVisibleItem = topRatedLayoutManager.findFirstVisibleItemPosition()
                    if (viewModel.loadingTopRated) {
                        if (totalItemCount > viewModel.previousTotalTopRated) {
                            viewModel.loadingTopRated = false
                            viewModel.previousTotalTopRated = totalItemCount
                        }
                    }
                    if (!viewModel.loadingTopRated && (totalItemCount - visibleItemCount ) <= (firstVisibleItem + 9)) {
                        Log.d("topRated", (viewModel.topRatedPage).toString())
                        viewModel.getNewTopRatedMovies(topRatedPage = viewModel.topRatedPage++)
                        Log.d("topRated", topRatedLayoutManager.itemCount.toString())
                        viewModel.loadingTopRated = true
                    }
                }
            })
            observeTopRatedLiveData()

        }


        fun bind(movies: List<Movie>) {
            topRatedAdapter = MovieAdapter(ArrayList(movies))
            itemBinding.rvTopRated.adapter = topRatedAdapter

            topRatedAdapter?.onItemClick = {
                onClick?.invoke(it)
            }
        }

        private fun observeTopRatedLiveData() {
            viewModel.topRatedLiveData.observe(itemBinding.rvTopRated.context as MainActivity) { result1 ->
                when (result1) {
                    is com.example.seeragroup.Result.Success -> {
                        itemBinding.progressCircularTopRated.visible(false)
                        topRatedAdapter?.updateData(result1.movies.topRatedMutableLiveData)
                    }
                    is com.example.seeragroup.Result.Loading -> {
                        itemBinding.progressCircularTopRated.visible(true)
                        itemBinding.progressCircularTopRated.background = null
                    }
                    is com.example.seeragroup.Result.Failure -> {
                        itemBinding.progressCircularTopRated.visible(false)
                        emptyError?.invoke()
                        resetValuesScrolling(viewModel)
                    }
                }
            }
        }
    }


    inner class ListOfRevenueMoviesViewHolder(private val itemBinding: MoviesRevenueBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val revenueLayoutManager = LinearLayoutManager(
            itemBinding.rvRevenue.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        private var revenueAdapter: MovieAdapter? = null
        private val viewModel: MainViewModel by lazy { ViewModelProvider(itemBinding.rvRevenue.context as MainActivity)[MainViewModel::class.java] }

        init {
            itemBinding.rvRevenue.layoutManager = revenueLayoutManager
            itemBinding.rvRevenue.setHasFixedSize(true)

            itemBinding.rvRevenue.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = revenueLayoutManager.childCount
                    val totalItemCount = revenueLayoutManager.itemCount
                    val firstVisibleItem = revenueLayoutManager.findFirstVisibleItemPosition()
                    if (viewModel.loadingRevenue) {
                        if (totalItemCount > viewModel.previousTotalRevenue) {
                            viewModel.loadingRevenue = false
                            viewModel.previousTotalRevenue = totalItemCount
                        }
                    }
                    if (!viewModel.loadingRevenue && (totalItemCount - visibleItemCount ) <= (firstVisibleItem + 9)) {
                        Log.d("revenue ", (viewModel.revenuePage).toString())
                        viewModel.getNewRevenueMovies(revenuePage = viewModel.revenuePage++)
                        Log.d("revenue ", revenueLayoutManager.itemCount.toString())
                        viewModel.loadingRevenue = true
                    }
                }
            })
            observeRevenueLiveData()
        }

        fun bind(movies: List<Movie>) {

            revenueAdapter = MovieAdapter(ArrayList(movies))
            itemBinding.rvRevenue.adapter = revenueAdapter

            revenueAdapter?.onItemClick = {
                onClick?.invoke(it)
            }
        }

        private fun observeRevenueLiveData() {
            viewModel.revenueLiveData.observe(itemBinding.rvRevenue.context as MainActivity) { result1 ->
                when (result1) {
                    is com.example.seeragroup.Result.Success -> {
                        itemBinding.progressCircularRevenue.visible(false)
                        revenueAdapter?.updateData(result1.movies.revenueMutableLiveData)
                    }
                    is com.example.seeragroup.Result.Loading -> {
                        itemBinding.progressCircularRevenue.visible(true)
                        itemBinding.progressCircularRevenue.background = null
                    }
                    is com.example.seeragroup.Result.Failure -> {
                        itemBinding.progressCircularRevenue.visible(false)
                        emptyError?.invoke()
                        resetValuesScrolling(viewModel)
                    }
                }
            }
        }
    }


    var moviesList = arrayListOf<List<Movie>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LIST_MOVIES_POPULAR -> ListOfPopularMoviesViewHolder(
                MoviesPopularBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            TYPE_LIST_MOVIES_TOP_RATED -> ListOfTopRatedMoviesViewHolder(
                MoviesTopRatedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TYPE_LIST_MOVIES_REVENUE -> ListOfRevenueMoviesViewHolder(
                MoviesRevenueBinding.inflate(
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
            is ListOfPopularMoviesViewHolder -> holder.bind(moviesList[position])
            is ListOfTopRatedMoviesViewHolder -> holder.bind(moviesList[position])
            is ListOfRevenueMoviesViewHolder -> holder.bind(moviesList[position])
        }
    }

    override fun getItemCount(): Int = moviesList.size

    override fun getItemViewType(position: Int): Int {
        return when (position) {

            0 -> TYPE_LIST_MOVIES_POPULAR
            1 -> TYPE_LIST_MOVIES_TOP_RATED
            2 -> TYPE_LIST_MOVIES_REVENUE

            else -> throw IllegalArgumentException("Invalid Item")
        }
    }

}