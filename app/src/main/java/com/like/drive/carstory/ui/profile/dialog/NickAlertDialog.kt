package com.like.drive.carstory.ui.profile.dialog

import android.view.ViewGroup
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.FragmentNickAlertDialogBinding
import com.like.drive.carstory.ui.base.BaseFragmentDialog

class NickAlertDialog :
    BaseFragmentDialog<FragmentNickAlertDialogBinding>(R.layout.fragment_nick_alert_dialog) {

    override fun onBindAfter(dataBinding: FragmentNickAlertDialogBinding) {
        super.onBindAfter(dataBinding)

        isCancelable = false

        dataBinding.btnConfirm.setOnClickListener {
            dismiss()
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
        fun newInstance() = NickAlertDialog()

    }
}