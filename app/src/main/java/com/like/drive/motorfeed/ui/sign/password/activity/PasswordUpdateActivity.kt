package com.like.drive.motorfeed.ui.sign.password.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.databinding.ActivityPasswordUpdateBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.dialog.AlertDialog
import com.like.drive.motorfeed.ui.sign.`in`.activity.SignInActivity
import com.like.drive.motorfeed.ui.sign.password.viewmodel.PasswordUpdateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordUpdateActivity :
    BaseActivity<ActivityPasswordUpdateBinding>(R.layout.activity_password_update) {

    val viewModel: PasswordUpdateViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivityPasswordUpdateBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
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
                message = getString(R.string.password_error_dialog_desc)
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

    private fun PasswordUpdateViewModel.loading() {
        isLoading.observe(this@PasswordUpdateActivity, Observer {
            loadingStatus(it)
        })
    }

}