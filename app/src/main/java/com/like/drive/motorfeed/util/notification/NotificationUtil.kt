package com.like.drive.motorfeed.util.notification

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.define.FirebaseDefine
import com.like.drive.motorfeed.ui.splash.activity.SplashActivity

class NotificationUtil(private val context: Context) {

    fun showNotificationMessageEmptyIntent(title: String, message: String) {
        if (message.isBlank()) return

        val intent = Intent(context, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("Notification", true)
        }
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        showNotification(getBuilder(), title, message, pendingIntent)
    }

    @Suppress("DEPRECATION")
    private fun getBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, FirebaseDefine.CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context)
        }
    }

    private fun showNotification(
        mBuilder: NotificationCompat.Builder,
        title: String,
        message: String,
        pendingIntent: PendingIntent
    ) {
        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.addLine(message)
        val notification = mBuilder
            .setTicker(title)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(inboxStyle)
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(FirebaseDefine.NOTIFICATION_ID, notification)
    }

    companion object {

        fun playNotificationSound(context: Context) {
            try {
                val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(context, alarmSound)
                ringtone.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun clearNotifications(context: Context) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }

        fun isAppIsInBackground(context: Context): Boolean {
            var isInBackground = true

            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningProcess = activityManager.runningAppProcesses
            for (processInfo in runningProcess) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }

            return isInBackground
        }
    }

}