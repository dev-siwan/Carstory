package com.like.drive.motorfeed.ui.dialog

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.DialogAlertBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.base.binder.setFormatHtml
import kotlinx.android.synthetic.main.dialog_alert.*

class AlertDialog : BaseFragmentDialog<DialogAlertBinding>(R.layout.dialog_alert) {

    private var title: String = ""
    private var message: String = ""
    private var buttonName: String? = null

    var action: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(EXTRA_TITLE) ?: ""
            message = it.getString(EXTRA_MESSAGE) ?: ""
            buttonName = it.getString(EXTRA_BUTTON_NAME) ?: ""
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (title.isNotBlank()) {
            tvTitle.text = title
            tvTitle?.isVisible = true
        } else {
            tvTitle?.isVisible = false
        }

        tvDescription.setFormatHtml(message)

        if (buttonName?.isNotBlank() == true) {
            btnConfirm.text = buttonName
        }

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
        const val TAG = "AlertDialog"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        const val EXTRA_BUTTON_NAME = "EXTRA_BUTTON_NAME"

        fun newInstance(title: String? = null, message: String, buttonName: String? = null): AlertDialog {
            val args = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_MESSAGE, message)
                putString(EXTRA_BUTTON_NAME, buttonName)
            }

            val fragment = AlertDialog()
            fragment.arguments = args
            return fragment
        }

    }

}
