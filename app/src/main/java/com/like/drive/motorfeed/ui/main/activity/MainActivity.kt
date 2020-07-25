package com.like.drive.motorfeed.ui.main.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityMainBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import com.like.drive.motorfeed.ui.sign.`in`.activity.SignInActivity
import com.like.drive.motorfeed.ui.upload.activity.UploadActivity
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
            startAct(UploadActivity::class)
        })
    }

    private fun MainViewModel.signOut(){
        signOutComplete.observe(this@MainActivity, Observer {
            startAct(SignInActivity::class)
            finish()
        })
    }
}

