package com.like.drive.motorfeed.ui.gallery.viewmodel

import android.net.Uri
import android.provider.MediaStore

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
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


    private val _originGalleryData = MutableLiveData<List<GalleryItemData>>()
    val originGalleryData: LiveData<List<GalleryItemData>> get() = _originGalleryData

    private val _galleryDirectory = MutableLiveData<List<GalleryDirectoryData>>()
    val galleryDirectory: LiveData<List<GalleryDirectoryData>> get() = _galleryDirectory

    private val _selectedDirectory = MutableLiveData<GalleryDirectoryData>()
    val selectedDirectory: LiveData<GalleryDirectoryData> get() = _selectedDirectory

    val enableStatus = MediatorLiveData<Boolean>().apply {
        addSource(_selectedDirectory) { value = isExistSelectedItem() }
    }

    var remainCountValue :Int = 0
    private val _remainCount =MutableLiveData<Int>()
    val remainCount : LiveData<Int> get() = _remainCount

    private val _maxSize =MutableLiveData(0)
    val maxSize : LiveData<Int> get() = _maxSize

    private val _pickPhotoCount = MutableLiveData<Int>()
    val pickPhotoCount :LiveData<Int> get() = _pickPhotoCount

    val completeClickEvent = SingleLiveEvent<Unit>()
    val notAvailablePhoto = SingleLiveEvent<@StringRes Int>()
    val isLoading = SingleLiveEvent<Boolean>()

    val selectDirectoryClickEvent = SingleLiveEvent<Unit>()

    val singleUri = SingleLiveEvent<Uri>()

    private val uriList = ArrayList<Uri>()

    val addDataEvent =SingleLiveEvent<GalleryItemData>()
    val removeDataEvent = SingleLiveEvent<GalleryItemData>()

    val isMultiple=ObservableBoolean(false)


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
     * 사진 폴더 선택 시
     */
    fun onClickDirectory(data: GalleryDirectoryData) {
        _selectedDirectory.value = data
    }

    /**
     * 사진 아이템 클릭 시
     */
    fun onClickGalleryItem(data: GalleryItemData) {
        if (isAvailablePhoto(data.sizeMb, data.mimeType)) {

            if (!isMultiple.get()) {
                singleUri.value = data.uri
                return
            }

            data.run {
                if (selected.get()) {
                    removeUri(uri, this)
                } else {
                    addUri(uri, this)
                }
            }
            _pickPhotoCount.value = uriList.size
            enableStatus.value = isExistSelectedItem()
        }
    }


    /**
     * 아이템 추가
     */
    private fun addUri(uri: Uri, data: GalleryItemData) {
        if (isAddPhoto()) {
            if(uriList.add(uri)) {
                _remainCount.value = ++remainCountValue
                addDataEvent.value = data
            }
        } else {
            notAvailablePhoto.value = R.string.pick_photo_limit_message
            return
        }
    }

    /**
     * 아이템 삭제
     */
    private fun removeUri(uri: Uri, data: GalleryItemData) {
        if (uriList.remove(uri)) {
            _remainCount.value = --remainCountValue
            removeDataEvent.value = data
        }
    }


    /**
     * 다중선택 파일인지 아닌지 체크
     */

    fun initMultiple(isMultiple:Boolean){
        this.isMultiple.set(isMultiple)
    }

    /**
    * 남는갯수랑 맥스갯수 받음
     */
    fun initCount(maxSize: Int, count: Int) {
        _maxSize.value = maxSize
        remainCountValue = count
        _remainCount.value = remainCountValue
    }

    /**
     * 사용갯수 맥스 사이즈 비교
     */
    
    private fun isAddPhoto() = _remainCount.value ?: 0 < _maxSize.value ?: 0

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


}