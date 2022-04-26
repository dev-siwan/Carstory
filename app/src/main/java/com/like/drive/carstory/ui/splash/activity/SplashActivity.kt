package com.like.drive.carstory.ui.splash.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.like.drive.carstory.R
import com.like.drive.carstory.common.define.FirebaseDefine
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.databinding.ActivitySplashBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.base.ext.startAct
import com.like.drive.carstory.ui.main.activity.MainActivity
import com.like.drive.carstory.ui.profile.activity.ProfileActivity
import com.like.drive.carstory.ui.sign.`in`.activity.SignInActivity
import com.like.drive.carstory.ui.splash.viewmodel.SplashCompleteType
import com.like.drive.carstory.ui.splash.viewmodel.SplashErrorType
import com.like.drive.carstory.ui.splash.viewmodel.SplashViewModel
import com.like.drive.carstory.util.photo.PickImageUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val viewModel: SplashViewModel by viewModels()
    private var notificationSendData: NotificationSendData? = null
    private var shareBoardId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                getDynamicLink()?.let {
                    shareBoardId = it.toString().substringAfterLast("/")
                }

            }

            getNotificationData()
            withViewModel()
            deleteTempImageDirectory()
        }
    }

    private fun getNotificationData() {
        notificationSendData =
            intent.getParcelableExtra(FirebaseDefine.PUSH_NOTIFICATION)
    }

    private suspend fun getDynamicLink(): Uri? {

        return Firebase.dynamicLinks.getDynamicLink(intent).let {
            if (it.isSuccessful) {
                it.await().link
            } else {
                null
            }
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            completeDeleteDirectory()
            complete()
            error()
            emptyNickName()
        }
    }

    private fun SplashViewModel.completeDeleteDirectory() {
        completeDeleteDirectory.observe(this@SplashActivity, Observer {
            versionCheck()
        })
    }

    private fun SplashViewModel.complete() {
        completeEvent.observe(this@SplashActivity, Observer {
            when (it) {
                SplashCompleteType.HOME -> {

                    shareBoardId?.let {
                        Intent(this@SplashActivity, MainActivity::class.java).apply {
                            putExtra(FirebaseDefine.SHARE_BOARD_ID, it)
                            addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        }.run {
                            startActivity(this)
                        }
                        finish()
                        return@Observer
                    }

                    startAct(MainActivity::class, Bundle().apply {
                        notificationSendData?.let { notificationData ->
                            putParcelable(
                                FirebaseDefine.PUSH_NOTIFICATION,
                                notificationData
                            )
                        }
                        finish()
                    })

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

    private fun moveToActivity(clazz: KClass<*>) {
        startAct(clazz)
        finish()
    }

    private fun deleteTempImageDirectory() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.deleteDirectory(getExternalFilesDir(null)?.absolutePath + PickImageUtil.IMAGE_PATH)
        } else {
            viewModel.versionCheck()
        }
    }

}