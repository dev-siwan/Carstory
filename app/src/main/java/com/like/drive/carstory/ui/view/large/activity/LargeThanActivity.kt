package com.like.drive.carstory.ui.view.large.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.R
import com.like.drive.carstory.data.photo.PhotoData
import com.like.drive.carstory.databinding.ActivityLargeThanBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.view.large.adapter.LargeThanAdapter
import com.like.drive.carstory.ui.view.large.viewmodel.LargeThanViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_large_than.*

@AndroidEntryPoint
class LargeThanActivity : BaseActivity<ActivityLargeThanBinding>(R.layout.activity_large_than) {

    private val viewModel: LargeThanViewModel by viewModels()
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onBinding(dataBinding: ActivityLargeThanBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvLargePhoto.adapter = LargeThanAdapter()

    }

    private fun initData() {
        intent.getParcelableArrayListExtra<PhotoData>(KEY_PHOTO_DATA_ARRAY)?.let {
            viewModel.init(it)
        }
        position = intent.getIntExtra(KEY_PHOTO_DATA_ARRAY_POSITION, 0)

        initView()
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
        rvLargePhoto.run {
            setSnapHelper()
            smoothScrollToPosition(position)
            viewModel.setIndex(position + 1)
            setImagePosition()
        }
    }

    private fun RecyclerView.setSnapHelper() {
        onFlingListener = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }

    private fun RecyclerView.setImagePosition() {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val index =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1
                viewModel.setIndex(index)
            }
        })
    }

    companion object {
        const val KEY_PHOTO_DATA_ARRAY = "PhotoDataArray"
        const val KEY_PHOTO_DATA_ARRAY_POSITION = "PhotoDataArrayPosition"
    }
}