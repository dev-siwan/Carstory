package com.like.drive.motorfeed.ui.more.fragment

import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.databinding.FragmentMoreBinding
import com.like.drive.motorfeed.ui.base.BaseFragment

class MoreFragment : BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {

    override fun onBind(dataBinding: FragmentMoreBinding) {
        super.onBind(dataBinding)

        dataBinding.userInfo = UserInfo
    }
}