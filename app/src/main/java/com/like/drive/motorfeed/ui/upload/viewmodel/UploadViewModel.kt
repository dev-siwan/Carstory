package com.like.drive.motorfeed.ui.upload.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.upload.data.PhotoData
import java.io.File

class UploadViewModel:BaseViewModel(){

    val selectPhotoClickEvent = SingleLiveEvent<Unit>()
    val photoItemClickEvent = SingleLiveEvent<PhotoData>()

    private val _originListData = MutableLiveData<List<PhotoData>>()

    private val _photoListData = MutableLiveData<List<PhotoData>>()
    val photoListData: LiveData<List<PhotoData>> get() = _photoListData

    private val originFileList = ArrayList<File>()

    private val _pickPhotoCount = MutableLiveData(0)
    val pickPhotoCount :LiveData<Int> get() = _pickPhotoCount

    init {
       // createPhotoList(null)
    }
    /**
     * 초기 리스트 구성
     * 이미 업로드 된 이미지 있으면 표시
     * 없으면 빈 리스트로 초기화
     */
    private fun createPhotoList(uploadedKeys: Array<String>?) {
        if (uploadedKeys?.isNotEmpty() == true) {
            _photoListData.value = uploadedKeys.map {
                PhotoData(imgUrl = it)
            }
        } else {
            _photoListData.value = emptyList()
        }
        _originListData.value = _photoListData.value
    }


    fun addFile(file:File){
        originFileList.add(file)
        setPhotoSize()
    }

    fun removeFile(photoData: PhotoData){
        originFileList.remove(photoData.file)
        setPhotoSize()
    }

    /**
     * 사진 클릭 시 이벤트
     */
    fun onClickPhotoItem(photoData: PhotoData) {
        photoItemClickEvent.value = photoData
    }

    private fun setPhotoSize(){ _pickPhotoCount.postValue(originFileList.size) }

    fun isPhotoLimitSize() = originFileList.size < PHOTO_MAX_SIZE


    companion object{
        val PHOTO_MAX_SIZE = 5
    }

}