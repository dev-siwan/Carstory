package com.like.drive.motorfeed.pref

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.pref.util.ModelPreferencesManager

class UserPref(application:Application){
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    private val modelPref = ModelPreferencesManager.apply {
        with(application)
    }



    var motorTypeVersion :Int?
    get() = preferences.getInt(MOTOR_TYPE_VERSION,0)
    set(value) = preferences.edit { putInt(MOTOR_TYPE_VERSION,value!!) }

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
        const val USER_INFO="USER_INFO_KEY"
    }
}