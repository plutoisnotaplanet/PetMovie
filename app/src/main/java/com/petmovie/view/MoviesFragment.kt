package com.petmovie.view

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petmovie.R
import com.example.petmovie.databinding.FragmentMoviesBinding
import com.petmovie.PetMovie
import com.petmovie.utils.afterTextChanged
import com.petmovie.utils.hideKeyboard
import com.petmovie.viewmodel.*
import kotlinx.coroutines.launch


class MoviesFragment : Fragment() {

    companion object {

        fun newInstance() = MoviesFragment()

    }

    private var _binding: FragmentMoviesBinding? = null

    private val binding get() = _binding!!

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MoviesViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

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

            moviesAdapter = MoviesAdapter(viewModel::onMovieAction)
            adapter = moviesAdapter

            addItemDecoration(GridSpacingitemDecoration(spanCount, resources.getDimension(R.dimen.itemsDist).toInt(), true))
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

        viewModel.searchResult.observe(viewLifecycleOwner,::handleMoviesListResult)
        viewModel.searchState.observe(viewLifecycleOwner, ::handleLoadingState)
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
                binding.moviesPlaceholder.visibility = View.GONE
                binding.moviesList.visibility = View.VISIBLE
                moviesAdapter.submitList(result.result)
            }
            is ErrorResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.GONE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.search_error)
                Log.e(MoviesFragment::class.java.name, "Something went wrong.", result.e)
            }
            is EmptyResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.VISIBLE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.empty_result)
            }
            is EmptyQuery -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.VISIBLE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.movies_placeholder)
            }
            is TerminalError -> {
                println("Our Flow terminated! JUST RUN!")
                Toast.makeText(activity,R.string.error_unknown_on_download, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}