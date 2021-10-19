package com.petmovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.petmovie.entity.Movie
import kotlinx.coroutines.channels.SendChannel

abstract class MoviesViewModel: ViewModel() {

    abstract val queryChannel: SendChannel<String>

    abstract val searchResult: LiveData<MoviesResult>
    abstract val searchState: LiveData<SearchState>

    abstract fun onMovieAction(movie: Movie)
}