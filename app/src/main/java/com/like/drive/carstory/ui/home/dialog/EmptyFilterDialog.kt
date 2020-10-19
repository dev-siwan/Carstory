package com.like.drive.carstory.ui.home.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.FragmentEmptyFilterDialogBinding
import com.like.drive.carstory.ui.base.BaseFragmentDialog

class EmptyFilterDialog :
    BaseFragmentDialog<FragmentEmptyFilterDialogBinding>(R.layout.fragment_empty_filter_dialog) {

    var registerAction: (() -> Unit)? = null

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

    override fun onBind(dataBinding: FragmentEmptyFilterDialogBinding) {
        dataBinding.run {
            containerFragment.setOnClickListener {
                dismiss()
            }
            btnComplete.setOnClickListener {
                registerAction?.invoke()
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.run {
            attributes.windowAnimations = R.style.AnimationCategoryStyle
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = EmptyFilterDialog()
    }
}