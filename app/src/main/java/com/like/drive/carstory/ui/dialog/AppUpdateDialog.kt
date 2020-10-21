package com.like.drive.carstory.ui.dialog

import android.view.ViewGroup
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.FragmentAppUpdateDialogBinding
import com.like.drive.carstory.databinding.FragmentNickAlertDialogBinding
import com.like.drive.carstory.ui.base.BaseFragmentDialog

class AppUpdateDialog :
    BaseFragmentDialog<FragmentAppUpdateDialogBinding>(R.layout.fragment_app_update_dialog) {

    var confirmAction: (() -> Unit)? = null

    override fun onBindAfter(dataBinding: FragmentAppUpdateDialogBinding) {
        super.onBindAfter(dataBinding)

        isCancelable = false

        dataBinding.btnNext.setOnClickListener {
            dismiss()
        }

        dataBinding.btnUpdate.setOnClickListener {
            confirmAction?.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = AppUpdateDialog()

    }
}