package com.like.drive.motorfeed.ui.feed.tag.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityFeedTagBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.hideKeyboard
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.feed.tag.adapter.FeedTagAdapter
import com.like.drive.motorfeed.ui.feed.tag.viewmodel.FeedTagViewModel
import kotlinx.android.synthetic.main.activity_feed_tag.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedTagActivity : BaseActivity<ActivityFeedTagBinding>(R.layout.activity_feed_tag) {

    val viewModel : FeedTagViewModel by viewModel()
    private val feedAdapter by lazy { FeedTagAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivityFeedTagBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
    }

    private fun initData(){
        intent.getStringArrayListExtra(TAG_LIST_KEY)?.let {
            feedAdapter.tagList = it
        }

        initView()
    }
    private fun initView(){

        rvTag.run {
            adapter= feedAdapter
        }

        setCloseButtonToolbar(toolbar){finish()}

        tvEnrollment.setOnClickListener {
            if(feedAdapter.tagList.isNotEmpty()) {

                setResult(Activity.RESULT_OK, Intent().apply {
                    putStringArrayListExtra(TAG_LIST_KEY, feedAdapter.tagList)
                })

                finish()
            }else{
                showShortToast(R.string.tag_empty_desc)
            }
        }
    }

    private fun withViewModel(){
        with(viewModel){
            addTag()
            removeTag()
        }
    }

    private fun FeedTagViewModel.addTag() {
        addTagEvent.observe(this@FeedTagActivity, Observer {
            feedAdapter.run {
                addTag(it,
                    action = {
                      binding?.isTagList = tagList.isNotEmpty()
                    },
                    actionEquals = {
                        showShortToast(R.string.tag_equal)
                    },
                    actionMaxSize = {
                        showShortToast(R.string.tag_desc)
                    })
                hideKeyboard(clSearchBox)

            }
        })
    }

    private fun FeedTagViewModel.removeTag() {
        removeTagEvent.observe(this@FeedTagActivity, Observer {
            feedAdapter.run {
                removeTag(it,
                action = { binding?.isTagList = tagList.isNotEmpty()})
            }
        })
    }

    companion object{
        const val TAG_LIST_KEY= "tagListKey"
        const val TAG_ACT_REQ= 1233
    }
}