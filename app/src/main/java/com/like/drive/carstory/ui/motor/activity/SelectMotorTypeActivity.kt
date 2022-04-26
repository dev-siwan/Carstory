package com.like.drive.carstory.ui.motor.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.ActivitySelectMotorTypeBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.showListDialog
import com.like.drive.carstory.ui.motor.adapter.SelectMotorTypeAdapter
import com.like.drive.carstory.ui.motor.viewmodel.MotorTypeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_select_motor_type.*
import kotlinx.android.synthetic.main.layout_motor_select_search_box.view.*

@AndroidEntryPoint
class SelectMotorTypeActivity :
    BaseActivity<ActivitySelectMotorTypeBinding>(R.layout.activity_select_motor_type) {

    private val viewModel: MotorTypeViewModel by viewModels()
    private val motorTypeAdapter by lazy { SelectMotorTypeAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivitySelectMotorTypeBinding) {
        super.onBinding(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.rvMotorType.adapter = motorTypeAdapter
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) {
            finish()
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            showBrandList()
            callbackMotorTypeData()
        }
    }

    private fun MotorTypeViewModel.showBrandList() {
        motorTypeListBrand.observe(this@SelectMotorTypeActivity, Observer {
            showListDialog(
                it.map { value -> value.brandName }.toTypedArray(),
                getString(R.string.brand_list_title)
            ) { position ->
                when (position) {
                    0 -> getMotorTypeData()
                    else -> getBrandByList(it[position].brandCode)
                }
                incMotorSearchBox.etSearchBox.text = null
            }
        })
    }

    private fun MotorTypeViewModel.callbackMotorTypeData() {
        motorTypeDataCallbackEvent.observe(this@SelectMotorTypeActivity, Observer {
            Intent().apply {
                putExtra(RESULT_KEY, it)
            }.run {
                setResult(Activity.RESULT_OK, this)
                finish()
            }

        })
    }

    companion object {
        const val REQUEST_CODE = 1003
        const val RESULT_KEY = "RESULT_MOTOR_TYPE"
    }

}
