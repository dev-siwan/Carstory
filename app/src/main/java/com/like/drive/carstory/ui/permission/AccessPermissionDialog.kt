package com.like.drive.carstory.ui.permission


import android.os.Bundle
import android.view.ViewGroup
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.DialogAccessPermissionBinding
import com.like.drive.carstory.ui.base.BaseFragmentDialog
import kotlinx.android.synthetic.main.dialog_access_permission.*

class AccessPermissionDialog :
    BaseFragmentDialog<DialogAccessPermissionBinding>(R.layout.dialog_access_permission) {

    var action: (() -> Unit)? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isCancelable = false
        btnConfirm.setOnClickListener {
            action?.invoke()
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

    override fun onBackPressed(): Boolean {
        dismiss()
        return super.onBackPressed()
    }

    companion object {
        const val TAG = "AccessPermissionDialog"

        fun newInstance(): AccessPermissionDialog {
            return AccessPermissionDialog()
        }
    }

}
