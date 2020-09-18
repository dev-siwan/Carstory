package com.like.drive.motorfeed.ui.main.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.define.FirebaseDefine
import com.like.drive.motorfeed.databinding.ActivityMainBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.*
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.home.fragment.HomeFragment
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import com.like.drive.motorfeed.ui.sign.`in`.activity.SignInActivity
import com.like.drive.motorfeed.util.notification.NotificationUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by inject()
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var fcmBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        withViewModel()
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }


        intent.getBooleanExtra("Notification", false).run {
            if (this) {
                navBottomView.selectedItemId = R.id.action_notification
            }
        }
        initFcmBroadcastReceiver()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    override fun onBinding(dataBinding: ActivityMainBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
    }

    private fun withViewModel() {
        with(viewModel) {
            signOut()
            moveToUploadPage()
        }
    }

    private fun MainViewModel.moveToUploadPage() {
        uploadClickEvent.observe(this@MainActivity, Observer {
            startActForResult(FeedUploadActivity::class, UPLOAD_FEED_REQ)
        })
    }

    private fun MainViewModel.signOut() {
        signOutComplete.observe(this@MainActivity, Observer {
            startAct(SignInActivity::class)
            finish()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (val currentFragment = supportFragmentManager.currentNavigationFragment()) {
            is HomeFragment -> currentFragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()

        NotificationUtil.clearNotifications(this)
    }

    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(
            R.navigation.nav_main_home,
            R.navigation.nav_main_search,
            R.navigation.nav_main_notification,
            R.navigation.nav_main_user
        ) //graph들의 id
        // Setup the bottom navigation view with a list of navigation graphs
        val controller = navBottomView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.mainNavFragment, //FragmentContainerView의 id
            intent = intent
        )
        // Whenever the selected controller changes, setup the action bar.
        /*controller.observe(this, Observer { navController -> setupActionBarWithNavController(navController) })*/ //액션바 관련 코드 (액션바를 사용하지 않기 때문에 주석처리)
        currentNavController = controller
    }

    private fun initFcmBroadcastReceiver() {
        fcmBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    it.getStringExtra(FirebaseDefine.PUSH_EXTRA_KEY)?.let { title ->
                        showShortToast(title)
                        val badge = navBottomView.getOrCreateBadge(R.id.action_notification)
                        badge.isVisible = true
                    }
                }
            }

        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            fcmBroadcastReceiver,
            IntentFilter(FirebaseDefine.PUSH_NOTIFICATION)
        )
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmBroadcastReceiver)
        super.onDestroy()
    }

    companion object {
        const val UPLOAD_FEED_REQ = 1530
    }

}

