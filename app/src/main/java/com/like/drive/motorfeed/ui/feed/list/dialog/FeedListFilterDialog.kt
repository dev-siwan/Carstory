package com.like.drive.motorfeed.ui.feed.list.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.DialogFeedListFilterBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData


private const val FEED_TYPE = "feedType"
private const val MOTOR_TYPE = "motorType"

class FeedListFilterDialog : BaseFragmentDialog<DialogFeedListFilterBinding>(R.layout.dialog_feed_list_filter) {
    // TODO: Rename and change types of parameters
    private var feedType: FeedTypeData? = null
    private var motorType: MotorTypeData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            feedType = it.getParcelable(FEED_TYPE)
            motorType = it.getParcelable(MOTOR_TYPE)
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(feedType: FeedTypeData, motorType: MotorTypeData) =
            FeedListFilterDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(FEED_TYPE, feedType)
                    putParcelable(MOTOR_TYPE, motorType)
                }
            }
    }
}