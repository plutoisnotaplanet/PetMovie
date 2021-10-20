package com.petmovie.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.petmovie.Navigator
import com.petmovie.entity.Movie
import com.petmovie.repo.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import kotlin.coroutines.coroutineContext

private const val DEBOUNCE_DELAY_TIME_MS = 500L

class MovieViewModelImpl( val moviesRepository: MoviesRepository, val navigator: Navigator) : MoviesViewModel() {

    override val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    override val searchState = MutableLiveData<SearchState>()

    override val _topMoviesResult = MutableLiveData<MoviesResult>()

    override val topMoviesResult: LiveData<MoviesResult>
        get() = _topMoviesResult

    init {
       viewModelScope.launch {
           try {
               val result = moviesRepository.topMovies()
               if (result.isEmpty()) {
                   EmptyResult
               } else {
                   val result = ValidResult(result)
                   _topMoviesResult.value = result
               }
           }
        catch (e: Throwable) {
           if (e is CancellationException) {
               throw e
           } else {
               Log.w(MovieViewModelImpl::class.java.name, e)
               ErrorResult(e)
           }
       }
       }
   }

    override val searchResult = queryChannel
        .asFlow()
        .debounce(DEBOUNCE_DELAY_TIME_MS)
        .onEach { searchState.value = Loading }
        .mapLatest {
            if (it.isEmpty()) {
                EmptyQuery
            } else {
                try {
                    val result = moviesRepository.searchMovies(it)
                    if (result.isEmpty()) {
                        EmptyResult
                    } else {
                        ValidResult(result)
                    }
                } catch (e: Throwable) {
                    if (e is CancellationException) {
                        throw e
                    } else {
                        Log.w(MovieViewModelImpl::class.java.name, e)
                        ErrorResult(e)
                    }
                }
            }
        }
        .onEach { searchState.value = Ready }
        .catch { emit(TerminalError) }
        .asLiveData(viewModelScope.coroutineContext)



    override fun onMovieAction(movie: Movie) {
        navigator.navigateTo("\"https://www.themoviedb.org/movie/${movie.id}")
    }

    @Suppress("UNCHEKED_CAST")
    class Factory(private val repo: MoviesRepository, private val navigator: Navigator) :
            ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MovieViewModelImpl(repo, navigator) as T
        }
            }
}