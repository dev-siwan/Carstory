package com.like.drive.carstory.ui.main.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.like.drive.carstory.R
import com.like.drive.carstory.common.define.FirebaseDefine
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.data.notification.NotificationType
import com.like.drive.carstory.data.user.FilterData
import com.like.drive.carstory.databinding.ActivityMainBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.*
import com.like.drive.carstory.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.carstory.ui.board.upload.activity.UploadActivity
import com.like.drive.carstory.ui.dialog.AlertDialog
import com.like.drive.carstory.ui.home.fragment.HomeFragment
import com.like.drive.carstory.ui.main.viewmodel.MainViewModel
import com.like.drive.carstory.ui.notice.detail.activity.NoticeDetailActivity
import com.like.drive.carstory.ui.permission.AccessPermissionDialog
import com.like.drive.carstory.util.notification.NotificationUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModel()
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var fcmBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

        getNotificationData()
        getSharedBoardID()
        withViewModel()

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
            permission()
            userMessage()
            moveToUploadPage()
        }
    }

    private fun getNotificationData() {

        intent.getParcelableExtra<NotificationSendData>(FirebaseDefine.PUSH_NOTIFICATION)?.let {
            when (it.notificationType) {
                NotificationType.NOTICE.value -> {
                    startAct(NoticeDetailActivity::class, Bundle().apply {
                        putString(NoticeDetailActivity.NOTICE_ID_KEY, it.nid)
                    })
                }
                else -> {
                    startAct(BoardDetailActivity::class, Bundle().apply {
                        putString(BoardDetailActivity.KEY_BOARD_ID, it.bid)
                    })
                }
            }
        }
    }

    private fun getSharedBoardID() {
        intent.getStringExtra(FirebaseDefine.SHARE_BOARD_ID)?.let {
            startAct(BoardDetailActivity::class, Bundle().apply {
                putString(BoardDetailActivity.KEY_BOARD_ID, it)
            })
        }
    }

    private fun MainViewModel.moveToUploadPage() {
        uploadClickEvent.observe(this@MainActivity, Observer {
            startActForResult(UploadActivity::class, UPLOAD_FEED_REQ)
        })
    }

    private fun MainViewModel.permission() {
        showPermissionEvent.observe(this@MainActivity, Observer {
            AccessPermissionDialog.newInstance().apply {
                isCancelable = false
                action = { confirmPermission() }

            }.show(supportFragmentManager, AccessPermissionDialog.TAG)
        })
    }

    private fun MainViewModel.userMessage() {
        userMessageEvent.observe(this@MainActivity, Observer {
            AlertDialog.newInstance(title = "메세지", message = it).apply {
                isCancelable = false
                action = {
                    confirmUserMessage()
                }
            }.show(supportFragmentManager, AlertDialog.TAG)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (val currentFragment = supportFragmentManager.currentNavigationFragment()) {
            is HomeFragment -> currentFragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun moveToFilterUploadPage(filterData: FilterData) {

        startActForResult(UploadActivity::class, UPLOAD_FEED_REQ, Bundle().apply {
            putParcelable(UploadActivity.FILTER_DATA_KEY, filterData)
        })

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
        )
        val controller = navBottomView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.mainNavFragment, //FragmentContainerView의 id
            intent = intent
        )
        currentNavController = controller
    }

    private fun initFcmBroadcastReceiver() {
        fcmBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    it.getStringExtra(FirebaseDefine.PUSH_EXTRA_KEY)?.let { title ->
                        lifecycleScope.launch {
                            delay(1000)
                            showShortToast(title)
                            val badge = navBottomView.getOrCreateBadge(R.id.action_notification)
                            badge.isVisible = true
                            viewModel.onNotificationRefreshListener()
                        }
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

