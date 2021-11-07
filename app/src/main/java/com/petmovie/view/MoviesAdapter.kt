package com.petmovie.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.petmovie.entity.Movie
import androidx.recyclerview.widget.ListAdapter
import com.example.petmovie.databinding.ItemMovieBinding

class MoviesAdapter(private val needName: Boolean, private val onClickListener: MoviesFragment.onClickListener) :
    ListAdapter<Movie, MovieViewHolder>(DIFF_CALLBACK){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(needName, binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onClickListener.onClick(getItem(position))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem

        }
    }
}