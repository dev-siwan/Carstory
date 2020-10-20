package com.like.drive.carstory.ui.notification.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.like.drive.carstory.R
import com.like.drive.carstory.data.notification.NotificationType
import com.like.drive.carstory.databinding.FragmentNotificationBinding
import com.like.drive.carstory.ui.base.BaseFragment
import com.like.drive.carstory.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.carstory.ui.dialog.ConfirmDialog
import com.like.drive.carstory.ui.main.activity.MainActivity
import com.like.drive.carstory.ui.notice.detail.activity.NoticeDetailActivity
import com.like.drive.carstory.ui.notification.activity.NotificationSettingActivity
import com.like.drive.carstory.ui.notification.adapter.NotificationAdapter
import com.like.drive.carstory.ui.notification.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {

    private val viewModel: NotificationViewModel by viewModel()
    private val adapter by lazy { NotificationAdapter(viewModel) }

    override fun onBind(dataBinding: FragmentNotificationBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.rvNotification.apply {
            adapter = this@NotificationFragment.adapter

            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                    ContextCompat.getDrawable(requireContext(), R.drawable.divider_board_list)
                        ?.let {
                            setDrawable(it)
                        }
                }

            addItemDecoration(dividerItemDecoration)

        }

        dataBinding.ivNotificationSetting.setOnClickListener {
            startAct(NotificationSettingActivity::class)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.init()
        withViewModel()

        (requireActivity() as MainActivity).navBottomView.removeBadge(R.id.action_notification)
    }

    private fun withViewModel() {

        with(viewModel) {
            getList()
            moveToPage()
            removeItemListener()
        }
    }

    private fun NotificationViewModel.getList() {
        notificationList.observe(viewLifecycleOwner, Observer {
            adapter.apply {
                list.clear()
                list.addAll(it)
                notifyDataSetChanged()
            }
        })
    }

    private fun NotificationViewModel.moveToPage() {
        clickItemEvent.observe(viewLifecycleOwner, Observer {
            when (it.notificationType) {
                NotificationType.COMMENT.value, NotificationType.RE_COMMENT.value -> {
                    startAct(BoardDetailActivity::class, Bundle().apply {
                        putString(BoardDetailActivity.KEY_BOARD_ID, it.bid)
                    })
                }
                NotificationType.NOTICE.value -> {
                    startAct(NoticeDetailActivity::class, Bundle().apply {
                        putString(NoticeDetailActivity.NOTICE_ID_KEY, it.nid)
                    })
                }
            }
        })
    }

    private fun NotificationViewModel.removeItemListener() {
        removeItemEvent.observe(viewLifecycleOwner, Observer {
            ConfirmDialog.newInstance(message = getString(R.string.remove_notification_item_desc))
                .apply {
                    confirmAction = { removeNotificationItem(it) }
                }.show(requireActivity().supportFragmentManager, ConfirmDialog.TAG)
        })
    }

}