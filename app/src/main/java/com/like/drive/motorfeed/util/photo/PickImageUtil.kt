package com.like.drive.motorfeed.util.photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.like.drive.motorfeed.R

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


object PickImageUtil {

    private var tempFile: File? = null
    private var isCamera = false
    private var requestCode = 0

    private lateinit var activity: Activity
    private var fragment: Fragment? = null


    fun pickFromGallery(
        activity: Activity,
        requestCode: Int = PICK_FROM_ALBUM
    ) {
        this.activity = activity
        this.requestCode = requestCode

        permissionStorage()
    }

    fun pickFromGallery(fragment: Fragment, requestCode: Int = PICK_FROM_ALBUM) {

        this.activity = fragment.requireActivity()
        this.fragment = fragment
        this.requestCode = requestCode

        permissionStorage()
    }

    fun pickFromCamera(activity: Activity, requestCode: Int = PICK_FROM_CAMERA) {

        PickImageUtil.activity = activity
        this.requestCode = requestCode
        permissionCamera()
    }


    fun pickFromCamera(fragment: Fragment, requestCode: Int = PICK_FROM_CAMERA) {

        this.fragment = fragment
        this.activity = fragment.requireActivity()
        this.requestCode = requestCode
        permissionCamera()
    }

    private fun setImageFromGallery() {

        isCamera = false

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        if (fragment != null) {
            fragment?.startActivityForResult(intent, requestCode)
        } else {
            activity.startActivityForResult(intent, requestCode)
        }
    }

    fun getImageFromGallery(data: Intent) {
        val photoUri = data.data
        cropImage(photoUri!!)
    }

    private fun permissionCamera() {
        TedPermission.with(activity)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    setImageFromCamera()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }
            })
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .setDeniedMessage(activity.getString(R.string.denied_permission_camera))
            .check()
    }

    private fun permissionStorage() {
        TedPermission.with(activity)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    setImageFromGallery()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                }
            }).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .setDeniedMessage(activity.getString(R.string.denied_permission_storage))
            .check()
    }


    private fun setImageFromCamera() {

        isCamera = true

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            tempFile = createImageFile()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        tempFile?.let { file ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    activity,
                    "${activity.applicationContext.packageName}.fileprovider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }.also { uri ->
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                if (fragment != null) {
                    fragment?.startActivityForResult(intent, requestCode)
                } else {
                    activity.startActivityForResult(intent, requestCode)
                }
            }
        }
    }


    fun getImageFromCameraPath():String? {
        val path = tempFile?.path.toString()
        tempFile = null
        return path
    }

    fun cancelPick() {
        tempFile?.apply {
            if (exists()) {
                delete()
            }
        }
    }

    fun setImage(path: String):File {

        val originFile = File(path)
        ImageResizeUtil.resizeFile(originFile, originFile, 1280, isCamera)

      /*  val options = BitmapFactory.Options()
        val originalBm = BitmapFactory.decodeFile(originFile.absolutePath, options)*/

        tempFile=null

        return originFile
    }

    private fun cropImage(photoUri: Uri) {

        if (tempFile == null) {
            try {
                tempFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        /*// 크롭 후 이미지 비율
        CropImage.activity(photoUri).apply {
            when (cropCode) {
                CROP_1_1 -> {
                    setAspectRatio(1, 1)
                    setCropShape(CropImageView.CropShape.RECTANGLE)
                    start(activity)
                }
            }
        }*/
    }


    private fun createImageFile(): File {

        // 이미지 파일 이름
        val cal = Calendar.getInstance().time
        val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val imageFileName = "DabangPro_${timeStamp.format(cal)}"

        // 이미지가 저장될 폴더 이름
        val storageDir = File(activity.getExternalFilesDir(null)?.absolutePath + "/img/")
        if (!storageDir.exists()) storageDir.mkdirs()

        // 빈 파일 생성
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    //image pick code
    const val PICK_FROM_ALBUM = 1000
    const val PICK_FROM_CAMERA = 1001

    //crop code
    const val CROP_1_1 = 2000
}