package com.like.drive.motorfeed.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.like.drive.motorfeed.R

abstract class BaseActivity<V : ViewDataBinding>(@get:LayoutRes val layoutId: Int?) : AppCompatActivity() {

    protected var binding: V? = null
    protected var remoteConfig =  Firebase.remoteConfig.apply {
        setDefaultsAsync(R.xml.remote_config_defaults)
        setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
            fetchTimeoutInSeconds = 60
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId?.let { layoutID ->
            binding = DataBindingUtil.setContentView<V>(this, layoutID)?.apply {
                onBinding(this)
            }
        }
    }

    protected open fun onBinding(dataBinding: V)= dataBinding.run{
        lifecycleOwner = this@BaseActivity
        executePendingBindings()
    }

    //툴바 설정
    protected open fun setBackButtonToolbar(toolbar: Toolbar, title: String = "") = toolbar.apply {
        this.title = title
        navigationIcon = getDrawable(R.drawable.ic_action_back)
    }.run {
        setSupportActionBar(this)
    }

    protected open fun setCloseButtonToolbar(toolbar: Toolbar, title: String = "") = toolbar.apply {
        this.title = title
        navigationIcon = getDrawable(R.drawable.ic_action_close)
    }.run {
        setSupportActionBar(this)
    }

    protected open fun setBackButtonToolbar(toolbar: Toolbar, title: String = "", action: () -> Unit) = toolbar.apply {
        this.title = ""
        navigationIcon = getDrawable(R.drawable.ic_action_back)
    }.run {
        setSupportActionBar(this)
        setNavigationOnClickListener { action.invoke() }
    }

    protected open fun setCloseButtonToolbar(toolbar: Toolbar, title: String = "", action: () -> Unit) = toolbar.apply {
        this.title = ""
        navigationIcon = getDrawable(R.drawable.ic_action_close)
    }.run {
        setSupportActionBar(this)
        setNavigationOnClickListener { action.invoke() }
    }

    protected fun Toolbar?.changeNavigationClickEvent(action: () -> Unit) {
        this?.setNavigationOnClickListener { action.invoke() }
    }



}