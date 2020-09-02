@file:Suppress("DEPRECATION")

package com.like.drive.motorfeed.ui.base.binder

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("formatHtml")
fun TextView.setFormatHtml(text: String?) {
    text?.let {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) setText(
            Html.fromHtml(
                it
            )
        )
        else setText(Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY))
    }
}

@BindingAdapter("hintFormatHtml")
fun TextView.setHintFormatHtml(text: String?) {
    text?.let {
        hint = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) Html.fromHtml(it)
        else Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
    }
}