package com.like.drive.carstory.ui.main.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) :BaseViewModel(){

    val uploadClickEvent = SingleLiveEvent<Unit>()
    val signOutComplete = SingleLiveEvent<Unit>()

    fun signOut(view: View) {
        viewModelScope.launch {
            userRepository.signOut(
                success = {
                    signOutComplete.call()
                },
                fail = {}
            )
        }
    }

}