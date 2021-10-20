package com.petmovie.repo

import android.util.Log
import com.petmovie.entity.Movie
import com.petmovie.network.MoviesApi
import com.example.petmovie.BuildConfig
import com.petmovie.network.MovieNetworkModel
import com.petmovie.network.asMovie
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
            .map { Movie(it.id, it.title, getPosterUrl(it),
                it.movieAgeRate, it.movieGenre, it.releasedDate, it.movieDescription, it.movieRate) }
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
            .map { Movie(it.id, it.title, getPosterUrl(it),
                it.movieAgeRate, it.movieGenre, it.releasedDate, it.movieDescription, it.movieRate) }
            .toList()
    }

    internal suspend fun movieDetails(movieId: Int) : Movie {
        return withContext(Dispatchers.IO) {
                moviesApi.detailMovie(movieId, BuildConfig.API_KEY)
        }.asMovie()
    }

    private fun getPosterUrl(it: MovieNetworkModel) = "${BuildConfig.BASE_IMAGE_URL}${it.posterPath}"
}