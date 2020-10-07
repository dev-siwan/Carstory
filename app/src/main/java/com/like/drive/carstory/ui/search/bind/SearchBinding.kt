package com.like.drive.carstory.ui.search.bind

import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.like.drive.carstory.R
import com.like.drive.carstory.ui.search.adapter.RecentlyListAdapter
import com.like.drive.carstory.ui.search.data.RecentlyData

@BindingAdapter("recentlyList")
fun RecyclerView.setRecentlyList(list: List<RecentlyData>?) {
    list?.let {
        (adapter as RecentlyListAdapter).run {
            submitList(it)
            notifyDataSetChanged()
        }
    }
}

@BindingAdapter("searchViewStatus")
fun EditText.setSearchViewStatus(isStatus:Boolean){

    val params = layoutParams as ConstraintLayout.LayoutParams
    if(isStatus){
        background = null
        params.width = 0
    }else{
        background = ContextCompat.getDrawable(context, R.drawable.rect_solid_white_stroke_black_conner_8dp)
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}

@BindingAdapter("scrollViewStatus")
fun AppBarLayout.setScrollViewStatus(isStatus:Boolean){
    if(isStatus){
        setBackgroundColor(ContextCompat.getColor(context,R.color.white_100))
    }else{
        setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent))
    }
}
