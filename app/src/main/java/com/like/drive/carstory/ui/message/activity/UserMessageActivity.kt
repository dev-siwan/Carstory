package com.like.drive.carstory.ui.message.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.ActivityUserMessageBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.dividerItemDecoration
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.dialog.ConfirmDialog
import com.like.drive.carstory.ui.message.adapter.UserMessageTypeAdapter
import com.like.drive.carstory.ui.message.viewmodel.UserMessageViewModel
import kotlinx.android.synthetic.main.activity_user_message.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserMessageActivity :
    BaseActivity<ActivityUserMessageBinding>(R.layout.activity_user_message) {

    private val viewModel: UserMessageViewModel by viewModel()
    private val userTypeAdapter by lazy { UserMessageTypeAdapter(viewModel) }
    private var isUserBan: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        withViewModel()
    }

    private fun initData() {
        val userId = intent.getStringExtra(USER_ID_KEY)
        isUserBan = intent.getBooleanExtra(IS_USER_BAN_KEY, false)

        userId?.let {
            viewModel.init(it, isUserBan ?: false)
        }

        tvTitle.text =
            if (isUserBan == true) getString(R.string.user_ban_text) else getString(R.string.user_message_text)

    }

    override fun onBinding(dataBinding: ActivityUserMessageBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvMessageTypeList.run {
            adapter = userTypeAdapter
            addItemDecoration(dividerItemDecoration())
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            typeList()
            confirm()
            error()
            complete()
            loading()
        }
    }

    private fun UserMessageViewModel.typeList() {
        userMessageType.observe(this@UserMessageActivity, Observer {
            userTypeAdapter.submitList(it)
        })
    }

    private fun UserMessageViewModel.confirm() {
        confirmMessage.observe(this@UserMessageActivity, Observer {
            ConfirmDialog.newInstance(
                message = getString(R.string.user_message_confirm, it)
            ).apply {
                confirmAction = { sendRequestUserStatus(it) }
            }.show(supportFragmentManager, ConfirmDialog.TAG)
        })
    }

    private fun UserMessageViewModel.complete() {
        completeEvent.observe(this@UserMessageActivity, Observer {
            showShortToast(it)
            finish()
        })
    }

    private fun UserMessageViewModel.error() {
        errorEvent.observe(this@UserMessageActivity, Observer {
            showShortToast(it)
        })
    }

    private fun UserMessageViewModel.loading() {
        loading.observe(this@UserMessageActivity, Observer {
            if (it) loadingProgress.show() else loadingProgress.dismiss()
        })
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
    }

    companion object {
        const val USER_ID_KEY = "uidKey"
        const val IS_USER_BAN_KEY = "isUserBanKey"
    }
}