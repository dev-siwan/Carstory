package com.like.drive.carstory.ui.board.upload.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.photo.PhotoData
import com.like.drive.carstory.ui.board.upload.holder.BoardUploadPhotoHolder
import com.like.drive.carstory.ui.board.upload.viewmodel.UploadViewModel


class BoardUploadPhotoAdapter(private val viewModel: UploadViewModel) :
    RecyclerView.Adapter<BoardUploadPhotoHolder>() {

    var photoList = arrayListOf<PhotoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardUploadPhotoHolder {
        return BoardUploadPhotoHolder.from(parent)
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holderBoard: BoardUploadPhotoHolder, position: Int) {
        holderBoard.bind(photoList[position], viewModel)
    }

    fun addItem(photoData: PhotoData) {
        photoList.add(photoData)
        notifyItemInserted(photoList.size - 1)
    }

    fun removeItem(data: PhotoData) {
        photoList.firstOrNull { it == data }?.let {
            val index = photoList.indexOf(it)
            photoList.remove(it)
            notifyItemRemoved(index)
        }
    }

}