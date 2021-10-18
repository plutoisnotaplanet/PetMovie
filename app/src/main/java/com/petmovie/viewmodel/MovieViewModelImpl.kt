package com.petmovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.petmovie.Navigator
import com.petmovie.entity.Movie
import com.petmovie.repo.MoviesRepository
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel

class MovieViewModelImpl( val moviesRepository: MoviesRepository, val navigator: Navigator) : MoviesViewModel() {
    override val queryChannel: SendChannel<String> = BroadcastChannel<String>(Channel.CONFLATED)

    override val searchState: LiveData<SearchState> = MutableLiveData<SearchState>()

    override val searchResult: LiveData<QueryResult> = TODO()

    override fun onMovieAction(movie: Movie) {
        TODO("Not yet implemented")
    }
}