package com.example.seeragroup.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.seeragroup.Constants.IMAGE_BASE
import com.example.seeragroup.R
import com.example.seeragroup.models.Movie
import com.example.seeragroup.utils.loadImage
import com.example.seeragroup.utils.visible
import kotlinx.android.synthetic.main.movie_item_row.view.*

@Suppress("DEPRECATION")
class MovieAdapter (private val movies:ArrayList<Movie>)
    :RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    var onItemClick: ((Movie) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(
            R.layout.movie_item_row,
            parent,false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newMovies: List<Movie>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bindMovie(movie: Movie){
            if(movie.poster==null){
                itemView.img_movie.loadImage(R.drawable.comingsoon)
            }
            else{itemView.img_movie.loadImage(IMAGE_BASE+movie.poster)}
        }
        init {
            itemView.img_movie.setOnClickListener {
                onItemClick?.invoke(movies[adapterPosition])
            }}
    }

}

