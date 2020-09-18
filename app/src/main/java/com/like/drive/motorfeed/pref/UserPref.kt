package com.like.drive.motorfeed.pref

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.data.user.UserFilter
import com.like.drive.motorfeed.pref.util.ModelPreferencesManager
import com.like.drive.motorfeed.ui.search.data.RecentlyData

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

    var isNoticeTopic: Boolean
        get() = preferences.getBoolean(NOTICE_TOPIC, true)
        set(value) = preferences.edit { putBoolean(NOTICE_TOPIC, value) }

    var motorTypeVersion: Int?
        get() = preferences.getInt(MOTOR_TYPE_VERSION, 0)
        set(value) = preferences.edit { putInt(MOTOR_TYPE_VERSION, value!!) }

    var userFilter: UserFilter?
        get() = modelPref.get(FEED_TYPE)
        set(value) {
            modelPref.put(value, FEED_TYPE)
        }

    var userData: UserData?
        get() = modelPref.get(USER_INFO)
        set(value) {
            modelPref.put(value, USER_INFO)
        }

    var recentlyData:ArrayList<RecentlyData>
        get() {

            val jsonPref = preferences.getString(RECENTLY_LIST,"")

            return if(jsonPref.isNullOrEmpty()){
                ArrayList()
            }else{
                val type = object :TypeToken<ArrayList<RecentlyData>>(){}.type
                Gson().fromJson(jsonPref,type)

            }
        }
        set(value) {
            val tagList = Gson().toJson(value)
            preferences.edit { putString(RECENTLY_LIST,tagList) }
        }

    fun removeUserInfo() {
        modelPref.remove(USER_INFO)
    }

    companion object {
        const val MOTOR_TYPE_VERSION = "MOTOR_TYPE_VERSION_KEY"
        const val USER_INFO = "USER_INFO_KEY"
        const val FCM_TOKEN = "FCM_TOKEN"
        const val IS_SEND_FCM_TOKEN = "IS_SEND_FCM_TOKEN"
        const val FEED_TYPE = "USER_FILTER"
        const val RECENTLY_LIST = "RECENTLY_LIST"
        const val NOTICE_TOPIC="NOTICE_TOPIC"
    }
}