package com.like.drive.motorfeed.ui.upload.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.ActivityUploadBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import kotlinx.android.synthetic.main.activity_upload.*

class UploadActivity : BaseActivity<ActivityUploadBinding>(R.layout.activity_upload) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvSelectMotor.setOnClickListener {
            startActForResult(SelectMotorTypeActivity::class,SelectMotorTypeActivity.REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SelectMotorTypeActivity.REQUEST_CODE -> {

                    data?.getParcelableExtra<MotorTypeData>(SelectMotorTypeActivity.RESULT_KEY)
                        ?.let {
                            tvSelectMotor.text = "브랜드:${it.brandName} / 모델:${it.modelName} "
                        }
                }
            }
        }

    }
}