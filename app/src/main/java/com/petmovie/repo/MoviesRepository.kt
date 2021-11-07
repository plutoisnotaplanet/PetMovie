package com.petmovie.repo

import android.util.Log
import com.petmovie.entity.Movie
import com.petmovie.network.MoviesApi
import com.example.petmovie.BuildConfig
import com.petmovie.entity.asMovie
import com.petmovie.network.MovieNetworkModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.http.Query

class MoviesRepository(private val moviesApi: MoviesApi) {

    internal suspend fun searchMovies(query: String, page: Int = 1): List<Movie> {
        return withContext(Dispatchers.IO) {
            flowOf(
                moviesApi.searchMovie(BuildConfig.API_KEY, query, page)
            )
        }
            .flowOn(Dispatchers.IO)
            .onEach { Log.d(MoviesRepository::class.java.name, it.movies.toString()) }
            .flatMapMerge { it.movies.asFlow() }
            .map { Movie(it.id, it.title, getPosterUrl(it),it.overview,
                it.voteAverage, it.budget, it.releasedDate) }
            .toList()

    }

    internal suspend fun topMovies(page: Int = 1) : List<Movie> {
        return withContext(Dispatchers.IO) {
            flowOf(
                moviesApi.topMovie(BuildConfig.API_KEY,page)
            )
        }
            .flowOn(Dispatchers.IO)
            .onEach { Log.d(MoviesRepository::class.java.name, it.movies.toString()) }
            .flatMapMerge { it.movies.asFlow() }
            .map { Movie(it.id, it.title, getPosterUrl(it),it.overview,
                it.voteAverage, it.budget, it.releasedDate) }
            .toList()
    }

    internal suspend fun detailMovie(movieId: Int, page: Int = 1) : Movie {
        return withContext(Dispatchers.IO) {
            moviesApi.detailMovie(movieId, BuildConfig.API_KEY).asMovie()

        }
    }

    internal suspend fun similarMovies(movieId: Int, page: Int = 1) : List<Movie> {
        return withContext(Dispatchers.IO) {
            flowOf(moviesApi.similarMovie(movieId, BuildConfig.API_KEY, page))
        }
            .flowOn(Dispatchers.IO)
            .onEach { Log.d("DOWNLOADED", it.movies.toString()) }
            .flatMapMerge { it.movies.asFlow() }
            .map { Movie(it.id, it.title, getPosterUrl(it),it.overview,
                it.voteAverage, it.budget, it.releasedDate) }
            .toList()
    }


    private fun getPosterUrl(it: MovieNetworkModel) = "${BuildConfig.BASE_IMAGE_URL}${it.posterPath}"
}