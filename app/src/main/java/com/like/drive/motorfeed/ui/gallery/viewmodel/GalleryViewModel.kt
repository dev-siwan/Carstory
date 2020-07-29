package com.like.drive.motorfeed.ui.gallery.viewmodel

import android.net.Uri
import android.provider.MediaStore
import android.view.View

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.gallery.data.GalleryDirectoryData
import com.like.drive.motorfeed.ui.gallery.data.GalleryItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel : BaseViewModel() {

    private val _galleryListData = MutableLiveData<List<GalleryItemData>>()
    val galleryListData: LiveData<List<GalleryItemData>> get() = _galleryListData

    private val _galleryDirectory = MutableLiveData<List<GalleryDirectoryData>>()
    val galleryDirectory: LiveData<List<GalleryDirectoryData>> get() = _galleryDirectory

    private val _selectedDirectory = MutableLiveData<GalleryDirectoryData>()
    val selectedDirectory: LiveData<GalleryDirectoryData> get() = _selectedDirectory

    val enableStatus = MediatorLiveData<Boolean>().apply {
        addSource(_selectedDirectory) { value = isExistSelectedItem() }
    }

    private val _originGalleryData = MutableLiveData<List<GalleryItemData>>()
    val originGalleryData: LiveData<List<GalleryItemData>> get() = _originGalleryData

    val completeClickEvent = SingleLiveEvent<Unit>()
    val notAvailablePhoto = SingleLiveEvent<@StringRes Int>()
    val isLoading = SingleLiveEvent<Boolean>()
    val selectDirectoryClickEvent = SingleLiveEvent<Unit>()

    private val uriList = ArrayList<Uri>()

    init {
        viewModelScope.launch {
            isLoading.value = true
            _originGalleryData.value = withContext(Dispatchers.IO) {
                loadGalleryImage()
            }
            isLoading.value = false
        }
    }


    /**
     *  갤러리 이미지 리스트 가져오기
     */
    private fun loadGalleryImage(): List<GalleryItemData> {
        val galleryList = mutableListOf<GalleryItemData>()
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.SIZE)
        val contentResolver = MotorFeedApplication.getContext().contentResolver
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media._ID + " DESC"
        )?.use {
            val idColumn = it.getColumnIndexOrThrow(projection[0])
            val bucketColumn: Int = it.getColumnIndexOrThrow(projection[1])
            val sizeColumn: Int = it.getColumnIndexOrThrow(projection[2])
            if (it.moveToFirst()) {
                do {
                    val uri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it.getString(idColumn)
                    )
                    val mimeType = contentResolver.getType(uri) ?: ""
                    val directory = it.getString(bucketColumn) ?: ""
                    val sizeMb = it.getLong(sizeColumn) / (1024 * 1024)

                    galleryList.add(GalleryItemData(uri, mimeType, directory, sizeMb))
                } while (it.moveToNext())
            }
        }
        return galleryList
    }

    /**
     * 사진 폴더 반환
     */
    fun getGalleryDirectory() {
        _galleryDirectory.value = GalleryDirectoryData().getDirectoryData(_originGalleryData.value)
    }

    /**
     * 선택한 폴더의 사진을 반환
     */
    fun bringGalleryItem(directoryName: String?) {

        uriList.clear()

        _galleryListData.value = directoryName?.let { directory ->
            _originGalleryData.value?.filter { it.directory == directory }
        } ?: _originGalleryData.value
    }

    /**
     * 사진 폴더 선택 시
     */
    fun onClickDirectory(data: GalleryDirectoryData) {
        _selectedDirectory.value = data
    }

    /**
     * 사진 아이템 클릭 시
     */
    fun onClickGalleryItem(view: TextView, data: GalleryItemData) {
        if (isAvailablePhoto(data.sizeMb, data.mimeType)) {

            if (view.isVisible) {

                view.visibility = View.GONE
                uriList.remove(data.uri)

            } else {

                view.visibility = View.VISIBLE
                uriList.add(data.uri)

            }

            view.text = uriList.size.toString()

            enableStatus.value = isExistSelectedItem()
        }
    }

    /**
     * 사진 크기 및 확장자 체크
     * 크기 : 파일당 최대 10MB
     */
    private fun isAvailablePhoto(size: Long, mimeType: String): Boolean {
        return when {
            size > 10 -> {
                notAvailablePhoto.value = R.string.not_available_photo_size
                false
            }
            !mimeType.availableMimeType() -> {
                notAvailablePhoto.value = R.string.not_available_mime_type
                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * 선택 가능한 확장자 체크
     */
    private fun String.availableMimeType(): Boolean = contains("jpeg") || contains("jpg") || contains("png")


    /**
     *  선택된 아이템이 있는 지 체크
     */
    private fun isExistSelectedItem() = uriList.isNotEmpty()


    /**
     *  선택된 아이템의 Uri를 반환
     */
    fun getSelectedGalleryItem(): ArrayList<Uri> {
        return uriList
    }

    /**
     * 선택한 폴더명을 반환
     */
    fun getSelectedDirectoryTitle() = selectedDirectory.value?.display ?: ""

    /**
     *  폴더명을 강제로 설정
     */
    fun setSelectedDirectory(data: GalleryDirectoryData) {
        _selectedDirectory.value = data
    }

}