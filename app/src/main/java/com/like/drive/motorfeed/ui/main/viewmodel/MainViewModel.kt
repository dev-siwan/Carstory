package com.like.drive.motorfeed.ui.main.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
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