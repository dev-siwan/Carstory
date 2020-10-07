package com.like.drive.carstory.ui.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.HolderGalleryBinding
import com.like.drive.carstory.ui.gallery.data.GalleryItemData
import com.like.drive.carstory.ui.gallery.holder.GalleryHolder
import com.like.drive.carstory.ui.gallery.viewmodel.GalleryViewModel

class GalleryAdapter(val viewModel: GalleryViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var galleryListData = mutableListOf<GalleryItemData>()
    var allGalleryData: List<GalleryItemData> = emptyList()
    private val selectItemList = mutableListOf<GalleryItemData>()

    fun init(data: List<GalleryItemData>) {
        allGalleryData = data
        galleryListData.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HolderGalleryBinding>(
            inflater,
            R.layout.holder_gallery,
            parent,
            false
        )
        return GalleryHolder(binding, viewModel)
    }

    override fun getItemCount(): Int = galleryListData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GalleryHolder).bind(galleryListData[position])
    }

    fun bringGalleryItem(directoryName: String?) {
        if (galleryListData.isNotEmpty()) {

            galleryListData = (directoryName?.let { directory ->
                allGalleryData.filter { it.directory == directory }
            } ?: allGalleryData).toMutableList()

            notifyDataSetChanged()
        }
    }

    fun addItem(itemData: GalleryItemData) {
        itemData.run {
            selectItemList.add(this)
            selected.set(!itemData.selected.get())
            index.set(selectItemList.size)
        }
    }

    fun removeItem(itemData: GalleryItemData) {
        itemData.run {
            selectItemList.remove(this)
            selected.set(!itemData.selected.get())
        }
        replaceItemPosition(itemData.index.get())
    }


    private fun replaceItemPosition(index: Int) {
        selectItemList.filter { it.index.get() > index }.forEach {
            val indexOf = it.index.get()
            if (indexOf > 1) {
                it.index.set(indexOf - 1)
            }
        }
    }
}