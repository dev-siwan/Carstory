package com.like.drive.motorfeed.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.base.ext.startToAct
import kotlin.reflect.KClass


abstract class BaseFragment<V : ViewDataBinding>(
    @get:LayoutRes val layoutId: Int
) : Fragment() {
    lateinit var binding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return DataBindingUtil.inflate<V>(inflater, layoutId, container, false).apply {
            onBindBefore(this)
            onBind(this)
            onBindAfter(this)
        }.root
    }

    protected open fun onBindBefore(dataBinding: V) = Unit
    protected open fun onBind(dataBinding: V) = dataBinding.run {
        binding = this
        lifecycleOwner = this@BaseFragment
        executePendingBindings()
    }

    protected open fun onBindAfter(dataBinding: V) = Unit

    val intent: Intent? get() = activity?.intent


    protected fun binding(action: V.() -> Unit) {
        binding.run(action)
    }


    protected fun startAct(clazz: KClass<*>, bundle: Bundle? = null) {
        startToAct(clazz, bundle) {
           MotorFeedApplication.getContext().startAct(clazz, bundle)
        }
    }

    protected fun startForResult(clazz: KClass<*>, requestCode: Int, bundle: Bundle? = null) {
        startActForResult(clazz, requestCode, bundle){
            MotorFeedApplication.getContext().showShortToast(R.string.not_found_data)
        }
    }


}