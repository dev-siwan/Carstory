package com.like.drive.carstory.ui.more.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.like.drive.carstory.R
import com.like.drive.carstory.common.define.RequestDefine
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.databinding.FragmentMoreBinding
import com.like.drive.carstory.ui.base.BaseFragment
import com.like.drive.carstory.ui.base.binder.setProfileImg
import com.like.drive.carstory.ui.base.ext.openWebBrowser
import com.like.drive.carstory.ui.base.ext.startActForResult
import com.like.drive.carstory.ui.board.list.activity.BoardListActivity
import com.like.drive.carstory.ui.more.viewmodel.MoreViewModel
import com.like.drive.carstory.ui.notice.list.activity.NoticeListActivity
import com.like.drive.carstory.ui.notification.activity.NotificationSettingActivity
import com.like.drive.carstory.ui.profile.activity.ProfileActivity
import com.like.drive.carstory.ui.report.list.activity.ReportActivity
import com.like.drive.carstory.ui.terms.TermsActivity
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.layout_more_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreFragment : BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {

    private val viewModel: MoreViewModel by viewModel()

    override fun onBind(dataBinding: FragmentMoreBinding) {
        dataBinding.vm = viewModel
    }

    override fun onBindAfter(dataBinding: FragmentMoreBinding) {
        super.onBindAfter(dataBinding)

        moreItemClickListener(dataBinding)
    }

    private fun moreItemClickListener(dataBinding: FragmentMoreBinding) {
        dataBinding.incProfile.btnProfileSetting.setOnClickListener {
            startActForResult(ProfileActivity::class, RequestDefine.TO_PROFILE_ACTIVITY)
        }
        dataBinding.containerMyFeed.containerMoreItem.setOnClickListener {
            startAct(BoardListActivity::class, Bundle().apply {
                putParcelable(BoardListActivity.BOARD_DATA_KEY, UserInfo.userInfo)
            })
        }

        dataBinding.containerNotificationSetting.containerMoreItem.setOnClickListener {
            startAct(NotificationSettingActivity::class)
        }

        dataBinding.containerNotice.containerMoreItem.setOnClickListener {
            startAct(NoticeListActivity::class)
        }

        dataBinding.containerTermsUse.containerMoreItem.setOnClickListener {
            startAct(TermsActivity::class, Bundle().apply {
                putString(TermsActivity.TERMS_KEY, TermsActivity.TERMS_USE_VALUE)
            })
        }


        dataBinding.containerTermsPrivacy.containerMoreItem.setOnClickListener {
            startAct(TermsActivity::class, Bundle().apply {
                putString(TermsActivity.TERMS_KEY, TermsActivity.TERMS_PRIVACY_VALUE)
            })
        }

        dataBinding.containerQuestions.containerMoreItem.setOnClickListener {
            requireActivity().openWebBrowser("https://forms.gle/vSgHbpE8rGRADnKv9")
        }

        dataBinding.containerReportPage.containerMoreItem.setOnClickListener {
            startAct(ReportActivity::class)
        }

    }

}