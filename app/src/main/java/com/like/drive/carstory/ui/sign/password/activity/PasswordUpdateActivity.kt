package com.like.drive.carstory.ui.sign.password.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.carstory.R
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.databinding.ActivityPasswordUpdateBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.base.ext.startAct
import com.like.drive.carstory.ui.dialog.AlertDialog
import com.like.drive.carstory.ui.sign.`in`.activity.SignInActivity
import com.like.drive.carstory.ui.sign.password.viewmodel.PasswordUpdateViewModel
import kotlinx.android.synthetic.main.activity_password_update.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordUpdateActivity :
    BaseActivity<ActivityPasswordUpdateBinding>(R.layout.activity_password_update) {

    val viewModel: PasswordUpdateViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withViewModel()
        initView()
    }

    override fun onBinding(dataBinding: ActivityPasswordUpdateBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) {
            finish()
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            warningField()
            complete()
            error()
            loading()
        }
    }

    private fun PasswordUpdateViewModel.warningField() {
        fieldWarning.observe(this@PasswordUpdateActivity, Observer {
            showShortToast(it.message())
        })
    }

    private fun PasswordUpdateViewModel.complete() {
        completedEvent.observe(this@PasswordUpdateActivity, Observer {
            AlertDialog.newInstance(
                title = getString(R.string.password_update_dialog_title),
                message = getString(R.string.password_complete_dialog_desc)
            )
                .apply {
                    isCancelable = false
                    action = {
                        UserInfo.signOut()
                        finishAffinity()
                        startAct(SignInActivity::class)
                    }
                }.show(supportFragmentManager, AlertDialog.TAG)
        })
    }

    private fun PasswordUpdateViewModel.error() {
        errorEvent.observe(this@PasswordUpdateActivity, Observer {
            AlertDialog.newInstance(
                title = getString(R.string.password_update_dialog_title),
                message = getString(it)
            )
                .apply {
                    isCancelable = true
                }.show(supportFragmentManager, AlertDialog.TAG)
        })
    }

    private fun PasswordUpdateViewModel.loading() {
        isLoading.observe(this@PasswordUpdateActivity, Observer {
            loadingStatus(it)
        })
    }

}