package com.like.drive.carstory.ui.board.detail.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.databinding.HolderDetailPhotoHolderBinding
import com.like.drive.carstory.ui.board.detail.viewmodel.BoardDetailViewModel

class DetailPhotoHolder(val binding: HolderDetailPhotoHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: BoardDetailViewModel, imgUrl: String?) {
        binding.vm = vm
        binding.imgUrl = imgUrl
    }

    companion object {
        fun from(parent: ViewGroup): DetailPhotoHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderDetailPhotoHolderBinding.inflate(layoutInflater, parent, false)

            return DetailPhotoHolder(binding)
        }
    }
}