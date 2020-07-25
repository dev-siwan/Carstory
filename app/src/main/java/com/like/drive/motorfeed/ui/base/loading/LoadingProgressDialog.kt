package com.like.drive.motorfeed.ui.base.loading
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.like.drive.motorfeed.R


class LoadingProgressDialog(context: Context): Dialog(context, android.R.style.Theme_Black){

    init {
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_loading_progress, null)

        setCancelable(false)
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(view)
    }

}