package com.like.drive.motorfeed.ui.notification.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentNotificationBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.notification.adapter.NotificationAdapter
import com.like.drive.motorfeed.ui.notification.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_notification.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {

    private val viewModel: NotificationViewModel by viewModel()

    override fun onBind(dataBinding: FragmentNotificationBinding) {
        super.onBind(dataBinding)

        viewModel.init()
        withViewModel()
    }

    private fun withViewModel() {

        with(viewModel) {
            getList()
        }
    }

    private fun NotificationViewModel.getList() {
        notificationList.observe(viewLifecycleOwner, Observer {
            rvNotification.adapter = NotificationAdapter().apply {
                list.addAll(it)
            }
        })
    }

}