package com.like.drive.motorfeed.ui.splash.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.firebase.perf.internal.RemoteConfigManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.get
import com.like.drive.motorfeed.BuildConfig
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivitySplashBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showLongToast
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.sign.`in`.activity.SignInActivity
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashCompleteType
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashErrorType
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashViewModel
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val viewModel : SplashViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setRemoteConfig()
        withViewModel()
    }

    private fun withViewModel(){
        with(viewModel){
            complete()
            error()
        }
    }

 /*   private fun setRemoteConfig(){
        remoteConfig.fetchAndActivate().addOnCompleteListener(this){task ->
            if (task.isSuccessful) {
                showShortToast("Fetch and activate succeeded")
                try {
                    val mLatestVersionCode =remoteConfig["app_latest_version_code"].asLong()
                    if (mLatestVersionCode > BuildConfig.VERSION_CODE) {
                        showShortToast("리모트컨피그 적용 완료")
                    }
                }catch (e:FirebaseRemoteConfigException){
                    showShortToast(e.message?:"")
                }
            } else {
               showShortToast("Fetch failed")
            }
        }
    }*/

    private fun SplashViewModel.complete(){
        completeEvent.observe(this@SplashActivity, Observer {
            when(it){
                SplashCompleteType.FEED->{
                    showShortToast("피드로 간다 !")
                }
                else->{ startAct(SignInActivity::class)}
            }
        })
    }

    private fun SplashViewModel.error(){
        errorEvent.observe(this@SplashActivity, Observer {
            when(it){
                SplashErrorType.VERSION_CHECK_ERROR->showShortToast("버전체크 에러")
                SplashErrorType.MOTOR_TYPE_ERROR->showShortToast("모터타입 에러")
                SplashErrorType.USER_ERROR ->showShortToast("유저 에러")
                else->{showShortToast("유저 없음")}
            }
        })
    }


}