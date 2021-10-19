package com.petmovie.viewmodel

import com.petmovie.entity.Movie

sealed class MoviesResult
data class ValidResult(val result: List<Movie>) : MoviesResult()
object EmptyResult : MoviesResult()
object EmptyQuery : MoviesResult()
data class ErrorResult(val e: Throwable) : MoviesResult()
object TerminalError : MoviesResult()