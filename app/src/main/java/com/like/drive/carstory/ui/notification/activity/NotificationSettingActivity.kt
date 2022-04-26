package com.like.drive.carstory.ui.notification.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.ActivityNotificationSettingBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.notification.viewmodel.NotificationSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_notification_setting.*

@AndroidEntryPoint
class NotificationSettingActivity :
    BaseActivity<ActivityNotificationSettingBinding>(R.layout.activity_notification_setting) {

    val viewModel: NotificationSettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onBinding(dataBinding: ActivityNotificationSettingBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
    }

}