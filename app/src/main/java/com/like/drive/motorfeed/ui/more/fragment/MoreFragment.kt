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
import com.like.drive.motorfeed.ui.dialog.AlertDialog
import com.like.drive.motorfeed.ui.feed.list.activity.FeedListActivity
import com.like.drive.motorfeed.ui.more.viewmodel.MoreViewModel
import com.like.drive.motorfeed.ui.profile.activity.ProfileActivity
import com.like.drive.motorfeed.ui.sign.password.activity.PasswordUpdateActivity
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.layout_more_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreFragment : BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {

    private val viewModel: MoreViewModel by viewModel()

    override fun onBind(dataBinding: FragmentMoreBinding) {
        super.onBind(dataBinding)

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
            startAct(FeedListActivity::class, Bundle().apply {
                putParcelable(FeedListActivity.FEED_DATE_KEY, UserInfo.userInfo)
            })
        }
        dataBinding.containerPasswordReset.containerMoreItem.setOnClickListener {
            startAct(PasswordUpdateActivity::class)
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
                            tvIntro.text = it.intro
                        }

                    }

                }
            }
        }

    }
}