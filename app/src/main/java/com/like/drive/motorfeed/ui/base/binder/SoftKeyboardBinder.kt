package com.like.drive.motorfeed.ui.base.binder

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("onEditorSearchAction","parentRequestView")
fun EditText.onEditorSearchAction(action: ((String?) -> Unit)?,view:View?) {
    setOnEditorActionListener { v, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> {
                action?.invoke(v.text?.toString())
                this.clearFocus()
                view?.requestFocus()
                val inputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
                true
            }
            else -> false
        }
    }
}