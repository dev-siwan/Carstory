package com.like.drive.motorfeed.pref

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.data.user.UserFilter
import com.like.drive.motorfeed.pref.util.ModelPreferencesManager
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData

class UserPref(application: Application) {
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val modelPref = ModelPreferencesManager.apply {
        with(application)
    }

    var fcmToken: String?
        get() = preferences.getString(FCM_TOKEN, null)
        set(value) = preferences.edit { putString(FCM_TOKEN, value) }

    var isNewToken: Boolean
        get() = preferences.getBoolean(IS_SEND_FCM_TOKEN, false)
        set(value) = preferences.edit { putBoolean(IS_SEND_FCM_TOKEN, value) }

    var motorTypeVersion: Int?
        get() = preferences.getInt(MOTOR_TYPE_VERSION, 0)
        set(value) = preferences.edit { putInt(MOTOR_TYPE_VERSION, value!!) }


    var userFilter : UserFilter?
        get() = modelPref.get(FEED_TYPE)
        set(value) {
            modelPref.put(value, FEED_TYPE)
        }

    var userData: UserData?
        get() = modelPref.get(USER_INFO)
        set(value) {
            modelPref.put(value, USER_INFO)
        }

    fun removeUserInfo() {
        modelPref.remove(USER_INFO)
        //preferences.edit { remove(AUTH_KEY) }
        //AccountInfo.authKey = null
    }

    companion object {
        const val MOTOR_TYPE_VERSION = "MOTOR_TYPE_VERSION_KEY"
        const val USER_INFO = "USER_INFO_KEY"
        const val FCM_TOKEN = "FCM_TOKEN"
        const val IS_SEND_FCM_TOKEN = "IS_SEND_FCM_TOKEN"
        const val FEED_TYPE ="USER_FILTER"
    }
}