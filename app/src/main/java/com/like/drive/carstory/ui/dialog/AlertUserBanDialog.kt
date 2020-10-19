package com.like.drive.carstory.ui.dialog

import android.os.Bundle
import android.view.ViewGroup
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.FragmentNickAlertDialogBinding
import com.like.drive.carstory.databinding.FragmentUserBanAlertDialogBinding
import com.like.drive.carstory.ui.base.BaseFragmentDialog
import com.like.drive.carstory.ui.base.binder.setFormatHtml
import com.like.drive.carstory.ui.base.ext.htmlFormat
import com.like.drive.carstory.ui.base.ext.openWebBrowser
import kotlinx.android.synthetic.main.fragment_user_ban_alert_dialog.*

class AlertUserBanDialog :
    BaseFragmentDialog<FragmentUserBanAlertDialogBinding>(R.layout.fragment_user_ban_alert_dialog) {

    var message: String? = ""
    var userNick: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString(EXTRA_MESSAGE)
            userNick = it.getString(EXTRA_USER_ID)
        }
    }

    override fun onBindAfter(dataBinding: FragmentUserBanAlertDialogBinding) {
        super.onBindAfter(dataBinding)

        isCancelable = false

        dataBinding.btnConfirm.setOnClickListener {
            dismiss()
        }

        dataBinding.tvDescription.setFormatHtml(
            getString(R.string.user_ban_dialog_desc_format, userNick, message)
        )
        dataBinding.tvUserBanSubmitDesc.setOnClickListener {
            requireActivity().openWebBrowser("https://forms.gle/27PYkojWAtEJ126G7")
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
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        const val EXTRA_USER_ID = "USER_ID"

        @JvmStatic
        fun newInstance(message: String? = "", userNick: String? = "") =
            AlertUserBanDialog().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_MESSAGE, message)
                    putString(EXTRA_USER_ID, userNick)
                }
            }

    }
}