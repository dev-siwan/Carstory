package com.like.drive.carstory.ui.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.R
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLookUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> get() = _userData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    val errorEvent = SingleLiveEvent<Int>()

    fun init(uid: String) {

        _loading.value = true

        viewModelScope.launch {
            userRepository.getUserProfile(
                uid,
                success = {
                    it?.let { userData ->
                        _userData.value = userData
                        _loading.value = false
                    } ?: setError()

                },
                fail = {
                    setError()
                }
            )
        }
    }

    private fun setError() {
        _loading.value = false
        errorEvent.value = R.string.profile_error_message
    }

}