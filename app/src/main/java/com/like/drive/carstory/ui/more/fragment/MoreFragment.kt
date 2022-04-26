package com.like.drive.carstory.ui.more.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.like.drive.carstory.R
import com.like.drive.carstory.common.define.RequestDefine
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.databinding.FragmentMoreBinding
import com.like.drive.carstory.ui.base.BaseFragment
import com.like.drive.carstory.ui.base.ext.openWebBrowser
import com.like.drive.carstory.ui.base.ext.startActForResult
import com.like.drive.carstory.ui.board.list.activity.BoardListActivity
import com.like.drive.carstory.ui.more.viewmodel.MoreViewModel
import com.like.drive.carstory.ui.notice.list.activity.NoticeListActivity
import com.like.drive.carstory.ui.notification.activity.NotificationSettingActivity
import com.like.drive.carstory.ui.profile.activity.ProfileActivity
import com.like.drive.carstory.ui.report.list.activity.ReportActivity
import com.like.drive.carstory.ui.terms.TermsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {

    private val viewModel: MoreViewModel by viewModels()
    @Inject lateinit var userInfo: UserInfo

    override fun onBind(dataBinding: FragmentMoreBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
    }

    override fun onBindAfter(dataBinding: FragmentMoreBinding) {
        super.onBindAfter(dataBinding)

        moreItemClickListener(dataBinding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInfo()
    }

    private fun moreItemClickListener(dataBinding: FragmentMoreBinding) {
        dataBinding.incProfile.btnProfileSetting.setOnClickListener {
            startActForResult(ProfileActivity::class, RequestDefine.TO_PROFILE_ACTIVITY)
        }
        dataBinding.containerMyFeed.containerMoreItem.setOnClickListener {
            startAct(BoardListActivity::class, Bundle().apply {
                putParcelable(BoardListActivity.BOARD_DATA_KEY, userInfo.userInfo)
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