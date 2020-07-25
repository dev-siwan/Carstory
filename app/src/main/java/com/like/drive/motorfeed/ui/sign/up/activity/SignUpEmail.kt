package com.like.drive.motorfeed.ui.sign.up.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivitySignUpEmailBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.sign.up.viewmodel.SignUpViewModel
import org.koin.android.ext.android.inject

class SignUpEmail : BaseActivity<ActivitySignUpEmailBinding>(R.layout.activity_sign_up_email) {

    private val viewModel:SignUpViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withViewModel()

    }

    override fun onBinding(dataBinding: ActivitySignUpEmailBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
    }

    private fun withViewModel(){
        with(viewModel){
            complete()
            error()
            fieldWarning()
            isLoading()
        }
    }

    private fun SignUpViewModel.fieldWarning(){
        fieldWarning.observe(this@SignUpEmail, Observer {
            showShortToast(it.message())
        })
    }

    private fun SignUpViewModel.complete() {
        completeEvent.observe(this@SignUpEmail, Observer {
            Intent(application, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run {
                application.startActivity(this)
                finish()
            }
        })
    }

    private fun SignUpViewModel.isLoading(){
        isLoading.observe(this@SignUpEmail, Observer {
            if(it) loadingProgress.show() else loadingProgress.dismiss()

        })
    }

    private fun SignUpViewModel.error(){
        errorEvent.observe(this@SignUpEmail, Observer {
            showShortToast(getString(R.string.sign_up_error))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loadingProgress.isShowing) {
            loadingProgress.dismiss()
        }

    }
}