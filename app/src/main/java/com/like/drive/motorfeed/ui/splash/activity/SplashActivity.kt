package com.like.drive.motorfeed.ui.splash.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.define.FirebaseDefine
import com.like.drive.motorfeed.data.notification.NotificationSendData
import com.like.drive.motorfeed.databinding.ActivitySplashBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.profile.activity.ProfileActivity
import com.like.drive.motorfeed.ui.sign.`in`.activity.SignInActivity
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashCompleteType
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashErrorType
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashViewModel
import org.koin.android.ext.android.inject
import kotlin.reflect.KClass

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val viewModel: SplashViewModel by inject()
    private var notificationSendData: NotificationSendData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setRemoteConfig()
        getNotificationData()
        withViewModel()
    }

    private fun getNotificationData() {
        notificationSendData =
            intent.getParcelableExtra(FirebaseDefine.PUSH_NOTIFICATION)
    }

    private fun withViewModel() {
        with(viewModel) {
            complete()
            error()
            emptyNickName()
        }
    }

    private fun SplashViewModel.complete() {
        completeEvent.observe(this@SplashActivity, Observer {
            when (it) {
                SplashCompleteType.HOME -> {
                    startAct(MainActivity::class, Bundle().apply {
                        notificationSendData?.let { notificationData ->
                            putParcelable(
                                FirebaseDefine.PUSH_NOTIFICATION,
                                notificationData
                            )
                        }
                    })
                    finish()
                }
                else -> {
                    startAct(SignInActivity::class)
                    finish()
                }
            }
        })
    }

    private fun SplashViewModel.error() {
        errorEvent.observe(this@SplashActivity, Observer {
            when (it) {
                SplashErrorType.VERSION_CHECK_ERROR -> showShortToast("버전체크 에러")
                SplashErrorType.MOTOR_TYPE_ERROR -> showShortToast("모터타입 에러")
                SplashErrorType.USER_ERROR -> {
                    showShortToast(getString(R.string.login_error))
                    moveToActivity(SignInActivity::class)
                }
                SplashErrorType.USER_BAN -> {
                    showShortToast(getString(R.string.user_ban))
                    moveToActivity(SignInActivity::class)
                }
                else -> {
                    showShortToast(getString(R.string.user_error))
                    moveToActivity(SignInActivity::class)
                }
            }
        })
    }

    private fun SplashViewModel.emptyNickName() {
        emptyNickNameEvent.observe(this@SplashActivity, Observer {
            moveToActivity(ProfileActivity::class)
            showShortToast(R.string.nick_name_null_error)
        })
    }

    private fun moveToActivity(clazz: KClass<*>, data: NotificationSendData? = null) {
        startAct(clazz)
        finish()
    }

}