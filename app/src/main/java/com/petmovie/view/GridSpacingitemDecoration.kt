package com.petmovie.view

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingitemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean, private val isVertical: Boolean) : RecyclerView.ItemDecoration()
 {
     override fun getItemOffsets(
         outRect: Rect,
         view: View,
         parent: RecyclerView,
         state: RecyclerView.State
     ) {
         val position = parent.getChildAdapterPosition(view)
         val column = position % spanCount

         when (isVertical) {
             true -> {
                 if (includeEdge) {
                     outRect.left = spacing - column * spacing / spanCount
                     outRect.right = (column + 1) * spacing / spanCount
                     outRect.bottom = spacing / 2
                 } else {
                     outRect.left = column * spacing / spanCount
                     outRect.right = spacing - (column + 1) * spacing / spanCount
                 }
             }
             else -> {
                 if (includeEdge) {
                     outRect.left = spacing
                     Log.e("include edge", "${outRect.left}")
//                     outRect.right = column * spacing
                 }
                 else {
//                     outRect.left = spacing
//                     outRect.right = (spacing - column) * spacing
                 }
             }
         }

     }
}