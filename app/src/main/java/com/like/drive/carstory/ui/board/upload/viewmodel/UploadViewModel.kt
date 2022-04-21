package com.like.drive.carstory.ui.board.upload.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.R
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.data.photo.PhotoData
import com.like.drive.carstory.data.user.FilterData
import com.like.drive.carstory.repository.board.BoardRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.board.category.data.CategoryData
import com.like.drive.carstory.ui.board.upload.data.BoardUploadField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val boardRepository: BoardRepository) :
    BaseViewModel() {

    val selectPhotoClickEvent = SingleLiveEvent<Unit>()
    val photoItemClickEvent = SingleLiveEvent<PhotoData>()

    private val _photoListData = MutableLiveData<List<PhotoData>>()
    val photoListData: LiveData<List<PhotoData>> get() = _photoListData

    private val originFileList = ArrayList<PhotoData>()

    private val _pickPhotoCount = MutableLiveData(0)
    val pickPhotoCount: LiveData<Int> get() = _pickPhotoCount

    private val _motorType = MutableLiveData<MotorTypeData>()
    val motorTypeData: LiveData<MotorTypeData> get() = _motorType

    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()

    val isUploadLoading = SingleLiveEvent<Boolean>()

    private val _isPhotoUpload = MutableLiveData<Boolean>(true)
    val isPhotoUpload: LiveData<Boolean> get() = _isPhotoUpload

    private val _uploadPhotoCount = MutableLiveData(0)
    val uploadPhotoCount: LiveData<Int> get() = _uploadPhotoCount

    private val _categoryData = MutableLiveData<CategoryData>()
    val categoryData: LiveData<CategoryData> get() = _categoryData

    val completeEvent = SingleLiveEvent<BoardData>()
    val errorEvent = SingleLiveEvent<Int>()

    val closeCategoryItemPage = SingleLiveEvent<Unit>()
    val showCategoryItemPage = SingleLiveEvent<Unit>()

    val isFieldEnable = MediatorLiveData<Boolean>().apply {
        addSource(title) {
            value = isResultFieldValue(it, content.value, _categoryData.value)
        }
        addSource(content) {
            value = isResultFieldValue(title.value, it, _categoryData.value)
        }
        addSource(_categoryData) {
            value = isResultFieldValue(title.value, content.value, it)
        }
    }

    val isUpdate = ObservableBoolean(false)
    private var boardData: BoardData? = null

    /**
     * 초기 리스트 구성
     * 이미 업로드 된 이미지 있으면 표시
     * 없으면 빈 리스트로 초기화
     */
    fun getBoardData(boardData: BoardData?) {
        boardData?.let {

            this.boardData = it

            _photoListData.value = it.imageUrls?.map { url -> PhotoData(imgUrl = url) }
            _pickPhotoCount.value = it.imageUrls?.size

            _motorType.value = if (!it.brandName.isNullOrBlank()) MotorTypeData(
                brandName = it.brandName,
                brandCode = it.brandCode ?: 0,
                modelCode = it.modelCode ?: 0,
                modelName = it.modelName ?: ""
            ) else null

            _categoryData.value = CategoryData(it.categoryStr ?: "", "", it.categoryCode ?: 0)

            title.value = it.title
            content.value = it.content

            isUpdate.set(true)
        }
    }

    fun addFile(file: File) {
        originFileList.add(PhotoData().apply { this.file = file })
        setPhotoSize()
    }

    fun removeFile(photoData: PhotoData) {
        originFileList.remove(photoData)
        setPhotoSize()
    }

    /**
     * 사진 클릭 시 이벤트
     */
    fun onClickPhotoItem(photoData: PhotoData) {
        photoItemClickEvent.value = photoData
    }

    fun upload(tagList: List<String>) {

        isUploadLoading.value = true
        _isPhotoUpload.value = originFileList.isNotEmpty()

        val feedField = BoardUploadField(
            title = this.title.value!!,
            content = this.content.value!!,
            category = _categoryData.value!!,
            motorTypeData = _motorType.value,
            tagList = tagList as ArrayList<String>
        )

        if (isUpdate.get()) updateBoard(feedField, boardData) else addBoard(feedField)
    }

    private fun addBoard(boardField: BoardUploadField) {
        viewModelScope.launch {
            boardRepository.addBoard(boardField, originFileList,
                photoSuccessCount = { count ->
                    _uploadPhotoCount.postValue(count)
                    if (count == originFileList.size) {
                        _isPhotoUpload.postValue(false)
                    }
                },
                success = {
                    isUploadLoading.postValue(false)
                    completeEvent.value = it
                },
                fail = {
                    setError(R.string.board_upload_error_message)
                })
        }
    }

    private fun updateBoard(boardField: BoardUploadField, boardData: BoardData?) {
        boardData?.let {
            viewModelScope.launch {
                boardRepository.updateBoard(boardField, boardData,
                    success = {
                        isUploadLoading.postValue(false)
                        completeEvent.value = it
                    },
                    fail = {
                        setError(R.string.board_upload_error_message)
                    })
            }
        } ?: setError()
    }

    private fun setError(resID: Int? = null) {
        isUploadLoading.value = false
        resID?.let {
            errorEvent.postValue(resID)
        } ?: errorEvent.call()
    }

    fun setCategoryItem(category: CategoryData?) {
        _categoryData.value = category
        closeCategoryItemPage.call()
    }

    private fun isResultFieldValue(title: String?, content: String?, feedItemType: CategoryData?) =
        !title.isNullOrBlank() && !content.isNullOrBlank() && feedItemType != null

    private fun setPhotoSize() {
        _pickPhotoCount.postValue(originFileList.size)
    }

    fun isPhotoLimitSize() = originFileList.size < PHOTO_MAX_SIZE

    fun setMotorType(motorTypeData: MotorTypeData) {
        if (motorTypeData.brandCode == 0) {
            _motorType.value = null
            return
        }
        _motorType.value = motorTypeData
    }

    fun setFilterData(filterData: FilterData) {
        _categoryData.value = filterData.categoryData
        _motorType.value = filterData.motorType
    }

    companion object {
        val PHOTO_MAX_SIZE = 5
    }

}