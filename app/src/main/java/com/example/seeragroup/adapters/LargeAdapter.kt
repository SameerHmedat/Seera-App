package com.example.seeragroup.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seeragroup.Constants
import com.example.seeragroup.Constants.TYPE_LIST_MOVIES
import com.example.seeragroup.databinding.MoviesBinding
import com.example.seeragroup.models.Movie
import com.example.seeragroup.models.Section
import com.example.seeragroup.utils.visible


class LargeAdapter(var moviesList : ArrayList<Section>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onMovieClick: ((Movie) -> Unit)? = null
    var scrolling: ((Int, Int, Int, String) -> Unit)? = null


    inner class ListMoviesViewHolder(private val itemBinding: MoviesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        private val moviesLayoutManager = LinearLayoutManager(
            itemBinding.rvMovies.context,
            LinearLayoutManager.HORIZONTAL,
            false)

        init {
            itemBinding.rvMovies.layoutManager = moviesLayoutManager
            itemBinding.rvMovies.setHasFixedSize(false)
        }


        @SuppressLint("NotifyDataSetChanged")
        fun bind(section: Section) {
            itemBinding.txtCategory.text = section.id

            val movieAdapter = MovieAdapter(movies = section.movies)
            itemBinding.rvMovies.adapter = movieAdapter

            (itemBinding.rvMovies.adapter as? MovieAdapter)?.onMovieClicked = {
                onMovieClick?.invoke(it)
            }

            itemBinding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = moviesLayoutManager.childCount
                    val totalItemCount = moviesLayoutManager.itemCount
                    val firstVisibleItem = moviesLayoutManager.findFirstVisibleItemPosition()
                    scrolling?.invoke(visibleItemCount, totalItemCount, firstVisibleItem, section.id)
                    if (Constants.loading) {
                        itemBinding.progressCircularMovies.visible(true)
                        itemBinding.progressCircularMovies.background = null
                    } else {
                        itemBinding.progressCircularMovies.visible(false)
                    }
                }
            })
        }
    }


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
        return when (moviesList[position].id) {
            "Popular" -> TYPE_LIST_MOVIES
            "Top Rated" -> TYPE_LIST_MOVIES
            "Revenue" -> TYPE_LIST_MOVIES
            else -> throw IllegalArgumentException("Invalid Item")
        }
    }

}