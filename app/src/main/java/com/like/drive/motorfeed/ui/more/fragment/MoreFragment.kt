package com.like.drive.motorfeed.ui.more.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.define.RequestDefine
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.databinding.FragmentMoreBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.binder.setProfileImg
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.board.list.activity.BoardListActivity
import com.like.drive.motorfeed.ui.dialog.AlertDialog
import com.like.drive.motorfeed.ui.more.viewmodel.MoreViewModel
import com.like.drive.motorfeed.ui.notice.list.activity.NoticeListActivity
import com.like.drive.motorfeed.ui.notification.activity.NotificationSettingActivity
import com.like.drive.motorfeed.ui.profile.activity.ProfileActivity
import com.like.drive.motorfeed.ui.sign.password.activity.PasswordUpdateActivity
import com.like.drive.motorfeed.ui.terms.TermsActivity
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

        withViewModel()
        moreItemClickListener(dataBinding)
    }

    private fun moreItemClickListener(dataBinding: FragmentMoreBinding) {
        dataBinding.incProfile.containerProfileImg.setOnClickListener {
            startActForResult(ProfileActivity::class, RequestDefine.TO_PROFILE_ACTIVITY)
        }
        dataBinding.containerMyFeed.containerMoreItem.setOnClickListener {
            startAct(BoardListActivity::class, Bundle().apply {
                putParcelable(BoardListActivity.BOARD_DATE_KEY, UserInfo.userInfo)
            })
        }
        dataBinding.containerPasswordUpdate.containerMoreItem.setOnClickListener {
            startAct(PasswordUpdateActivity::class)
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
    }

    private fun withViewModel() {
        with(viewModel) {
            resetComplete()
            error()
        }
    }

    private fun MoreViewModel.resetComplete() {
        resetPasswordCompleteEvent.observe(viewLifecycleOwner, Observer {
            AlertDialog.newInstance(message = "가입 이메일에서 재설정 해주세요.").apply {
                action = { Unit }
            }.show(requireActivity().supportFragmentManager, AlertDialog.TAG)
        })
    }

    private fun MoreViewModel.error() {
        errorEvent.observe(viewLifecycleOwner, Observer {
            requireContext().showShortToast(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestDefine.TO_PROFILE_ACTIVITY -> {

                    UserInfo.userInfo?.let {

                        incProfile.apply {
                            ivProfileImg.setProfileImg(it.profileImgPath)
                            tvNick.text = it.nickName
                            tvIntro.text = if (it.intro.isNullOrBlank()) null else it.intro
                        }

                    }

                }
            }
        }

    }
}