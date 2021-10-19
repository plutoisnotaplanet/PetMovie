package com.petmovie.view

import android.view.animation.Transformation
import androidx.recyclerview.widget.RecyclerView
import com.example.petmovie.R
import com.example.petmovie.databinding.ItemMovieBinding
import com.petmovie.entity.Movie
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class MovieViewHolder(private val binding: ItemMovieBinding) :
RecyclerView.ViewHolder(binding.root) {

    val transformation: com.squareup.picasso.Transformation

    init {
        val dimension = itemView.resources.getDimension(R.dimen.cornerRad)
        val cornerRadius = dimension.toInt()
        transformation = RoundedCornersTransformation(cornerRadius, 0)
    }

    fun bind(movie: Movie, listener: (Movie) -> Unit) {
        setName(movie)
        setThumbnail(movie)
        setClickListener(listener, movie)
    }

    private fun setName(movie: Movie) {
        binding.movieName.text = movie.name
    }

    private fun setThumbnail(movie: Movie) {
        Picasso.get()
            .load(movie.thumbnail)
            .placeholder(R.drawable.ph_movie_grey_200)
            .error(R.drawable.ph_movie_grey_200)
            .transform(transformation)
            .fit()
            .centerCrop()
            .into(binding.movieThumbnail)
    }

    private fun setClickListener(
        listener: (Movie) -> Unit,
        movie: Movie
    ) {
        itemView.setOnClickListener { listener(movie) }
    }
}