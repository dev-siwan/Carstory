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
    val photoItemClickEvent = SingleLiveEvent<Int>()


    private val _originListData = MutableLiveData<List<PhotoData>>()
    private val _photoListData = MutableLiveData<List<PhotoData>>()
    val photoListData: LiveData<List<PhotoData>> get() = _photoListData

    private val originFileList = ArrayList<File>()


    init {
        createPhotoList(null)
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


    /**
     * 초기 진입 시 데이터와 비교해서 변경된 데이터가 있는지 확인
     * 사진 삭제 또는 대표사진 변경으로 순서 변경되었는지 체크
     */
    private fun isDiffOriginData(): Boolean {
        val originData = _originListData.value ?: emptyList()
        val tempData = _photoListData.value ?: emptyList()

        return if (originData.size != tempData.size) {
            true
        } else {
            val zippedList = originData.zip(tempData)
            !zippedList.all { (origin, temp) -> origin == temp }
        }
    }

    fun deletePhoto(position: Int) {
        val tempList = mutableListOf<PhotoData>()
        _photoListData.value?.filterIndexed { index, _ -> index != position }
            ?.map { tempList.add(it) }
        _photoListData.value = tempList
        removeFile(position)
    }

    /**
     * 이미지 리스트에 추가
     */
    fun setPath(file: File) {
        _photoListData.value = _photoListData.value?.plus(PhotoData().apply { this.file = file })
        addFile(file)
    }

    fun setPath(pathList:List<Uri>){
        pathList.map {
            _photoListData.value = _photoListData.value?.plus(PhotoData().apply { this.uri = it })
        }
    }

    fun addFile(file:File){
        originFileList.add(file)
    }

    private fun removeFile(position: Int){
        originFileList.removeAt(position)

        Timber.i("originFileSize = ${originFileList.size}")
    }

    /**
     * 사진 클릭 시 이벤트
     */
    fun onClickPhotoItem(position: Int) {
        photoItemClickEvent.value = position
    }



}