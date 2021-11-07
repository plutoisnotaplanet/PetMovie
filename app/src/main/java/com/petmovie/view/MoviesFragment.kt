package com.petmovie.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petmovie.R
import com.example.petmovie.databinding.FragmentMoviesBinding
import com.petmovie.PetMovie
import com.petmovie.entity.Movie
import com.petmovie.utils.afterTextChanged
import com.petmovie.utils.hideKeyboard
import com.petmovie.viewmodel.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class MoviesFragment : Fragment() {

    companion object {

        fun newInstance() = MoviesFragment()

    }

    private var listener: onFragmentInteractionListener? = null

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MoviesViewModel
    private lateinit var topMoviesAdapter: TopMoviesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is onFragmentInteractionListener) {
            listener = context
        } else {
            throw IllegalArgumentException("${context.toString()} must implement onFragmentInteractionListener")
        }
    }

    private fun getMovieDetails(movie: Movie) {
            if (movie != null) {
                listener?.openBottom(movie)
            }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity()!!.application as PetMovie).myComponent.getMoviesViewModel(this)

        binding.moviesList.apply {
            val spanCount =
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> 4
                    else -> 2
                }
            layoutManager = GridLayoutManager(activity, spanCount)

            moviesAdapter = MoviesAdapter(true , onClickListener { movie ->
                viewModel.onMovieAction(movie)
            })
            adapter = moviesAdapter

            addItemDecoration(GridSpacingitemDecoration(spanCount, resources.getDimension(R.dimen.itemsDist).toInt(), true, true))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        recyclerView.hideKeyboard()
                    }
                }
            } )
        }

        binding.topMovies.apply {
            layoutManager = object : GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                    lp?.width = width/4
                    return true
                }
            }

            topMoviesAdapter = TopMoviesAdapter(false, onClickListener { movie ->
                viewModel.onMovieAction(movie)
            })
            adapter = topMoviesAdapter

            addItemDecoration(GridSpacingitemDecoration(1, resources.getDimension(R.dimen.itemsDist).toInt(), true, false))
            val result = resources.getDimension(R.dimen.itemsDist).toInt()
            Log.e("LOLOOLOL","this shit is $result")
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        recyclerView.hideKeyboard()
                    }
                }
            } )
        }

        if (savedInstanceState == null) {
            lifecycleScope.launch {
                viewModel.queryChannel.send("")
            }
        }
        binding.searchInput.afterTextChanged {
            lifecycleScope.launch {
                viewModel.queryChannel.send(it.toString())
            }
        }

//        viewModel.searchResult.observe(viewLifecycleOwner,::handleMoviesListResult)
//        viewModel.searchState.observe(viewLifecycleOwner, ::handleLoadingState)
        viewModel.topMoviesResult.observe(viewLifecycleOwner,::handleTopMoviesListResult)
        viewModel.movieForBottom.observe(viewLifecycleOwner, ::getMovieDetails)
    }

    private fun handleTopMoviesListResult(result: MoviesResult) {
        when (result) {
            is ValidResult -> {
                binding.moviesList.visibility = View.GONE
                binding.topMovies.visibility = View.VISIBLE
                topMoviesAdapter.submitList(result.result)
            }
        }
    }

    private fun handleLoadingState(state: SearchState) {
        when (state) {
            Loading -> {
                binding.searchIcon.visibility = View.GONE
                binding.searchProgress.visibility = View.INVISIBLE
            }
            Ready -> {
                binding.searchIcon.visibility = View.VISIBLE
                binding.searchProgress.visibility = View.GONE
            }
        }
    }

    private fun handleMoviesListResult(result: MoviesResult) {
        when (result) {
            is ValidResult -> {
                binding.moviesList.visibility = View.VISIBLE
                binding.topMovies.visibility = View.GONE
                moviesAdapter.submitList(result.result)
            }
            is ErrorResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesList.visibility = View.GONE
                Log.e(MoviesFragment::class.java.name, "Something went wrong.", result.e)
            }
            is EmptyResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesList.visibility = View.GONE
            }
            is EmptyQuery -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesList.visibility = View.GONE
                binding.topMovies.visibility = View.VISIBLE
            }
            is TerminalError -> {
                println("Our Flow terminated! JUST RUN!")
                Toast.makeText(activity,R.string.error_unknown_on_download, Toast.LENGTH_SHORT).show()
            }
        }
    }

    class onClickListener(val clickListener: (movie: Movie) -> Unit) {
        fun onClick(movie: Movie) = clickListener(movie)
    }

    interface onFragmentInteractionListener {
        fun openBottom(movie: Movie)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }


}