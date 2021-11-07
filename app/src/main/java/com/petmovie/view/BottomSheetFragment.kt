package com.petmovie.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petmovie.R
import com.example.petmovie.databinding.BottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.petmovie.MainActivity
import com.petmovie.PetMovie
import com.petmovie.entity.Movie
import com.petmovie.viewmodel.MoviesViewModel
import com.squareup.picasso.Picasso
import timber.log.Timber


private const val COLLAPSED_HEIGHT = 228

class BottomSheetFragment : BottomSheetDialogFragment() {


    companion object {
        fun newInstance(movie: Movie) = BottomSheetFragment().apply {
            arguments = Bundle().apply {
                putInt("id", movie.id)
                putString("name", movie.name)
                putString("url", movie.thumbnail)
                putString("description", movie.description)
                movie.rating?.let { putDouble("rating", it) }
                movie.budget?.let { putInt("budget", it) }
                putString("pubDate", movie.pubDate)
                putParcelableArrayList("movies", ArrayList(movie.similarMovies))
            }
        }
    }


    lateinit var binding: BottomSheetLayoutBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BottomSheetAdapter
    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var viewModel: MoviesViewModel
    private lateinit var mActivity: MainActivity



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val density = requireContext().resources.displayMetrics.density

        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            behavior = BottomSheetBehavior.from(sheet)

            behavior.peekHeight = (COLLAPSED_HEIGHT * density).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    with(binding) {
                        if (slideOffset > 0) {

                            layoutCollapsed.alpha = 1 - 2 * slideOffset

                            layoutExpanded.alpha = slideOffset * slideOffset
                        }
                        if (slideOffset > 0.5) {
                            layoutCollapsed.visibility = View.GONE
                            layoutExpanded.visibility = View.VISIBLE
                        }
                        if (slideOffset < 0.5 && binding.layoutExpanded.visibility == View.VISIBLE) {
                            layoutCollapsed.visibility = View.VISIBLE
                            layoutExpanded.visibility = View.INVISIBLE
                        }
                    }
                }
            })
        }
        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_layout, container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity()!!.application as PetMovie).myComponent.getMoviesViewModel(this)

        if (arguments != null) {
            binding.movieName.text = arguments?.getString("name")
            binding.movieYear.text = arguments?.getString("pubDate")
            binding.movieRate.text = arguments?.getDouble("rating").toString()
            binding.movieDescription.text = arguments?.getString("description")
            binding.movieBudget.text = arguments?.getInt("budget").toString() + " $"
            binding.movieDescriptionAgain.text = arguments?.getString("description")
            var movieThumbnail = arguments?.getString("url")
            Picasso.get()
                .load(movieThumbnail)
                .placeholder(R.drawable.ph_movie_grey_200)
                .error(R.drawable.ph_movie_grey_200)
                .fit()
                .centerCrop()
                .into(binding.movieThumbnail)
            initRecyclerView(arguments?.getParcelableArrayList("movies") ?: ArrayList())
        }
        viewModel.movieForBottom.observe(viewLifecycleOwner, ::updateContent)
    }

    fun updateContent(movie: Movie) {
            binding.movieName.text = movie.name
            binding.movieRate.text = movie.rating.toString()
            binding.movieDescription.text = movie.description
            binding.movieYear.text = movie.pubDate
            binding.movieDescriptionAgain.text = movie.description
            binding.movieBudget.text = movie.budget.toString() + " $"
            Picasso.get()
                .load(movie.thumbnail)
                .placeholder(R.drawable.ph_movie_grey_200)
                .error(R.drawable.ph_movie_grey_200)
                .fit()
                .centerCrop()
                .into(binding.movieThumbnail)
            initRecyclerView(movie.similarMovies)
            dialog.show()
    }


    private fun initRecyclerView(movies: List<Movie>) {
        adapter = BottomSheetAdapter(movies, false ,MoviesFragment.onClickListener { movie ->
            viewModel.onMovieAction(movie)
        })
        recyclerView = binding.relatedMovies
        recyclerView.adapter = adapter
        recyclerView.layoutManager = object : GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                lp?.width = width/4
                return true
            }
        }
        recyclerView.addItemDecoration(GridSpacingitemDecoration(1, 19, true, false))
    }


    inner class MyHandler {
        fun onClose(view: View) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            dialog.hide()
        }
    }
}