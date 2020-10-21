package com.like.drive.carstory.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.like.drive.carstory.R
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.pref.UserPref
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel(private val userRepository: UserRepository) : BaseViewModel(), KoinComponent {

    private val userPref: UserPref by inject()

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
        if (!userPref.isPermissionPopUp) {
            showPermissionEvent.call()
        }
    }

    fun confirmPermission() {
        userPref.isPermissionPopUp = true
    }

    private fun userMessage() {
        if (UserInfo.userInfo?.userMessageStatus == true) {
            UserInfo.userInfo?.userMessage?.let {
                if (!it.isBlank()) {
                    userMessageEvent.value = it
                }
            }
        }
    }

    fun confirmUserMessage() {
        UserInfo.userInfo?.uid?.let {
            viewModelScope.launch {
                userRepository.confirmUserMessage(it)
                UserInfo.userInfo?.userMessageStatus = false
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