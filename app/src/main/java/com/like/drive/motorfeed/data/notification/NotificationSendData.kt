package com.like.drive.motorfeed.data.notification

data class NotificationSendData(
    val notificationType: String? = null,
    val gid: String? = null,
    val eid: String? = null,
    val fid: String? = null,
    val uid: String? = null,
    val message:String? =null
) {
    fun getHashMap() =
        mapOf(
            "notificationType" to notificationType,
            "gid" to gid,
            "eid" to eid,
            "fid" to fid,
            "uid" to uid,
            "message" to message
        )
}

enum class NotificationType(val value:String) {
    NOTICE("0"), EVENT("1"), COMMENT("2"), RE_COMMENT("3")
}