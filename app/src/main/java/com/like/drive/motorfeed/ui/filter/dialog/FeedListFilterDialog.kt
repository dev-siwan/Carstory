package com.like.drive.motorfeed.ui.filter.dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.DialogFeedListFilterBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.feed.type.data.getFeedTypeList
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import com.like.drive.motorfeed.ui.filter.viewmodel.FilterViewModel
import kotlinx.android.synthetic.main.dialog_feed_list_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val FEED_TYPE="feed_type"
const val MOTOR_TYPE="motor_type"

class FeedListFilterDialog : BaseFragmentDialog<DialogFeedListFilterBinding>(R.layout.dialog_feed_list_filter) {

    val viewModel: FilterViewModel by viewModel()
    var setFilter:((FeedTypeData?,MotorTypeData?)->Unit)?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.feedType.value =it.getParcelable(FEED_TYPE)
            viewModel.motorType.value = it.getParcelable(MOTOR_TYPE)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onBind(dataBinding: DialogFeedListFilterBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
        setFeedType(viewModel.feedType.value)
        setMotorType(viewModel.motorType.value)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withViewModel()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.run {
            attributes.windowAnimations = R.style.AnimationFilterStyle
        }

        containerFragment.setOnClickListener {
            dismiss()
        }

        btnComplete.setOnClickListener {
            viewModel.setFilter(binding.feedType,binding.motorType)
            setFilter?.invoke(binding.feedType,binding.motorType)
        }
    }

    private fun withViewModel(){
        with(viewModel){
            motorType()
            pageToMotorType()
            showFeedType()
        }
    }


    private fun FilterViewModel.motorType() {
        motorTypeEvent.observe(viewLifecycleOwner, Observer {
            this@FeedListFilterDialog.setMotorType(it)
        })
    }


    private fun FilterViewModel.pageToMotorType(){
        filterMotorTypeClickEvent.observe(viewLifecycleOwner, Observer {
            startActForResult(SelectMotorTypeActivity::class, SelectMotorTypeActivity.REQUEST_CODE)
        })
    }

    private fun FilterViewModel.showFeedType() {
        filterFeedTypeClickEvent.observe(viewLifecycleOwner, Observer {
            getFeedTypeList(this@FeedListFilterDialog.requireContext()).toMutableList().apply {
                add(0, FeedTypeData(getString(R.string.not_select), "", 0))
            }.let {
                requireActivity().showListDialog(it.map {data-> data.title }.toTypedArray()) { position ->
                    when (position) {
                        0 -> {
                            setFeedType(null)
                        }
                        else -> {
                            this@FeedListFilterDialog.setFeedType(it[position])
                        }
                    }
                }
            }
        })
    }




    private fun setMotorType(motorTypeData: MotorTypeData?) {
        if (motorTypeData?.brandCode == 0) {
            binding.motorType = null
            return
        }
        binding.motorType = motorTypeData
    }

    private fun setFeedType(feedTypeData: FeedTypeData?) {
        if (feedTypeData?.typeCode == 0) {
            binding.feedType = null
            return
        }
        binding.feedType = feedTypeData
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SelectMotorTypeActivity.REQUEST_CODE -> {
                    data?.getParcelableExtra<MotorTypeData>(SelectMotorTypeActivity.RESULT_KEY)
                        ?.let {
                            setMotorType(it)
                        }
                }
            }
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(feedTypeData: FeedTypeData?=null,motorTypeData: MotorTypeData?=null) = FeedListFilterDialog().apply {
            arguments = Bundle().apply {
                putParcelable(FEED_TYPE,feedTypeData)
                putParcelable(MOTOR_TYPE,motorTypeData)
            }
        }
    }
}