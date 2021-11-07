package com.petmovie.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petmovie.R
import com.example.petmovie.databinding.ItemMovieBinding
import com.petmovie.entity.Movie
import com.petmovie.viewmodel.MoviesViewModel

class BottomSheetAdapter (
    private val similarMovies: List<Movie>,
    private val needName: Boolean,
    private val onClickListener: MoviesFragment.onClickListener
): RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(needName , binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(similarMovies.get(position))
        holder.itemView.setOnClickListener {
            onClickListener.onClick(similarMovies.get(position))
        }
    }

    override fun getItemCount(): Int {
        return similarMovies.size
    }


}