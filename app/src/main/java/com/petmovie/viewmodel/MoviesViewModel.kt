package com.petmovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.petmovie.entity.Movie
import kotlinx.coroutines.channels.SendChannel

abstract class MoviesViewModel: ViewModel() {

    abstract val queryChannel: SendChannel<String>

    abstract val searchResult: LiveData<MoviesResult>
    abstract val searchState: LiveData<SearchState>

    abstract val _topMoviesResult: MutableLiveData<MoviesResult>
    abstract val topMoviesResult: LiveData<MoviesResult>

    abstract val _navigateToDetailMovie:MutableLiveData<Movie>
    abstract val navigateToDetailMovie: LiveData<Movie>


    abstract fun onMovieAction(movie: Movie)
}