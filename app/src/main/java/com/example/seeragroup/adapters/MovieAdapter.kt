package com.example.seeragroup.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seeragroup.Constants.IMAGE_BASE
import com.example.seeragroup.R
import com.example.seeragroup.models.Movie
import com.example.seeragroup.utils.loadImage
import kotlinx.android.synthetic.main.movie_item_row.view.*

@Suppress("DEPRECATION")
class MovieAdapter(val movies: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var onMovieClicked: ((Movie) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_item_row,
            parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    fun updateData(newMovies: List<Movie>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindMovie(movie: Movie) {
            if (movie.poster == null) {
                itemView.img_movie.loadImage(R.drawable.comingsoon)
            } else {
                itemView.img_movie.loadImage(IMAGE_BASE + movie.poster)
            }
        }

        init {
            itemView.img_movie.setOnClickListener {
                onMovieClicked?.invoke(movies[adapterPosition])
            }
        }
    }

}

