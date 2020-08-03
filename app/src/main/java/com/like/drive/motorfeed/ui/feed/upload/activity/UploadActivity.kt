package com.like.drive.motorfeed.ui.feed.upload.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.enum.PhotoSelectType
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.ActivityUploadBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.gallery.activity.GalleryActivity
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import com.like.drive.motorfeed.ui.feed.upload.adapter.UploadPhotoAdapter
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.base.loading.UploadProgressDialog
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.upload.viewmodel.UploadViewModel
import com.like.drive.motorfeed.util.photo.PickImageUtil
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class UploadActivity : BaseActivity<ActivityUploadBinding>(R.layout.activity_upload) {

    private val viewModel: UploadViewModel by viewModel()
    private val uploadAdapter by lazy { UploadPhotoAdapter(viewModel) }
    private val uploadLoadingFragment by lazy { UploadProgressDialog() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
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
            isUploadLoading()
            uploadComplete()
            uploadError()
        }
    }

    private fun initView(){
        val divider = DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL).apply {
            ContextCompat.getDrawable(
                this@UploadActivity, R.drawable.line_solid_empty
            )?.let { setDrawable(it) }
        }
        rvPhotos.run {
            addItemDecoration(divider)
        }
    }


    /**
     * 사진 선택
     */

    private fun UploadViewModel.pickPhoto() {
        selectPhotoClickEvent.observe(this@UploadActivity, Observer {
            if (isPhotoLimitSize()) {
                showSelectPhotoList()
            } else {
                showShortToast(getString(R.string.pick_photo_limit_message))
            }
        })
    }


    /**
     * 사진 선택 리스트
     * 1.사진 2.앨범
     */

    private fun showSelectPhotoList() {
        showListDialog(
            resources.getStringArray(R.array.empty_photo_type_array),
            getString(R.string.select_photo)
        ) { position ->
            when (position) {
                PhotoSelectType.CAMERA.ordinal -> showCamera()
                PhotoSelectType.ALBUM.ordinal -> checkStoragePermission()
            }
        }
    }


    /**
     * 포토 아이템을 눌렸을 경우 메뉴 뜸
     * 1.삭제 2.크게보기
     */

    private fun UploadViewModel.photoItemClick() {
        photoItemClickEvent.observe(this@UploadActivity, Observer {
            showListDialog(
                resources.getStringArray(R.array.pick_photo_menu),
                getString(R.string.select_photo)
            ) { position ->
                when (position) {
                    0 -> {
                        removeItem(it)
                    }
                }
            }
        })
    }

    /**
     * 업로드 로딩
     */
    private fun UploadViewModel.isUploadLoading() {
        isUploadLoading.observe(this@UploadActivity, Observer { isLoading ->
            if (isLoading) {
                uploadLoadingFragment.show(supportFragmentManager, "")
            } else {
                if (uploadLoadingFragment.isVisible) {
                    uploadLoadingFragment.dismiss()
                }
            }
        })
    }

    /**
     * 업로드 완료
     */
    private fun UploadViewModel.uploadComplete() {
        completeEvent.observe(this@UploadActivity, Observer { feedData ->
            startAct(FeedDetailActivity::class,Bundle().apply {
                putParcelable(CREATE_FEED_DATA_KEY,feedData)
            })
            finish()
        })
    }

    /**
     * 업로드 에러
     */
    private fun UploadViewModel.uploadError() {
        errorEvent.observe(this@UploadActivity, Observer {
            showShortToast("에러")
        })
    }

    /**
     * 포토 아이템 삭제
     */
    private fun removeItem(photoData: PhotoData) {
        uploadAdapter.removeItem(photoData)
        viewModel.removeFile(photoData)
    }

    /**
     * 앨범 퍼미션 체크
     */
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


    /**
     * 카매라 오픈
     */
    private fun showCamera() {
        PickImageUtil.pickFromCamera(this)
    }

    /**
     * 갤러리 리스트로 향함
     */
    private fun showCustomGallery() {
        startActForResult(GalleryActivity::class, PickImageUtil.PICK_FROM_ALBUM, Bundle().apply {
            putBoolean(GalleryActivity.KEY_IS_MULTIPLE_PICK, true)
            putInt(GalleryActivity.KEY_PICK_PHOTO_COUNT, uploadAdapter.itemCount)
            putInt(GalleryActivity.KEY_PHOTO_MAX_SIZE, UploadViewModel.PHOTO_MAX_SIZE)
        })
    }


    /**
     * 1. SelectMotorTypeActivity.REQUEST_CODE : 자동차 모델 Request
     * 2. PickImageUtil.PICK_FROM_CAMERA : 카메라 Request
     * 3. PickImageUtil.PICK_FROM_ALBUM : 앨범 Request
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SelectMotorTypeActivity.REQUEST_CODE -> {

                    data?.getParcelableExtra<MotorTypeData>(SelectMotorTypeActivity.RESULT_KEY)
                        ?.let {
                           viewModel.setMotorType(it)
                        }
                }

                PickImageUtil.PICK_FROM_CAMERA -> {
                    loadingProgress.show()
                    lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            getCameraFlow()
                        }
                        loadingProgress.onDismiss()
                    }

                }

                PickImageUtil.PICK_FROM_ALBUM -> {
                    loadingProgress.show()
                    lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            getAlbumFlow(data)
                        }
                        loadingProgress.onDismiss()
                    }
                }
            }
        }
    }


    /**
     * 카메라 파일 리사이즈
     */
    private suspend fun getCameraFlow() {
        PickImageUtil.getImageFromCameraPath()?.let { path ->
            lifecycleScope.launch {
                addResizeImage(File(path))
            }
        } ?: imageError()
    }

    /**
     * 앨범 리스트들을 파일을 만들어서 stream 시킴
     */
    private suspend fun getAlbumFlow(data: Intent?) {
        data?.getParcelableArrayListExtra<Uri>(GalleryActivity.KEY_SELECTED_GALLERY_ITEM)
            ?.let { list ->
                list.map { uri -> createFileWithUri(uri) }
            } ?: imageError()
    }

    /**
     * 파일을 만든 후 리사이즈를 시킨다 !
     * */
    private suspend fun createFileWithUri(uri: Uri?) {
        uri?.let {
            withContext(Dispatchers.IO) {
                PickImageUtil.createUriImageFile(this@UploadActivity, uri)
                    ?.let { file -> addResizeImage(file) }
            }
        } ?: imageError()
    }

    /**
     * 리 사이즈를 시키고 포토 Adapter에 Add 시킨다
     * */
    private suspend fun addResizeImage(file: File?) {
        file?.let {
            withContext(Dispatchers.IO) {
                PickImageUtil.resizeImage(it.path)
            }
            withContext(Dispatchers.Main) {

                uploadAdapter.addItem(
                    PhotoData().apply { this.file = it })

                viewModel.addFile(it)

            }
        } ?: imageError()
    }


    private fun imageError() {
        showShortToast(getString(R.string.error_not_load_image))
        loadingProgress.onDismiss()
    }

    private fun Dialog.onDismiss() {
        if (isShowing) {
            dismiss()
        }
    }

    companion object{
        const val CREATE_FEED_DATA_KEY="CREATE_FEED_DATA"
    }

}
