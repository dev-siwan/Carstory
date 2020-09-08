package com.like.drive.motorfeed.ui.feed.detail.binder

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.CommentWrapData
import com.like.drive.motorfeed.ui.base.ext.convertDateToString
import com.like.drive.motorfeed.ui.feed.data.CommentFragmentExtra
import com.like.drive.motorfeed.ui.feed.detail.adapter.CommentAdapter
import com.like.drive.motorfeed.ui.feed.detail.adapter.DetailImgAdapter
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import java.util.*
import kotlin.collections.ArrayList

@BindingAdapter(
    value = ["detailBrandName", "detailModelCode", "detailModelName"],
    requireAll = true
)
fun TextView.setDetailMotorType(brandName: String?, modelCode: Int, modelName: String?) {
    brandName?.let {
        text = if (modelCode == 0) {
            brandName
        } else {
            String.format(context.getString(R.string.motorType_format_text), brandName, modelName)
        }
    }

}

@BindingAdapter("detailPhotoList")
fun RecyclerView.setDetailPhotoList(list: List<String>?) {
    list?.let {
        (adapter as DetailImgAdapter).submitList(list)
    }
}

@BindingAdapter("commentList")
fun RecyclerView.setCommentList(list: List<CommentWrapData>?) {
    list?.let {
        (adapter as CommentAdapter).run {
            commentList.addAll(list)
            notifyDataSetChanged()
        }
    }
}

@BindingAdapter("formatDate")
fun TextView.setFormatDate(date: Date?) {
    date?.let {
        text = it.convertDateToString()
    }
}

@BindingAdapter(
    value = ["commentFragmentValue", "commentStr"],
    requireAll = true
)
fun TextView.updateChange(
    commentFragmentExtra: CommentFragmentExtra?,
    commentStrValue: String?
) {
    isEnabled =
        !commentStrValue.isNullOrBlank() &&
                if (commentFragmentExtra?.commentUpdate == true) {
                    commentFragmentExtra.commentData?.commentStr != commentStrValue
                            && commentFragmentExtra.reCommentData?.commentStr != commentStrValue
                } else {
                    true
                }
}