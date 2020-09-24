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
import com.like.drive.motorfeed.databinding.DialogListFilterBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import com.like.drive.motorfeed.ui.board.category.data.getCategoryList
import com.like.drive.motorfeed.ui.filter.viewmodel.FilterViewModel
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import kotlinx.android.synthetic.main.dialog_list_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val CATEGORY_TYPE = "category_type"
const val MOTOR_TYPE = "motor_type"

class ListFilterDialog :
    BaseFragmentDialog<DialogListFilterBinding>(R.layout.dialog_list_filter) {

    val viewModel: FilterViewModel by viewModel()
    var setFilter: ((CategoryData?, MotorTypeData?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.feedType.value = it.getParcelable(CATEGORY_TYPE)
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

    override fun onBind(dataBinding: DialogListFilterBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
        setCategory(viewModel.feedType.value)
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
            viewModel.setFilter(binding.feedType, binding.motorType)
            setFilter?.invoke(binding.feedType, binding.motorType)
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            motorType()
            pageToMotorType()
            showCategory()
        }
    }

    private fun FilterViewModel.motorType() {
        motorTypeEvent.observe(viewLifecycleOwner, Observer {
            this@ListFilterDialog.setMotorType(it)
        })
    }

    private fun FilterViewModel.pageToMotorType() {
        filterMotorTypeClickEvent.observe(viewLifecycleOwner, Observer {
            startActForResult(SelectMotorTypeActivity::class, SelectMotorTypeActivity.REQUEST_CODE)
        })
    }

    private fun FilterViewModel.showCategory() {
        filterCategoryClickEvent.observe(viewLifecycleOwner, Observer {
            getCategoryList(this@ListFilterDialog.requireContext()).toMutableList().apply {
                add(0, CategoryData(getString(R.string.all), "", 0))
            }.let {
                requireActivity().showListDialog(it.map { data -> data.title }
                    .toTypedArray()) { position ->
                    when (position) {
                        0 -> {
                            setCategory(null)
                        }
                        else -> {
                            this@ListFilterDialog.setCategory(it[position])
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

    private fun setCategory(feedTypeData: CategoryData?) {
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
        fun newInstance(feedTypeData: CategoryData? = null, motorTypeData: MotorTypeData? = null) =
            ListFilterDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY_TYPE, feedTypeData)
                    putParcelable(MOTOR_TYPE, motorTypeData)
                }
            }
    }
}