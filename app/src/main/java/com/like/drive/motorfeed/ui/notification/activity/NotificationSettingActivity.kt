package com.like.drive.motorfeed.ui.notification.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityNotificationSettingBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.notification.viewmodel.NotificationSettingViewModel
import kotlinx.android.synthetic.main.activity_notification_setting.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationSettingActivity :
    BaseActivity<ActivityNotificationSettingBinding>(R.layout.activity_notification_setting) {

    val viewModel: NotificationSettingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withViewModel()
        initView()
    }

    override fun onBinding(dataBinding: ActivityNotificationSettingBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
    }

    private fun withViewModel() {
        with(viewModel) {
            noticeSubscribe()
            commentSubscribe()
        }
    }

    private fun NotificationSettingViewModel.noticeSubscribe() {
        noticeCheck.observe(this@NotificationSettingActivity, Observer {
            setNoticeSubscribe(it)
        })
    }

    private fun NotificationSettingViewModel.commentSubscribe() {
        commentCheck.observe(this@NotificationSettingActivity, Observer {
            setCommentSubscribe(it)
        })
    }

}