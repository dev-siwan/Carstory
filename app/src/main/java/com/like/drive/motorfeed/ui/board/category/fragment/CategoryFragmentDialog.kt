package com.like.drive.motorfeed.ui.board.category.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentCategoryDialogBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.board.category.adapter.CategoryAdapter
import com.like.drive.motorfeed.ui.board.category.data.getCategoryList
import com.like.drive.motorfeed.ui.board.upload.viewmodel.UploadViewModel
import kotlinx.android.synthetic.main.fragment_category_dialog.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CategoryFragmentDialog :
    BaseFragmentDialog<FragmentCategoryDialogBinding>(R.layout.fragment_category_dialog) {
    private val viewModel: UploadViewModel by sharedViewModel()
    val list by lazy { getCategoryList(requireContext()) }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        containerFragment.setOnClickListener {
            dismiss()
        }

        dialog?.window?.run {
            attributes.windowAnimations = R.style.AnimationCategoryStyle
        }
    }

    override fun onBind(dataBinding: FragmentCategoryDialogBinding) {
        super.onBind(dataBinding)
        dataBinding.rvCategory.adapter = CategoryAdapter(list, viewModel)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoryFragmentDialog()
    }
}