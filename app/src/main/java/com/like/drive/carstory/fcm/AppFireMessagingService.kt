package com.like.drive.carstory.fcm

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.like.drive.carstory.R
import com.like.drive.carstory.common.define.FirebaseDefine
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.data.notification.NotificationType
import com.like.drive.carstory.repository.notification.NotificationRepository
import com.like.drive.carstory.ui.base.ext.toDataClass
import com.like.drive.carstory.util.notification.NotificationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class AppFireMessagingService : FirebaseMessagingService(), KoinComponent {

    private val repo: NotificationRepository by inject()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessaging: RemoteMessage) {

        remoteMessaging.data.toDataClass<NotificationSendData>()?.let {
            handleNotification(it)
        }

    }

    private fun handleNotification(notificationSendData: NotificationSendData) {

        notificationSendData.let {
            val title =
                NotificationType.values().find { type -> type.value == it.notificationType }?.title
                    ?: applicationContext.getString(R.string.app_name_kr)
            val message = it.message ?: ""

            if (!NotificationUtil.isAppIsInBackground(applicationContext)) {
                val intent = Intent(FirebaseDefine.PUSH_NOTIFICATION).apply {
                    putExtra(FirebaseDefine.PUSH_EXTRA_KEY, title)
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                NotificationUtil.playNotificationSound(this)

            } else {

                val notificationUtil = NotificationUtil(applicationContext)
                notificationUtil.showNotificationMessageEmptyIntent(it)

            }

            CoroutineScope(Dispatchers.IO).launch {
                repo.insert(it)
            }

        }
    }
}