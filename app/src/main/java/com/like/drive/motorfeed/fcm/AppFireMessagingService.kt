package com.like.drive.motorfeed.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.util.notification.NotificationUtil

class AppFireMessagingService : FirebaseMessagingService() {

    val context = MotorFeedApplication.getContext()

    override fun onNewToken(token: String) {
        UserPref(context).isNewToken = true
        UserPref(context).fcmToken = token
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessaging: RemoteMessage) {
        super.onMessageReceived(remoteMessaging)

        if (UserInfo.userInfo == null) return

        remoteMessaging.notification?.let {
            val title = it.title ?: context.getString(R.string.app_name_kr)
            val body = it.body ?: ""
            handleNotification(title, body)
        }
    }

    private fun handleNotification(title: String, message: String) {
        if (!NotificationUtil.isAppIsInBackground(context)) {

            //BroadCast Call
//            val pushNotification = Intent(FirebaseDefine.PUSH_NOTIFICATION).apply {
//                putExtra("title", title)
//                putExtra("message", message)
//            }
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
//            NotificationUtil.playNotificationSound(context)

            //Notification Call
            val notificationUtil = NotificationUtil(context)
            notificationUtil.showNotificationMessageEmptyIntent(title, message)
        }
    }
}