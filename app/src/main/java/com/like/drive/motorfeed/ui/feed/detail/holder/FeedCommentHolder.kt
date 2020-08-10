package com.like.drive.motorfeed.ui.feed.detail.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.CommentWrapData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.databinding.HolderFeedCommentBinding
import com.like.drive.motorfeed.ui.feed.detail.adapter.ReCommentAdapter
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel

class FeedCommentHolder(val binding: HolderFeedCommentBinding) :
    RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private var wasPaused: Boolean = false
    var reCommentAdapter: ReCommentAdapter? = null

    fun bind(vm: FeedDetailViewModel, data: CommentWrapData) {

        reCommentAdapter = ReCommentAdapter(vm)

        binding.vm = vm
        binding.commentData = data.commentData

        binding.rvReComment.run {
            adapter = reCommentAdapter?.apply {
                reCommentList.addAll(data.reCommentList)
            }
        }

       // withViewModel(vm)
        binding.executePendingBindings()
    }

    private fun withViewModel(vm: FeedDetailViewModel) {
        with(vm) {
            //reCommentData()
        }
    }

    /*private fun FeedDetailViewModel.reCommentData() {
        reCommentEvent.observe(this@FeedCommentHolder, Observer {reComment->
            {
                    reCommentAdapter?.addReComment(reCommentWrapData.reCommentData)
            }
        })
    }*/

    fun lifeCycleCreated() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun lifeCycleAttach() {
        if (wasPaused) {
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
            wasPaused = false
        } else {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }

    fun lifeCycleDetach() {
        wasPaused = true
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun lifeCycleDestroyed() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry


    companion object {
        fun from(parent: ViewGroup): FeedCommentHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderFeedCommentBinding.inflate(layoutInflater, parent, false)

            return FeedCommentHolder(binding)
        }
    }
}