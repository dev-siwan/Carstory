package com.like.drive.motorfeed.ui.gallery.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentGalleryBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.ext.dpToPixel
import com.like.drive.motorfeed.ui.gallery.adapter.GalleryAdapter
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GalleryFragment : BaseFragment<FragmentGalleryBinding>(R.layout.fragment_gallery) {

    private val galleryViewModel: GalleryViewModel by sharedViewModel()
    private val galleryAdapter by lazy { GalleryAdapter(galleryViewModel) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initObserver()
        initData()
    }

    private fun initData() {
        binding.vm = galleryViewModel
    }

    private fun initObserver() {
        with(galleryViewModel) {
            clickDirectory()
        }
    }

    private fun initView() {
        rvGallery?.run {
            adapter = galleryAdapter
            addItemDecoration(GridSpacingItemDecoration(3, requireContext().dpToPixel(3.0f).toInt(), true))
        }
    }


    private fun GalleryViewModel.clickDirectory() {
        selectedDirectory.observe(requireActivity(), Observer {
            bringGalleryItem(it.value)
        })
    }
}

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }


}