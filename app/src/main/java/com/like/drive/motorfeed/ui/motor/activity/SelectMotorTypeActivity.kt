package com.like.drive.motorfeed.ui.motor.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivitySelectMotorTypeBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.motor.adapter.SelectMotorTypeAdapter
import com.like.drive.motorfeed.ui.motor.viewmodel.MotorTypeViewModel
import kotlinx.android.synthetic.main.activity_select_motor_type.*
import org.koin.android.ext.android.inject

class SelectMotorTypeActivity : BaseActivity<ActivitySelectMotorTypeBinding>(R.layout.activity_select_motor_type) {

    private val viewModel:MotorTypeViewModel by inject()
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

    private fun initView(){
        setCloseButtonToolbar(toolbar){
            finish()
        }
    }

    private fun withViewModel(){
        with(viewModel){
            showBrandList()
            callbackMotorTypeData()
        }
    }

    private fun MotorTypeViewModel.showBrandList(){
        motorTypeListBrand.observe(this@SelectMotorTypeActivity, Observer {
            showListDialog(it.map {value -> value.brandName }.toTypedArray(),"브랜드 목록"){position->
                when(position){
                    0 -> getMotorTypeData()
                    else -> getBrandByList(it[position].brandCode)
                }
                etSearchBox.text = null
            }
        })
    }

    private fun MotorTypeViewModel.callbackMotorTypeData(){
        motorTypeDataCallbackEvent.observe(this@SelectMotorTypeActivity, Observer {
           Intent().apply {
                putExtra(RESULT_KEY,it)
            }.run {
                setResult(Activity.RESULT_OK,this)
               finish()
            }

        })
    }


    companion object{
        const val REQUEST_CODE = 1001
        const val RESULT_KEY ="RESULT_MOTOR_TYPE"
    }

}
