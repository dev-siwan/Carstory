package com.like.drive.motorfeed.ui.more.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.define.RequestDefine
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.databinding.FragmentMoreBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.binder.setProfileImg
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.feed.list.activity.FeedListActivity
import com.like.drive.motorfeed.ui.profile.activity.ProfileActivity
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.layout_more_profile.view.*

class MoreFragment : BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {

    override fun onBind(dataBinding: FragmentMoreBinding) {
        super.onBind(dataBinding)

    }

    override fun onBindAfter(dataBinding: FragmentMoreBinding) {
        super.onBindAfter(dataBinding)

        dataBinding.incProfile.containerProfileImg.setOnClickListener {
            startActForResult(ProfileActivity::class, RequestDefine.TO_PROFILE_ACTIVITY)
        }
        dataBinding.containerMyFeed.containerMoreItem.setOnClickListener {
            startAct(FeedListActivity::class, Bundle().apply {
                putParcelable(FeedListActivity.FEED_DATE_KEY, UserInfo.userInfo)
            })
        }
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