package com.like.drive.carstory.ui.base.loading
import android.os.Bundle
import android.view.ViewGroup
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.DialogUploadProgressBinding
import com.like.drive.carstory.ui.base.BaseFragmentDialog
import com.like.drive.carstory.ui.board.upload.viewmodel.UploadViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class UploadProgressDialog: BaseFragmentDialog<DialogUploadProgressBinding>(R.layout.dialog_upload_progress){

    private val viewModel : UploadViewModel by sharedViewModel()

    override fun onStart() {
        super.onStart()
        dialog?.window?.run{
            setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
            setBackgroundDrawableResource(R.color.alpha_50)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
    }

    override fun onBind(dataBinding: DialogUploadProgressBinding) {
        super.onBind(dataBinding)

        dataBinding.vm = viewModel
    }

}