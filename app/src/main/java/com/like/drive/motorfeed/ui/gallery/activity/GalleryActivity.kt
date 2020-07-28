package com.like.drive.motorfeed.ui.gallery.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityGalleryBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.getCurrentFragmentClassName
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.gallery.data.GalleryDirectoryData
import com.like.drive.motorfeed.ui.gallery.fragment.GalleryDirectoryFragment
import com.like.drive.motorfeed.ui.gallery.fragment.GalleryDirectoryFragmentDirections
import com.like.drive.motorfeed.ui.gallery.fragment.GalleryFragment
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel
import com.like.drive.motorfeed.ui.upload.adapter.UploadPhotoAdapter
import kotlinx.android.synthetic.main.layout_custom_toolbar.*


import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity :
    BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery) {

    companion object {
        const val KEY_SELECTED_GALLERY_ITEM = "KEY_SELECTED_GALLERY_ITEM"
        const val KEY_SHOW_DIRECTORY = "KEY_SHOW_DIRECTORY"
    }

    private val toolbar by lazy { findViewById<Toolbar>(R.id.incToolbar) }
    private val galleryViewModel: GalleryViewModel by viewModel()
    private val navController by lazy {
        Navigation.findNavController(this, R.id.customGalleryNavFragment)
    }
    private var showDirectory = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        initObserver()
    }


    override fun onBackPressed() {
        if (navController.getCurrentFragmentClassName() == GalleryFragment::class.java.name) {
            if (showDirectory) navController.popBackStack() else finish()
        } else {
            finish()
        }
    }

    override fun setBackButtonToolbar(toolbar: Toolbar, title: String, action: () -> Unit) {
        super.setBackButtonToolbar(toolbar, title, action)
        tvToolbarTitle?.text = title
    }

    override fun setCloseButtonToolbar(toolbar: Toolbar, title: String, action: () -> Unit) {
        super.setCloseButtonToolbar(toolbar, title, action)
        tvToolbarTitle?.text = title
    }

    private fun initData() {
        showDirectory = intent?.getBooleanExtra(KEY_SHOW_DIRECTORY, false) ?: false
    }

    private fun initObserver() {
        with(galleryViewModel) {
            completeBringInitData()
            complete()
            clickDirectory()
            showNotAvailableMimeType()
            showLoading()
        }
    }

    private fun initView() {
        if (!showDirectory) {
            moveToGalleryFragment()
        }
        navController.apply {
            addOnDestinationChangedListener { _, _, _ ->
                when (getCurrentFragmentClassName()) {
                    GalleryDirectoryFragment::class.java.name -> setBackButtonToolbar(toolbar, getString(R.string.select_gallery_directory)) { finish() }
                    GalleryFragment::class.java.name -> setBackButtonToolbar(toolbar, galleryViewModel.getSelectedDirectoryTitle()) { if (showDirectory) popBackStack() else finish() }
                }
            }
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

    private fun GalleryViewModel.clickDirectory() {
        selectedDirectory.observe(this@GalleryActivity, Observer {
            //폴더 보여주지 않으면, 초기 진입 시 GalleryFragment 로 이동하므로, 폴더 선택 시에만 이동하도록 처리
            if (showDirectory) {
                moveToGalleryFragment()
            }
        })
    }

    private fun GalleryViewModel.showNotAvailableMimeType() {
        notAvailablePhoto.observe(this@GalleryActivity, Observer {
            it?.let { resId -> showShortToast(getString(resId)) }
        })
    }

    private fun GalleryViewModel.completeBringInitData() {
        originGalleryData.observe(this@GalleryActivity, Observer {
            when {
                showDirectory -> getGalleryDirectory()
                // 폴더 보여주지 않고 바로 사진화면으로 넘길 경우 전체 사진으로 보여주기 위해 설정
                else -> {
                    setSelectedDirectory(GalleryDirectoryData(getString(R.string.select_photo), null))
                    tvToolbarTitle.text = getString(R.string.select_photo)
                }
            }
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

    private fun moveToGalleryFragment() {
        val directions = GalleryDirectoryFragmentDirections.actionGalleryDirectoryFragmentToGalleryFragment()
        if (navController.currentDestination?.id == R.id.galleryDirectoryFragment) {
            navController.navigate(directions)
        }
    }

}