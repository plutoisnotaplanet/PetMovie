package com.petmovie

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.petmovie.entity.Movie

class Navigator( private val context: Context) {

//    fun navigateTo(url: String): Boolean {
//        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//        browserIntent.flags = browserIntent.flags or Intent.FLAG_ACTIVITY_NEW_TASK
//        try {
//            context.startActivity(browserIntent)
//            return true
//        } catch (e: ActivityNotFoundException) {
//            return false
//        }
//    }

    val _navigateToDetailMovie = MutableLiveData<Movie>()
    val navigateToDetailMovie: LiveData<Movie> get() = _navigateToDetailMovie

}