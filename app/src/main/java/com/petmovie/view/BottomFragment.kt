package com.petmovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.map
import com.example.petmovie.R
import com.example.petmovie.databinding.BottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.petmovie.PetMovie
import com.petmovie.entity.Movie
import com.petmovie.viewmodel.MovieViewModelImpl
import com.petmovie.viewmodel.MoviesViewModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

private const val COLLAPSED_HEIGHT = 228

class BottomFragment : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetLayoutBinding

    private lateinit var viewModel: MoviesViewModel

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomSheetLayoutBinding.bind(inflater.inflate(R.layout.bottom_sheet_layout, container))

        val args = arguments?.let {
            BottomFragmentArgs.fromBundle(it)
        }
        initViews(args?.movie)
//        viewModel = (requireActivity()!!.application as PetMovie).myComponent.getMoviesViewModel(this)
//        viewModel.navigateToDetailMovie.observe(viewLifecycleOwner, ::initViews)

        return binding.root




    }

    override fun onStart() {
        super.onStart()

        val density = requireContext().resources.displayMetrics.density

        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.peekHeight = (COLLAPSED_HEIGHT * density).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    with (binding) {
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
    }

    private fun initViews(movie: Movie) {
        viewModel.navigateToDetailMovie.map {
            binding.movieAgeRate.text = it.ageRate.toString()
            binding.movieDescription.text = it.description
            binding.movieName.text = it.name
            binding.movieRate.text = it.rate.toString()
            binding.movieYear.text = it.releasedDate.toString()
            setThumbnail(movie)
        }
    }

    private fun setThumbnail(movie: Movie) {
        Picasso.get()
            .load(movie.thumbnail)
            .placeholder(R.drawable.ph_movie_grey_200)
            .error(R.drawable.ph_movie_grey_200)
            .transform(transformation)
            .fit()
            .centerCrop()
            .into(binding.movieThumbnail)
    }

    val transformation: com.squareup.picasso.Transformation

    init {
        val dimension = resources.getDimension(R.dimen.cornerRad)
        val cornerRadius = dimension.toInt()
        transformation = RoundedCornersTransformation(cornerRadius, 0)
    }


}