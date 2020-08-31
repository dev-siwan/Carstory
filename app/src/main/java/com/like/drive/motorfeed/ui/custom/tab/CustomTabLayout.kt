package com.like.drive.motorfeed.ui.custom.tab

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

class CustomTabLayout : TabLayout {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        kotlin.runCatching {

            if (tabCount == 0) return

            val tabLayout = getChildAt(0) as ViewGroup
            val childCount = tabLayout.childCount
            val widths = IntArray(childCount + 1)

            for (i in 0 until childCount) {
                widths[i] = tabLayout.getChildAt(i).measuredWidth
                widths[childCount] += widths[i]
            }

            val measuredWidth: Int = measuredWidth
            for (i in 0 until childCount) {
                tabLayout.getChildAt(i).minimumWidth = measuredWidth * widths[i] / widths[childCount]
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}