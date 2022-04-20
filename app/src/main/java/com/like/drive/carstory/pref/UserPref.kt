package com.like.drive.carstory.pref

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.pref.util.ModelPreferencesManager
import com.like.drive.carstory.ui.search.data.RecentlyData

class UserPref(context: Context) {
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    private val modelPref = ModelPreferencesManager.apply {
        with(context)
    }

    var fcmToken: String?
        get() = preferences.getString(FCM_TOKEN, null)
        set(value) = preferences.edit { putString(FCM_TOKEN, value) }

    var isNewToken: Boolean
        get() = preferences.getBoolean(IS_SEND_FCM_TOKEN, false)
        set(value) = preferences.edit { putBoolean(IS_SEND_FCM_TOKEN, value) }

    var isNoticeTopic: Boolean
        get() = preferences.getBoolean(NOTICE_TOPIC, true)
        set(value) = preferences.edit { putBoolean(NOTICE_TOPIC, value) }

    var motorTypeVersion: Int?
        get() = preferences.getInt(MOTOR_TYPE_VERSION, 0)
        set(value) = preferences.edit { putInt(MOTOR_TYPE_VERSION, value!!) }

    var userData: UserData?
        get() = modelPref.get(USER_INFO)
        set(value) {
            modelPref.put(value, USER_INFO)
        }

    var recentlyData: ArrayList<RecentlyData>
        get() {

            val jsonPref = preferences.getString(RECENTLY_LIST, "")

            return if (jsonPref.isNullOrEmpty()) {
                ArrayList()
            } else {
                val type = object : TypeToken<ArrayList<RecentlyData>>() {}.type
                Gson().fromJson(jsonPref, type)

            }
        }
        set(value) {
            val tagList = Gson().toJson(value)
            preferences.edit { putString(RECENTLY_LIST, tagList) }
        }

    var isPermissionPopUp: Boolean
        get() = preferences.getBoolean(PERMISSION_CHECK, false)
        set(value) = preferences.edit { putBoolean(PERMISSION_CHECK, value) }

    fun removeUserInfo() {
        modelPref.remove(USER_INFO)
    }

    companion object {
        const val MOTOR_TYPE_VERSION = "MOTOR_TYPE_VERSION_KEY"
        const val USER_INFO = "USER_INFO_KEY"
        const val FCM_TOKEN = "FCM_TOKEN"
        const val IS_SEND_FCM_TOKEN = "IS_SEND_FCM_TOKEN"
        const val RECENTLY_LIST = "RECENTLY_LIST"
        const val NOTICE_TOPIC = "NOTICE_TOPIC"
        const val PERMISSION_CHECK = "PERMISSION_CHECK"
    }
}