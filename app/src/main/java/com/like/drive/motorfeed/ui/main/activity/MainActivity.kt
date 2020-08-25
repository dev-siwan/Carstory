package com.like.drive.motorfeed.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityMainBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.currentNavigationFragment
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.feed.list.fragment.FeedListFragment
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.home.fragment.HomeFragment
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import com.like.drive.motorfeed.ui.sign.`in`.activity.SignInActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel : MainViewModel by inject()
    private val navController by lazy {
        Navigation.findNavController(this, R.id.mainNavFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withViewModel()
        setUpNavigation()
        changeDestinationListener()
    }

    override fun onBinding(dataBinding: ActivityMainBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
    }

    private fun withViewModel(){
        with(viewModel){
            signOut()
            moveToUploadPage()
        }
    }

    private fun setUpNavigation() {
        navBottomView.setupWithNavController(navController = Navigation.findNavController(this, R.id.mainNavFragment))
        navBottomView.setOnNavigationItemReselectedListener {}

    }

    private fun changeDestinationListener() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->

        }
    }

    private fun MainViewModel.moveToUploadPage(){
        uploadClickEvent.observe(this@MainActivity, Observer {
            startActForResult(FeedUploadActivity::class,UPLOAD_FEED_REQ)
        })
    }

    private fun MainViewModel.signOut(){
        signOutComplete.observe(this@MainActivity, Observer {
            startAct(SignInActivity::class)
            finish()
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (val currentFragment = supportFragmentManager.currentNavigationFragment()) {
            is HomeFragment -> currentFragment.onActivityResult(requestCode,resultCode,data)
            is FeedListFragment-> currentFragment.onActivityResult(requestCode,resultCode,data)
        }
    }

    companion object{
        const val UPLOAD_FEED_REQ=1530
    }
}

