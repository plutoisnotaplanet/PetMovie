package com.petmovie

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.example.petmovie.R
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

@BindingAdapter("thumbnail")
fun setImageUrl(imageView: ImageView, url: String?) {
    val transformation: com.squareup.picasso.Transformation
    val dimension = imageView.resources.getDimension(R.dimen.cornerRad)
    val cornerRadius = dimension.toInt()
    transformation = RoundedCornersTransformation(cornerRadius, 0)
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.ph_movie_grey_200)
        .error(R.drawable.ph_movie_grey_200)
        .transform(transformation)
        .fit()
        .centerCrop()
        .into(imageView)
    imageView.updateLayoutParams<ConstraintLayout.LayoutParams> {
        dimensionRatio = "H, 3:4"
    }
}
