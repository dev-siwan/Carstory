package com.like.drive.motorfeed.ui.sign.`in`.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
import com.like.drive.motorfeed.ui.sign.`in`.viewmodel.SignInViewModel
import org.koin.android.ext.android.inject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val viewModel:SignInViewModel by inject()
    private val loginManager = LoginManager.getInstance()
    private val callbackManager = CallbackManager.Factory.create();
    private val facebookLoginCallback by lazy{ object:FacebookCallback<LoginResult>{
        override fun onSuccess(result: LoginResult?) {
            result?.let {
                viewModel.handleFacebookAccessToken(it.accessToken)
            }?:showShortToast("페이스북 로그인 리절트값 에러")

        }

        override fun onCancel() {
            showShortToast("페이스북 로그인 취소")
        }

        override fun onError(error: FacebookException?) {
            showShortToast("페이스북 에러")
        }

    }}

    @SuppressLint("PackageManagerGetSignatures")
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
            facebookLogin()
        }
    }


    private fun SignInViewModel.facebookLogin(){
        loginEmailClickEvent.observe(this@SignInActivity, Observer {
            loginManager.logInWithReadPermissions(this@SignInActivity, listOf("email", "public_profile"))
            loginManager.registerCallback(callbackManager,facebookLoginCallback)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}