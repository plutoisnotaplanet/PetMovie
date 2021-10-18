package com.petmovie.viewmodel

import com.petmovie.entity.Movie

sealed class QueryResult
data class ValidResult(val result: List<Movie>) : QueryResult()
object EmptyResult : QueryResult()
object EmptyQuery : QueryResult()
data class ErrorResult(val e: Throwable) : QueryResult()
object TerminalError : QueryResult()