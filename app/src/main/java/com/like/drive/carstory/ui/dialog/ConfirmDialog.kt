package com.like.drive.carstory.ui.dialog

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.DialogConfirmBinding
import com.like.drive.carstory.ui.base.BaseFragmentDialog
import com.like.drive.carstory.ui.base.binder.setFormatHtml
import kotlinx.android.synthetic.main.dialog_confirm.*

class ConfirmDialog :
    BaseFragmentDialog<DialogConfirmBinding>(R.layout.dialog_confirm) {

    var title: String? = ""
    var message: String? = ""
    var leftButtonName: String? = null
    var rightButtonName: String? = null

    var cancelAction: (() -> Unit)? = null
    var confirmAction: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(EXTRA_TITLE) ?: ""
            message = it.getString(EXTRA_MESSAGE) ?: ""
            leftButtonName = it.getString(EXTRA_LEFT_BUTTON_NAME)
            rightButtonName = it.getString(EXTRA_RIGHT_BUTTON_NAME)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (title?.isNotBlank() == true) {
            tvTitle.text = title
            tvTitle?.isVisible = true
        } else {
            tvTitle?.isVisible = false
        }
        message = message?.replace("\n", "<br />")
        tvDescription.setFormatHtml(message)
        if (leftButtonName != null) btnCancel.text = leftButtonName
        if (rightButtonName != null) btnConfirm.text = rightButtonName

        btnCancel.setOnClickListener {
            cancelAction?.invoke()
            dismiss()
        }

        btnConfirm.setOnClickListener {
            confirmAction?.invoke()
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
        const val TAG = "ConfirmDialog"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        const val EXTRA_LEFT_BUTTON_NAME = "EXTRA_LEFT_BUTTON_NAME"
        const val EXTRA_RIGHT_BUTTON_NAME = "EXTRA_RIGHT_BUTTON_NAME"

        fun newInstance(
            title: String? = "",
            message: String? = "",
            leftButtonName: String? = null,
            rightButtonName: String? = null
        ): ConfirmDialog {
            val args = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_MESSAGE, message)
                putString(EXTRA_LEFT_BUTTON_NAME, leftButtonName)
                putString(EXTRA_RIGHT_BUTTON_NAME, rightButtonName)
            }

            val fragment = ConfirmDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
