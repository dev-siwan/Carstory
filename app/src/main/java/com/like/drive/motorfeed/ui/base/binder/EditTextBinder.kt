package com.like.drive.motorfeed.ui.base.binder

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
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

@BindingAdapter("onEditorEnterAction")
fun EditText.onEditorEnterAction(f: Function1<String, Unit>?) {

    if (f == null) setOnEditorActionListener(null)
    else setOnEditorActionListener { v, actionId, event ->

        val imeAction = when (actionId) {
            EditorInfo.IME_ACTION_DONE,
            EditorInfo.IME_ACTION_SEND,
            EditorInfo.IME_ACTION_GO -> true
            else -> false
        }

        val keyDownEvent = event?.keyCode == KeyEvent.KEYCODE_ENTER
                && event.action == KeyEvent.ACTION_DOWN

        if (imeAction or keyDownEvent)
            true.also { f(v.editableText.toString()) }
        else false
    }
}
