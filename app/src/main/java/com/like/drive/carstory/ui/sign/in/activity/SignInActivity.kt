package com.like.drive.carstory.ui.sign.`in`.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.like.drive.carstory.R
import com.like.drive.carstory.data.intro.getIntroMessage
import com.like.drive.carstory.databinding.ActivitySignInBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.base.ext.startAct
import com.like.drive.carstory.ui.main.activity.MainActivity
import com.like.drive.carstory.ui.profile.activity.ProfileActivity
import com.like.drive.carstory.ui.sign.`in`.viewmodel.SignInErrorType
import com.like.drive.carstory.ui.sign.`in`.viewmodel.SignInViewModel
import com.like.drive.carstory.ui.sign.up.activity.SignUpEmail
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val viewModel: SignInViewModel by inject()
    private val loginManager = LoginManager.getInstance()
    private val callbackManager = CallbackManager.Factory.create()
    private val facebookLoginCallback by lazy {
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                result?.let {
                    viewModel.handleFacebookAccessToken(it.accessToken)
                } ?: showShortToast(getString(R.string.facebook_error))

            }

            override fun onCancel() {
                showShortToast(getString(R.string.facebook_cancel))
            }

            override fun onError(error: FacebookException?) {
                showShortToast(getString(R.string.facebook_error))
            }

        }
    }

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        error?.let {
            showShortToast(it.message ?: "")
        }

        token?.let {
            viewModel.createKaKaoToken(it.accessToken)
        }
    }

    private var introIndex = 0

    private val introList by lazy { getIntroMessage(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        withViewModel()
    }

    override fun onBinding(dataBinding: ActivitySignInBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.tvIntro.run {
            setAnimation()
        }

        dataBinding.tvKaKaoLogin.setOnClickListener {
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

    }

    private fun TextView.setAnimation() {
        lifecycleScope.launch {
            if (introIndex == (introList.size - 1)) {
                introIndex = 0
            }
            visibleAnimation()
            text = introList[introIndex]
            delay(2000)
            invisibleAnimation()
            introIndex++

            setAnimation()
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            complete()
            error()
            facebookLogin()
            moveToSignUp()
            isLoading()
            emptyNickName()
        }
    }

    private fun SignInViewModel.complete() {
        completeEvent.observe(this@SignInActivity, Observer {
            startAct(MainActivity::class)
            finish()
        })
    }

    private fun SignInViewModel.error() {
        errorEvent.observe(this@SignInActivity, Observer {
            when (it) {
                SignInErrorType.LOGIN_ERROR -> showShortToast(getString(R.string.login_error))
                SignInErrorType.USER_BAN -> showShortToast(getString(R.string.user_ban))
                SignInErrorType.USER_ERROR -> showShortToast(getString(R.string.user_error))
                else -> showShortToast(getString(R.string.facebook_error))
            }
        })
    }

    private fun SignInViewModel.isLoading() {
        isLoading.observe(this@SignInActivity, Observer {
            if (it) loadingProgress.show() else loadingProgress.dismiss()
        })
    }

    private fun SignInViewModel.facebookLogin() {
        loginFacebookClickEvent.observe(this@SignInActivity, Observer {
            loginManager.logInWithReadPermissions(
                this@SignInActivity,
                listOf("email", "public_profile")
            )
            loginManager.registerCallback(callbackManager, facebookLoginCallback)
        })
    }

    private fun SignInViewModel.moveToSignUp() {
        moveToSignUpEvent.observe(this@SignInActivity, Observer {
            startAct(SignUpEmail::class)
        })
    }

    private fun SignInViewModel.emptyNickName() {
        emptyNickNameEvent.observe(this@SignInActivity, Observer {
            startAct(ProfileActivity::class)
            showShortToast(R.string.nick_name_null_error)
        })
    }

    private fun TextView.visibleAnimation() {
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.apply {
            duration = 1000
            repeatCount = 1
            repeatMode = Animation.REVERSE
        }.run {
            startAnimation(this)
        }

    }

    private fun TextView.invisibleAnimation() {
        ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).run {
            duration = 2000
            start()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}