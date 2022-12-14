package com.example.seeragroup.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seeragroup.Constants.TYPE_LIST_MOVIES_POPULAR
import com.example.seeragroup.Constants.TYPE_LIST_MOVIES_TOP_RATED
import com.example.seeragroup.Constants.TYPE_LIST_MOVIES_UPCOMING
import com.example.seeragroup.databinding.MoviesAdapterBinding
import com.example.seeragroup.databinding.MoviesPopularBinding
import com.example.seeragroup.databinding.MoviesTopRatedBinding
import com.example.seeragroup.databinding.MoviesUpcomingBinding
import com.example.seeragroup.models.Movie


class LargeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClick: ((Movie) -> Unit)? = null


    inner class ListOfPopularMoviesViewHolder(val itemBinding: MoviesPopularBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        init {

            val popularLayoutManager = LinearLayoutManager(
                itemBinding.rvPopular.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            itemBinding.rvPopular.layoutManager = popularLayoutManager
            itemBinding.rvPopular.setHasFixedSize(true)
        }

        @SuppressLint("NotifyDataSetChanged")
        fun bind(movies:List<Movie>) {

//            itemBinding.rvPopular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    val visibleItemCount = popularLayoutManager.childCount
//                    val totalItemCount = popularLayoutManager.itemCount
//                    val firstVisibleItem = popularLayoutManager.findFirstVisibleItemPosition()
//                    if (loadingPopular) {
//                        if (totalItemCount > previousTotalPopular) {
//                            loadingPopular = false
//                            previousTotalPopular = totalItemCount
//                        }
//                    }
//                    if (!loadingPopular && (totalItemCount - visibleItemCount - 4) <= (firstVisibleItem + visibleThreshold)) {
//                        viewModel.refreshPopularData(popularPage = ++popularPage)
//                        Log.d("waled", popularPage.toString())
//                        Log.d("waled", popularLayoutManager.itemCount.toString())
//                        loadingPopular = true
//                    }
//                }
//            })
            val popularAdapter =
                MovieAdapter(
                    ArrayList(movies)
                )
            popularAdapter.onItemClick={
                onClick?.invoke(it)
            }
            itemBinding.rvPopular.adapter = popularAdapter

        }

    }

    inner class ListOfTopRatedMoviesViewHolder(val itemBinding: MoviesTopRatedBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {

            val topRatedLayoutManager = LinearLayoutManager(
                itemBinding.rvTopRated.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            itemBinding.rvTopRated.layoutManager = topRatedLayoutManager
            itemBinding.rvTopRated.setHasFixedSize(true)
        }


        fun bind(movies: List<Movie>) {
            val topRatedAdapter =
                MovieAdapter(
                    ArrayList(movies)
                )

            itemBinding.rvTopRated.adapter = topRatedAdapter

            topRatedAdapter.onItemClick={
                onClick?.invoke(it)
            }


        }
    }


    inner class ListOfUpcomingMoviesViewHolder(val itemBinding: MoviesUpcomingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        init {
            val upcomingLayoutManager = LinearLayoutManager(
                itemBinding.rvUpcoming.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            itemBinding.rvUpcoming.layoutManager = upcomingLayoutManager
            itemBinding.rvUpcoming.setHasFixedSize(true)
        }

        fun bind(movies: List<Movie>) {

            val upcomingAdapter =
                MovieAdapter(
                    ArrayList(movies)
                )
            itemBinding.rvUpcoming.adapter = upcomingAdapter

            upcomingAdapter.onItemClick={
                onClick?.invoke(it)
            }
        }
    }

    var itemsList = arrayListOf<List<Movie>>()


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
            TYPE_LIST_MOVIES_UPCOMING -> ListOfUpcomingMoviesViewHolder(
                MoviesUpcomingBinding.inflate(
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
            is ListOfPopularMoviesViewHolder -> holder.bind(itemsList[position])
            is ListOfTopRatedMoviesViewHolder -> holder.bind(itemsList[position])
            is ListOfUpcomingMoviesViewHolder -> holder.bind(itemsList[position])
        }
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getItemViewType(position: Int): Int {
        return when (position) {

            0 -> TYPE_LIST_MOVIES_POPULAR
            1-> TYPE_LIST_MOVIES_TOP_RATED
            2-> TYPE_LIST_MOVIES_UPCOMING

            else -> throw IllegalArgumentException("Invalid Item")
        }
    }

}