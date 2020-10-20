package com.like.drive.carstory.ui.profile.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.like.drive.carstory.R
import com.like.drive.carstory.common.enum.PhotoSelectType
import com.like.drive.carstory.common.enum.ProfilePhotoSelectType
import com.like.drive.carstory.databinding.ActivityProfileBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.showListDialog
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.base.ext.startAct
import com.like.drive.carstory.ui.base.ext.startActForResult
import com.like.drive.carstory.ui.dialog.ConfirmDialog
import com.like.drive.carstory.ui.gallery.activity.GalleryActivity
import com.like.drive.carstory.ui.main.activity.MainActivity
import com.like.drive.carstory.ui.dialog.AlertNickDialog
import com.like.drive.carstory.ui.profile.viewmodel.ProfileViewModel
import com.like.drive.carstory.ui.sign.`in`.activity.SignInActivity
import com.like.drive.carstory.util.photo.PickImageUtil
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivityProfileBinding) {
        super.onBinding(dataBinding)
        dataBinding.vm = viewModel
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { onBackPressed() }
        clickListener()
    }

    private fun clickListener() {
        tvLogout.setOnClickListener {
            showSignOutDialog(getString(R.string.logout_message))
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            imageClick()
            error()
            complete()
            signOutToPage()
            existNickname()
            loading()
            isFirstProfile()
            nickValidCheck()
        }
    }

    private fun ProfileViewModel.imageClick() {
        imageClickEvent.observe(this@ProfileActivity, Observer {
            showSelectPhotoList()
        })
    }

    private fun ProfileViewModel.complete() {
        completeEvent.observe(this@ProfileActivity, Observer {
            when (it) {
                ProfileViewModel.ProfileStatus.MODIFY -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                else -> {
                    finishAffinity()
                    startAct(MainActivity::class)
                }
            }
        })
    }

    private fun ProfileViewModel.signOutToPage() {
        signOut.observe(this@ProfileActivity, Observer {
            startAct(SignInActivity::class)
            finishAffinity()
        })
    }

    private fun ProfileViewModel.existNickname() {
        existNicknameEvent.observe(this@ProfileActivity, Observer {
            showShortToast(R.string.exist_nick_name_message)
        })
    }

    private fun ProfileViewModel.error() {
        errorEvent.observe(this@ProfileActivity, Observer {
            showShortToast(R.string.profile_error)
        })
    }

    private fun ProfileViewModel.loading() {
        isLoading.observe(this@ProfileActivity, Observer {
            if (it) loadingProgress.show() else if (loadingProgress.isShowing) loadingProgress.dismiss()
        })
    }

    private fun ProfileViewModel.isFirstProfile() {
        isFirstProfile.observe(this@ProfileActivity, Observer {
            if (it) {
                AlertNickDialog.newInstance().show(supportFragmentManager, "")
            }
        })
    }

    private fun ProfileViewModel.nickValidCheck() {
        nickValidEvent.observe(this@ProfileActivity, Observer {
            showShortToast(R.string.nick_valid_hint)
        })
    }

    /**
     * 사진 선택 리스트
     * 1.사진 2.앨범
     */

    private fun showSelectPhotoList() {
        showListDialog(
            ProfilePhotoSelectType.values().map { getString(it.resID) }.toTypedArray(),
            getString(R.string.select_photo)
        ) { position ->
            when (position) {
                ProfilePhotoSelectType.CAMERA.ordinal -> showCamera()
                ProfilePhotoSelectType.ALBUM.ordinal -> checkStoragePermission()
                ProfilePhotoSelectType.BASIC.ordinal -> viewModel.setImageFile(null)
            }
        }
    }

    /**
     * 카매라 오픈
     */
    private fun showCamera() {
        PickImageUtil.pickFromCamera(this)
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
     * 갤러리 리스트로 향함
     */
    private fun showCustomGallery() {
        startActForResult(GalleryActivity::class, PickImageUtil.PICK_FROM_ALBUM, Bundle().apply {
            putBoolean(GalleryActivity.KEY_IS_MULTIPLE_PICK, false)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                PickImageUtil.PICK_FROM_CAMERA -> {
                    loadingProgress.show()
                    lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            PickImageUtil.getImageFromCamera {
                                imageError()
                            }
                        }
                    }

                }

                PickImageUtil.PICK_FROM_ALBUM -> {
                    loadingProgress.show()
                    lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            data?.let {
                                data.getParcelableExtra<Uri>(GalleryActivity.KEY_SELECTED_GALLERY_ITEM)
                                    ?.let {
                                        PickImageUtil.getImageFromGallery(this@ProfileActivity, it)
                                    } ?: imageError()
                            }

                        }
                    }
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {

                    val result = CropImage.getActivityResult(data)
                    lifecycleScope.launch {
                        setResizeImage(result.uri)
                    }
                    loadingProgress.onDismiss()
                }

                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {

                    PickImageUtil.cancelPick()
                    loadingProgress.onDismiss()

                }

                else -> {

                    PickImageUtil.cancelPick()
                    loadingProgress.onDismiss()

                }

            }
        }
    }

    /**
     * 리사이즈를 시킨다 !
     * */

    private suspend fun setResizeImage(uri: Uri?) {
        uri?.let {
            val file = File(it.path!!)
            withContext(Dispatchers.IO) {
                PickImageUtil.resizeImage(file.path)
            }
            withContext(Dispatchers.Main) {
                viewModel.setImageFile(file)
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

    private fun showSignOutDialog(message: String) {
        ConfirmDialog.newInstance(message = message).apply {
            confirmAction = { viewModel.signOut() }
            cancelAction = { dismiss() }
        }.show(supportFragmentManager, "")
    }

    override fun onBackPressed() {

        if (viewModel.profileStatus == ProfileViewModel.ProfileStatus.INIT) {
            showSignOutDialog(getString(R.string.profile_init_logout_message))
            return
        }

        super.onBackPressed()
    }

}