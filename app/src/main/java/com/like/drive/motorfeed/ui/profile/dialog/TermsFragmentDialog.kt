package com.like.drive.motorfeed.ui.profile.dialog

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentTermsDialogBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.base.ext.startToAct
import com.like.drive.motorfeed.ui.terms.TermsActivity
import kotlinx.android.synthetic.main.fragment_terms_dialog.*

class TermsFragmentDialog :
    BaseFragmentDialog<FragmentTermsDialogBinding>(R.layout.fragment_terms_dialog) {

    override fun onBind(dataBinding: FragmentTermsDialogBinding) {
        super.onBind(dataBinding)

        val desc = getString(R.string.terms_dialog_desc)

        val useClickSpan = object : ClickableSpan() {

            override fun onClick(textView: View) {
                startToAct(TermsActivity::class, Bundle().apply {
                    putString(TermsActivity.TERMS_KEY, TermsActivity.TERMS_USE_VALUE)
                })
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }

        val privacyClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startToAct(TermsActivity::class, Bundle().apply {
                    putString(TermsActivity.TERMS_KEY, TermsActivity.TERMS_PRIVACY_VALUE)
                })
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }

        val useIndex = desc.indexOf("이용약관")
        val privacyIndex = desc.indexOf("개인정보처리방침")

        val descSpan = SpannableString(desc).apply {
            setSpan(useClickSpan, useIndex, useIndex + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(
                privacyClickSpan,
                privacyIndex,
                privacyIndex + 8,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        dataBinding.tvDescription.run {
            setText(descSpan, TextView.BufferType.SPANNABLE)
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT;
        }

    }

    override fun onBindAfter(dataBinding: FragmentTermsDialogBinding) {
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
        fun newInstance() = TermsFragmentDialog()

    }
}