package com.like.drive.motorfeed.ui.upload.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.ActivityUploadBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.gallery.activity.GalleryActivity
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import com.like.drive.motorfeed.ui.upload.adapter.UploadPhotoAdapter
import com.like.drive.motorfeed.ui.upload.viewmodel.UploadViewModel
import com.like.drive.motorfeed.util.photo.PickImageUtil
import kotlinx.android.synthetic.main.activity_upload.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UploadActivity : BaseActivity<ActivityUploadBinding>(R.layout.activity_upload) {

    private val viewModel: UploadViewModel by viewModel()
    private val uploadAdapter by lazy { UploadPhotoAdapter(this,viewModel) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        withViewModel()
        tvSelectMotor.setOnClickListener {
            startActForResult(SelectMotorTypeActivity::class, SelectMotorTypeActivity.REQUEST_CODE)
        }

    }

    override fun onBinding(dataBinding: ActivityUploadBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvPhotos.adapter = uploadAdapter

    }


    private fun withViewModel() {
        with(viewModel) {
            pickPhoto()
            photoItemClick()
        }
    }

    /**
     * 사진 선택
     */

    private fun UploadViewModel.pickPhoto() {
        selectPhotoClickEvent.observe(this@UploadActivity, Observer {
            showListDialog(
                resources.getStringArray(R.array.empty_photo_type_array),
                getString(R.string.select_photo)
            ) { position ->
                when (position) {
                    PhotoSelectType.CAMERA.ordinal -> showCamera()
                    PhotoSelectType.ALBUM.ordinal -> checkStoragePermission()
                }
            }
        })
    }


    private fun UploadViewModel.photoItemClick() {
        photoItemClickEvent.observe(this@UploadActivity, Observer {
            showListDialog(
                resources.getStringArray(R.array.pick_photo_menu),
                getString(R.string.select_photo)
            ) { position ->
                when (position) {
                    0 -> viewModel.deletePhoto(it)
                }
            }
        })
    }

    private fun checkStoragePermission() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    showCustomGallery()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }
            }).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .setDeniedMessage(getString(R.string.denied_permission_storage))
            .check()
    }


    private fun showCamera() {
        PickImageUtil.pickFromCamera(this)
    }

    private fun showCustomGallery() {
        startActForResult(GalleryActivity::class, PickImageUtil.PICK_FROM_ALBUM, Bundle().apply {
            putBoolean(GalleryActivity.KEY_SHOW_DIRECTORY, true)
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SelectMotorTypeActivity.REQUEST_CODE -> {

                    data?.getParcelableExtra<MotorTypeData>(SelectMotorTypeActivity.RESULT_KEY)
                        ?.let {
                            tvSelectMotor.text = "브랜드:${it.brandName} / 모델:${it.modelName} "
                        }
                }

                PickImageUtil.PICK_FROM_CAMERA -> {
                    PickImageUtil.getImageFromCameraPath()?.let { path ->
                        viewModel.setPath(PickImageUtil.setImage(path))
                    }
                }

                PickImageUtil.PICK_FROM_ALBUM -> {
                    data?.getParcelableArrayListExtra<Uri>(GalleryActivity.KEY_SELECTED_GALLERY_ITEM)
                        ?.let {
                            viewModel.setPath(it)
                        }
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        uploadAdapter.lifeCycleDestroyed()
    }

    companion object {
        const val REQ_REG_PHOTO = 1002
        const val KEY_UPLOADED_KEYS = "KEY_UPLOADED_KEYS"
    }
}

enum class PhotoSelectType {
    CAMERA,
    ALBUM
}