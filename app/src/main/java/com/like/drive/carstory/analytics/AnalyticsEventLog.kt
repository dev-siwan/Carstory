package com.like.drive.carstory.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class AnalyticsEventLog {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun setUploadEvent(brandName: String?, motorName: String?, category: String?) {
        firebaseAnalytics = Firebase.analytics

        firebaseAnalytics.logEvent(AnalyticsDefine.BOARD_UPLOAD_EVENT_NAME) {
            category?.let { param(AnalyticsDefine.UPLOAD_CATEGORY_TYPE, it) }
            brandName?.let { param(AnalyticsDefine.UPLOAD_MOTOR_TYPE, "$it-${motorName ?: ""}") }
        }

    }

    fun setFilterEvent(brandName: String?, motorName: String?, category: String?) {
        firebaseAnalytics = Firebase.analytics

        firebaseAnalytics.logEvent(AnalyticsDefine.FILTER_EVENT_NAME) {
            category?.let { param(AnalyticsDefine.FILTER_CATEGORY_TYPE, it) }
            brandName?.let { param(AnalyticsDefine.FILTER_MOTOR_TYPE, "$it-${motorName ?: ""}") }
        }
    }

    fun searchTagEvent(tagValue: String?) {
        firebaseAnalytics = Firebase.analytics

        firebaseAnalytics.logEvent(AnalyticsDefine.SEARCH_TAG_EVENT_NAME) {
            tagValue?.let { param(AnalyticsDefine.SEARCH_TAG_VALUE, it) }
        }
    }

}