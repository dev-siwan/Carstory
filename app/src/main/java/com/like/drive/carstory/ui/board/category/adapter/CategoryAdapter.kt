package com.like.drive.carstory.ui.board.category.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.ui.board.category.data.CategoryData
import com.like.drive.carstory.ui.board.category.holder.CategoryItemHolder
import com.like.drive.carstory.ui.board.upload.viewmodel.UploadViewModel

class CategoryAdapter(val list : List<CategoryData>, private val uploadViewModel: UploadViewModel)
    :RecyclerView.Adapter<CategoryItemHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        CategoryItemHolder.from(parent)

    override fun getItemCount() =list.size

    override fun onBindViewHolder(holder: CategoryItemHolder, position: Int) {
       holder.bind(uploadViewModel,list[position])
    }

}