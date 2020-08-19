package com.like.drive.motorfeed.ui.feed.type.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.feed.type.holder.FeedTypeItemHolder
import com.like.drive.motorfeed.ui.feed.upload.viewmodel.FeedUploadViewModel

class FeedTypeAdapter(val list : List<FeedTypeData>, private val feedUploadViewModel: FeedUploadViewModel)
    :RecyclerView.Adapter<FeedTypeItemHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        FeedTypeItemHolder.from(parent)

    override fun getItemCount() =list.size

    override fun onBindViewHolder(holder: FeedTypeItemHolder, position: Int) {
       holder.bind(feedUploadViewModel,list[position])
    }

}