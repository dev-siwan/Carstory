package com.like.drive.motorfeed.ui.upload.viewmodel


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.upload.data.PhotoData
import timber.log.Timber
import java.io.File

class UploadViewModel:BaseViewModel(){

    val selectPhotoClickEvent = SingleLiveEvent<Unit>()
    val photoItemClickEvent = SingleLiveEvent<PhotoData>()

    private val _originListData = MutableLiveData<List<PhotoData>>()
    private val _photoListData = MutableLiveData<List<PhotoData>>()
    val photoListData: LiveData<List<PhotoData>> get() = _photoListData

    private val originFileList = ArrayList<File>()


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
    }

    fun removeFile(photoData: PhotoData){
        originFileList.remove(photoData.file)

    }

    /**
     * 사진 클릭 시 이벤트
     */
    fun onClickPhotoItem(photoData: PhotoData) {
        photoItemClickEvent.value = photoData
    }



}