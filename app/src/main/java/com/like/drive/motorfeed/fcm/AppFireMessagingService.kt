package com.like.drive.motorfeed.fcm

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.define.FirebaseDefine
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.notification.NotificationSendData
import com.like.drive.motorfeed.data.notification.NotificationType
import com.like.drive.motorfeed.repository.notification.NotificationRepository
import com.like.drive.motorfeed.ui.base.ext.toDataClass
import com.like.drive.motorfeed.util.notification.NotificationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class AppFireMessagingService : FirebaseMessagingService(), KoinComponent {

    val context = MotorFeedApplication.getContext()
    private val repo: NotificationRepository by inject()

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessaging: RemoteMessage) {
        super.onMessageReceived(remoteMessaging)

        if (UserInfo.userInfo == null) return

        remoteMessaging.data.toDataClass<NotificationSendData>()?.let {

            CoroutineScope(Dispatchers.IO).launch {
                repo.insert(it)
                cancel()
            }

            val title =
                NotificationType.values().find { type -> type.value == it.notificationType }?.title
                    ?: context.getString(R.string.app_name_kr)
            val body = it.message ?: ""
            handleNotification(title, body)
        }

    }

    private fun handleNotification(title: String, message: String) {
        if (NotificationUtil.isAppIsInBackground(context)) {
            val notificationUtil = NotificationUtil(context)
            notificationUtil.showNotificationMessageEmptyIntent(title, message)
        } else {

            val intent = Intent(FirebaseDefine.PUSH_NOTIFICATION).apply {
                putExtra(FirebaseDefine.PUSH_EXTRA_KEY, title)
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            NotificationUtil.playNotificationSound(this)

        }
    }

}