package com.like.drive.carstory.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userInfo: UserInfo,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val showPermissionEvent = SingleLiveEvent<Unit>()

    val uploadClickEvent = SingleLiveEvent<Unit>()
    val userMessageEvent = SingleLiveEvent<String>()

    val configVersionCode = SingleLiveEvent<String>()

    val notificationRefreshEvent = SingleLiveEvent<Unit>()
    private val remoteConfig by lazy { Firebase.remoteConfig }

    init {
        checkPermission()
        userMessage()
        setRemoteConfig()
    }

    private fun checkPermission() {
        if (!userInfo.userPref.isPermissionPopUp) {
            showPermissionEvent.call()
        }
    }

    fun confirmPermission() {
        userInfo.userPref.isPermissionPopUp = true
    }

    private fun userMessage() {
        if (userInfo.userInfo?.userMessageStatus == true) {
            userInfo.userInfo?.userMessage?.let {
                if (!it.isBlank()) {
                    userMessageEvent.value = it
                }
            }
        }
    }

    fun confirmUserMessage() {
        userInfo.userInfo?.uid?.let {
            viewModelScope.launch {
                userRepository.confirmUserMessage(it)
                userInfo.userInfo?.userMessageStatus = false
            }
        }
    }

    fun onNotificationRefreshListener() {
        notificationRefreshEvent.call()
    }

    private fun setRemoteConfig() {
        val configSetting = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1200
        }
        remoteConfig.setConfigSettingsAsync(configSetting)

        //remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val configAppVersion = remoteConfig[APP_LAST_VERSION_KEY].asString()
                configVersionCode.value = configAppVersion

            }
        }
    }

    companion object {
        const val APP_LAST_VERSION_KEY = "app_latest_version_name"
    }

}