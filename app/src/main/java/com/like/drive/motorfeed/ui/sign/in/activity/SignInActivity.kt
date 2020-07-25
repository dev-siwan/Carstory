package com.like.drive.motorfeed.ui.sign.`in`.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivitySignInBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.sign.`in`.viewmodel.SignInErrorType
import com.like.drive.motorfeed.ui.sign.`in`.viewmodel.SignInViewModel
import com.like.drive.motorfeed.ui.sign.up.activity.SignUpEmail
import org.koin.android.ext.android.inject


class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val viewModel:SignInViewModel by inject()
    private val loginManager = LoginManager.getInstance()
    private val callbackManager = CallbackManager.Factory.create();
    private val facebookLoginCallback by lazy{ object:FacebookCallback<LoginResult>{
        override fun onSuccess(result: LoginResult?) {
            result?.let {
                viewModel.handleFacebookAccessToken(it.accessToken)
            }?:showShortToast(getString(R.string.facebook_error))

        }

        override fun onCancel() {
            showShortToast(getString(R.string.facebook_cancel))
        }

        override fun onError(error: FacebookException?) {
            showShortToast(getString(R.string.facebook_error))
        }

    }}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivitySignInBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
    }


    private fun withViewModel(){
        with(viewModel){
            complete()
            error()
            facebookLogin()
            moveToSignUp()
            isLoading()
        }
    }

    private fun SignInViewModel.complete(){
        completeEvent.observe(this@SignInActivity, Observer {
            startAct(MainActivity::class)
            finish()
        })
    }

    private fun SignInViewModel.error(){
        errorEvent.observe(this@SignInActivity, Observer {
            when(it){
                SignInErrorType.LOGIN_ERROR -> showShortToast(getString(R.string.login_error))
                SignInErrorType.USER_BAN->showShortToast(getString(R.string.user_ban))
                SignInErrorType.USER_ERROR->showShortToast(getString(R.string.user_error))
                else ->showShortToast(getString(R.string.facebook_error))
            }
        })
    }

    private fun SignInViewModel.isLoading(){
        isLoading.observe(this@SignInActivity, Observer {
            if(it)loadingProgress.show() else loadingProgress.dismiss()

        })
    }


    private fun SignInViewModel.facebookLogin(){
        loginFacebookClickEvent.observe(this@SignInActivity, Observer {
            loginManager.logInWithReadPermissions(this@SignInActivity, listOf("email", "public_profile"))
            loginManager.registerCallback(callbackManager,facebookLoginCallback)
        })
    }

    private fun SignInViewModel.moveToSignUp(){
        moveToSignUpEvent.observe(this@SignInActivity, Observer {
            startAct(SignUpEmail::class)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}