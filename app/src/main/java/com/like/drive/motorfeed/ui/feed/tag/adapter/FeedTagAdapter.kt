package com.like.drive.motorfeed.ui.feed.tag.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.ui.feed.tag.holder.FeedTagViewHolder
import com.like.drive.motorfeed.ui.feed.tag.viewmodel.FeedTagViewModel

class FeedTagAdapter(val viewModel: FeedTagViewModel) :RecyclerView.Adapter<FeedTagViewHolder>(){

    var tagList = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        FeedTagViewHolder.from(parent)

    override fun getItemCount()= tagList.size

    override fun onBindViewHolder(holder: FeedTagViewHolder, position: Int) {
       holder.bind(viewModel,tagList[position])
    }

    fun addTag(tag: String,action: () -> Unit, actionEquals: () -> Unit, actionMaxSize: () -> Unit) {
        when {
            tagList.contains(tag) -> actionEquals()
            tagList.size >= 5 -> actionMaxSize()
            else -> {
                tagList.add(tag)
                notifyItemInserted(tagList.size - 1)
                action()
            }
        }
    }

    fun removeTag(tag: String,action:()->Unit){
        val index = tagList.indexOf(tag)
        tagList.removeAt(index)
        notifyItemRemoved(index)
        action()
    }
}