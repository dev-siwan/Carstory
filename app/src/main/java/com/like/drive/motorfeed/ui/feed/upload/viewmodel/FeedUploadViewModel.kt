package com.like.drive.motorfeed.ui.feed.upload.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.feed.upload.data.FeedUploadField
import kotlinx.coroutines.launch
import java.io.File
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean

class FeedUploadViewModel(private val feedRepository: FeedRepository):BaseViewModel(){

    val selectPhotoClickEvent = SingleLiveEvent<Unit>()
    val photoItemClickEvent = SingleLiveEvent<PhotoData>()

    private val _photoListData = MutableLiveData<List<PhotoData>>()
    val photoListData: LiveData<List<PhotoData>> get() = _photoListData

    private val originFileList = ArrayList<PhotoData>()

    private val _pickPhotoCount = MutableLiveData(0)
    val pickPhotoCount: LiveData<Int> get() = _pickPhotoCount

    private val _motorType = MutableLiveData<MotorTypeData>()
    val motorTypeData: LiveData<MotorTypeData> get() = _motorType

    private val _tagList = MutableLiveData<ArrayList<String>>()
    val tagList: LiveData<ArrayList<String>> get() = _tagList

    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()

    val isUploadLoading = SingleLiveEvent<Boolean>()

    private val _isPhotoUpload = MutableLiveData<Boolean>(true)
    val isPhotoUpload: LiveData<Boolean> get() = _isPhotoUpload

    private val _uploadPhotoCount = MutableLiveData(0)
    val uploadPhotoCount: LiveData<Int> get() = _uploadPhotoCount

    private val _feedType = MutableLiveData<FeedTypeData>()
    val feedTypeData: LiveData<FeedTypeData> get() = _feedType

    val completeEvent = SingleLiveEvent<FeedData>()
    val errorEvent = SingleLiveEvent<@StringRes Int>()

    val closeFeedItemPage = SingleLiveEvent<Unit>()
    val showFeedItemPage = SingleLiveEvent<Unit>()


    val isFieldEnable = MediatorLiveData<Boolean>().apply {
        addSource(title) {
            value = isResultFieldValue(it, content.value, _feedType.value)
        }
        addSource(content) {
            value = isResultFieldValue(title.value, it, _feedType.value)
        }
        addSource(_feedType) {
            value = isResultFieldValue(title.value, content.value, it)
        }
    }

    val isUpdate = ObservableBoolean(false)
    private var feedData: FeedData? = null

    /**
     * 초기 리스트 구성
     * 이미 업로드 된 이미지 있으면 표시
     * 없으면 빈 리스트로 초기화
     */
    fun getFeedData(feedData: FeedData?) {
        feedData?.let {

            this.feedData= it

            _photoListData.value = it.imageUrls?.map { url -> PhotoData(imgUrl = url) }
            _pickPhotoCount.value = it.imageUrls?.size

            _motorType.value = if (!it.brandName.isNullOrBlank()) MotorTypeData(
                brandName = it.brandName,
                brandCode = it.brandCode ?: 0,
                modelCode = it.modelCode ?: 0,
                modelName = it.modelName ?: ""
            ) else null

            _feedType.value = FeedTypeData(it.feedTypeStr ?: "", "", it.feedTypeCode ?: 0)
            _tagList.value = it.feedTagList?.let { list-> list as ArrayList<String> }

            title.value = it.title
            content.value = it.content

            isUpdate.set(true)
        }
    }


    fun addFile(file:File){
        originFileList.add(PhotoData().apply { this.file = file })
        setPhotoSize()
    }

    fun removeFile(photoData: PhotoData){
        originFileList.remove(photoData)
        setPhotoSize()
    }

    /**
     * 사진 클릭 시 이벤트
     */
    fun onClickPhotoItem(photoData: PhotoData) {
        photoItemClickEvent.value = photoData
    }

    fun upload() {

        isUploadLoading.value = true
        _isPhotoUpload.value = originFileList.isNotEmpty()

        val feedField = FeedUploadField(
            title = this.title.value!!,
            content = this.content.value!!,
            feedType = _feedType.value!!,
            motorTypeData = _motorType.value,
            feedTagList = _tagList.value
        )

        if(isUpdate.get()) updateFeed(feedField,feedData) else addFeed(feedField)
    }

    /*
       * TODO 에러메세지 넣어야함*/
    private fun addFeed(feedField: FeedUploadField) {
        viewModelScope.launch {
            feedRepository.addFeed(feedField, originFileList,
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
                    setError()
                })
        }
    }

    /*
    * TODO 에러메세지 넣어야함*/
    private fun updateFeed(feedField: FeedUploadField, feedData: FeedData?) {
        feedData?.let {
            viewModelScope.launch {
                feedRepository.updateFeed(feedField, feedData,
                    success = {
                        isUploadLoading.postValue(false)
                        completeEvent.value = it
                    },
                    fail = {
                        setError()
                    })
            }
        } ?: setError()
    }


    private fun setError(resID:Int?=null){
        isUploadLoading.value = false
        resID?.let {
            errorEvent.postValue(resID)
        }?:errorEvent.call()
    }

    fun setFeedTypeItem(feedItemType: FeedTypeData?){
        _feedType.value =feedItemType
        closeFeedItemPage.call()
    }

    private fun isResultFieldValue(title:String?,content:String?,feedItemType:FeedTypeData?) =
        !title.isNullOrBlank() && !content.isNullOrBlank() && feedItemType!=null

    private fun setPhotoSize(){ _pickPhotoCount.postValue(originFileList.size) }

    fun isPhotoLimitSize() = originFileList.size < PHOTO_MAX_SIZE

    fun setMotorType(motorTypeData: MotorTypeData) {
        if (motorTypeData.brandCode == 0) {
            _motorType.value = null
            return
        }
        _motorType.value = motorTypeData
    }

    fun setTagList(tagList:ArrayList<String>){ _tagList.value = tagList}


    companion object{
        val PHOTO_MAX_SIZE = 5
    }

}