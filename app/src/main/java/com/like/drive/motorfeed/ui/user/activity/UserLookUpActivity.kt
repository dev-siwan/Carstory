package com.like.drive.motorfeed.ui.user.activity

import android.os.Bundle
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.databinding.ActivityUserLookUpBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.user.viewmodel.UserLookUpViewModel
import kotlinx.android.synthetic.main.activity_user_look_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserLookUpActivity :
    BaseActivity<ActivityUserLookUpBinding>(R.layout.activity_user_look_up) {

    private val viewModel: UserLookUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
        initView()
    }

    override fun onBinding(dataBinding: ActivityUserLookUpBinding) {
        dataBinding.vm = viewModel
    }

    private fun getData() {
        intent.getParcelableExtra<UserData>(USER_DATA_KEY)?.let {
            viewModel.init(it)
        }
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
    }

    companion object {
        const val USER_DATA_KEY = "UserDataKey"
    }
}