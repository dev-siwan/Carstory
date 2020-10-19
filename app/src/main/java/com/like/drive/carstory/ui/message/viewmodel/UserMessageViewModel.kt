package com.like.drive.carstory.ui.message.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.CarStoryApplication
import com.like.drive.carstory.R
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.repository.admin.AdminRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.message.data.UserMessageType
import kotlinx.coroutines.launch

class UserMessageViewModel(private val adminRepository: AdminRepository) : BaseViewModel() {

    val context = CarStoryApplication.getContext()

    private val _userMessageType = MutableLiveData<List<UserMessageType>>(
        UserMessageType().getList(CarStoryApplication.getContext())
    )
    val userMessageType: LiveData<List<UserMessageType>> get() = _userMessageType

    var uid: String? = null
    var isUserBan: Boolean? = false

    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val completeEvent = SingleLiveEvent<@StringRes Int>()

    val confirmMessage = SingleLiveEvent<String>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    val message = ObservableField<String>()

    fun setTypeMessage(message: String) {
        confirmMessage.value = if (isUserBan == true) {
            context.getString(R.string.user_ban_type_format, message)
        } else {
            context.getString(R.string.user_message_type_format, message)
        }
    }

    fun setContentMessage(message: String) {
        confirmMessage.value = message
    }

    fun sendRequestUserStatus(message: String) {
        _loading.value = true

        if (isUserBan == true) setUserBan(message) else sendUserMessage(message)

    }

    private fun sendUserMessage(message: String) {
        uid?.let {
            viewModelScope.launch {
                adminRepository.sendUserMessage(it, message,
                    success = {
                        completeEvent.value = R.string.user_message_send_complete
                    },
                    fail = {
                        setError(R.string.user_message_send_error)
                    })
            }
        }
    }

    private fun setUserBan(message: String) {
        uid?.let {
            viewModelScope.launch {
                adminRepository.setUserBam(it, message,
                    success = {
                        completeEvent.value = R.string.user_ban_send_complete
                    },
                    fail = {
                        setError(R.string.user_ban_error_message)
                    })
            }
        }
    }

    private fun setError(resID: Int) {
        _loading.value = false
        errorEvent.value = resID
    }

    fun init(uid: String, isUserBan: Boolean) {
        this.uid = uid
        this.isUserBan = isUserBan
    }

}