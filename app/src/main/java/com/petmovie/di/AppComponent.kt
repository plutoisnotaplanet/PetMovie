package com.petmovie.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.petmovie.BuildConfig
import com.petmovie.Navigator
import com.petmovie.network.MoviesApi
import com.petmovie.repo.MoviesRepository
import com.petmovie.viewmodel.MovieViewModelImpl
import com.petmovie.viewmodel.MoviesViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppComponent(appContext: Context) {

    private val movieRepo: MoviesRepository


    init {

        val api = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesApi::class.java)

        movieRepo = MoviesRepository(api)
    }

    internal fun getMoviesViewModel(fragment: Fragment) : MoviesViewModel {
        return ViewModelProvider(fragment, MovieViewModelImpl.Factory(movieRepo))
            .get(MoviesViewModel::class.java)
    }

}