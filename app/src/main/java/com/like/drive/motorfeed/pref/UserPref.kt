package com.like.drive.motorfeed.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.pref.util.ModelPreferencesManager

class UserPref(context:Context){
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private val modelPref = ModelPreferencesManager

    var motorTypeVersion :Int?
    get() = preferences.getInt(MOTOR_TYPE_VERSION,0)
    set(value) = preferences.edit { putInt(MOTOR_TYPE_VERSION,value!!) }
    var userData: UserData?
        get() = modelPref.get(USER_INFO)
        set(value) {
            modelPref.put(value, USER_INFO)
        }

    companion object {
        const val MOTOR_TYPE_VERSION = "MOTOR_TYPE_VERSION_KEY"
        const val USER_INFO="USER_INFO_KEY"
    }
}