package com.petmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.petmovie.R
import com.petmovie.view.MoviesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MoviesFragment.newInstance())
                .commitNow()
        }
    }
}