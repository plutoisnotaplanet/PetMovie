package com.petmovie.viewmodel

sealed class SearchState
object Loading: SearchState()
object Ready: SearchState()
