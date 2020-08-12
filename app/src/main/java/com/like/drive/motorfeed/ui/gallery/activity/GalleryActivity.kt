package com.like.drive.motorfeed.ui.gallery.activity


import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityGalleryBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.dpToPixel
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.gallery.adapter.GalleryAdapter
import com.like.drive.motorfeed.ui.gallery.fragment.GalleryDirectoryFragment
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.layout_custom_toolbar.tvToolbarTitle
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity :
    BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery) {

    companion object {
        const val KEY_SELECTED_GALLERY_ITEM = "KEY_SELECTED_GALLERY_ITEM"
        const val KEY_PICK_PHOTO_COUNT = "KEY_PICK_PHOTO_COUNT"
        const val KEY_PHOTO_MAX_SIZE = "KEY_PHOTO_MAX_SIZE"
        const val KEY_IS_MULTIPLE_PICK="KEY_IS_MULTIPLE_PICK"
    }

    private val toolbar by lazy { findViewById<Toolbar>(R.id.incToolbar) }
    private val galleryViewModel: GalleryViewModel by viewModel()

    private val galleryAdapter by lazy { GalleryAdapter(galleryViewModel) }
    private val directoryDialog by lazy{ GalleryDirectoryFragment() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        initObserver()
    }


    override fun onBinding(dataBinding: ActivityGalleryBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = galleryViewModel
        rvGallery.adapter = galleryAdapter
    }

    private fun initData() {
        galleryViewModel.run {
            intent.getBooleanExtra(KEY_IS_MULTIPLE_PICK, false).let {
                initMultiple(it)
                if (it) {
                    initCount(
                        intent.getIntExtra(KEY_PHOTO_MAX_SIZE, 0),
                        intent.getIntExtra(KEY_PICK_PHOTO_COUNT, 0)
                    )
                }
            }

        }
    }

    private fun initObserver() {
        with(galleryViewModel) {
            complete()
            clickDirectory()
            showNotAvailableMimeType()
            showLoading()
            selectDirectoryClickEvent()
            addData()
            removeData()
            singleImgComplete()
        }
    }


    private fun initView() {

        tvToolbarTitle.text = getString(R.string.all)
        setCloseButtonToolbar(toolbar) { finish() }

        rvGallery.run {
            addItemDecoration(GridSpacingItemDecoration(3, dpToPixel(3.0f).toInt(), true))
        }
    }


    private fun GalleryViewModel.complete() {
        completeClickEvent.observe(this@GalleryActivity, Observer {
            Intent().apply { putExtra(KEY_SELECTED_GALLERY_ITEM, getSelectedGalleryItem()) }.run {
                setResult(Activity.RESULT_OK, this)
            }
            finish()
        })
    }


    private fun GalleryViewModel.singleImgComplete() {
        singleUri.observe(this@GalleryActivity, Observer {
            Intent().apply { putExtra(KEY_SELECTED_GALLERY_ITEM, it) }.run {
                setResult(Activity.RESULT_OK, this)
            }
            finish()
        })
    }


    private fun GalleryViewModel.addData() {
        addDataEvent.observe(this@GalleryActivity, Observer {
            galleryAdapter.addItem(it)
        })
    }

    private fun GalleryViewModel.removeData() {
        removeDataEvent.observe(this@GalleryActivity, Observer {
            galleryAdapter.removeItem(it)
        })
    }


    private fun GalleryViewModel.clickDirectory() {
        selectedDirectory.observe(this@GalleryActivity, Observer {

            tvToolbarTitle.text = it.display
            galleryAdapter.bringGalleryItem(it.value)

            if (directoryDialog.isVisible) {
                directoryDialog.dismiss()
            }

        })

    }

    private fun GalleryViewModel.selectDirectoryClickEvent() {
        selectDirectoryClickEvent.observe(this@GalleryActivity, Observer {
            directoryDialog.show(supportFragmentManager,"")
        })

    }

    private fun GalleryViewModel.showNotAvailableMimeType() {
        notAvailablePhoto.observe(this@GalleryActivity, Observer {
            it?.let { resId -> showShortToast(getString(resId)) }
        })
    }


    private fun GalleryViewModel.showLoading() {
        isLoading.observe(this@GalleryActivity, Observer {
            when (it) {
                true -> if (!loadingProgress.isShowing) loadingProgress.show()
                else -> loadingProgress.dismiss()
            }
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