package com.petmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.petmovie.R
import com.petmovie.entity.Movie
import com.petmovie.view.BottomSheetFragment
import com.petmovie.view.MoviesFragment
import timber.log.Timber

class MainActivity : AppCompatActivity(), MoviesFragment.onFragmentInteractionListener {

    private var bottomSheetFragment: BottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MoviesFragment.newInstance())
                .commitNow()
        }
    }


    override fun openBottom(movie: Movie) {
        if (bottomSheetFragment == null) {
            bottomSheetFragment = BottomSheetFragment.newInstance(movie)
            bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment?.tag)
        } else {
            Log.e("openBottomTry", "im trying to open this one ${movie.name}")
            bottomSheetFragment?.updateContent(movie)
        }
    }

}