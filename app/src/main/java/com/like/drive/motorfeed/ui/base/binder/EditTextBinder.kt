package com.like.drive.motorfeed.ui.base.binder

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter

@BindingAdapter("maxTextLine")
fun EditText.setMaxLine(count: Int?) {
    var text = ""
    this.addTextChangedListener(object : TextWatcher {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            text = s.toString()
        }

        override fun afterTextChanged(s: Editable?) {
            if (lineCount > count ?: 0) {
                setText(text)
                setSelection(this@setMaxLine.text.length)
            }
        }
    })
}