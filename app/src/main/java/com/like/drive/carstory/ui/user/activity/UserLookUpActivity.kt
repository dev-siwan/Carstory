package com.like.drive.carstory.ui.user.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.ActivityUserLookUpBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.base.ext.startAct
import com.like.drive.carstory.ui.board.list.activity.BoardListActivity
import com.like.drive.carstory.ui.user.viewmodel.UserLookUpViewModel
import kotlinx.android.synthetic.main.activity_user_look_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserLookUpActivity :
    BaseActivity<ActivityUserLookUpBinding>(R.layout.activity_user_look_up) {

    private val viewModel: UserLookUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        getData()
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivityUserLookUpBinding) {
        super.onBinding(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.containerUserBoardList.containerMoreItem.setOnClickListener {
            startAct(BoardListActivity::class, Bundle().apply {
                putParcelable(BoardListActivity.BOARD_DATE_KEY, viewModel.userData.value)
            })
        }
    }

    private fun getData() {
        intent.getStringExtra(USER_ID_KEY)?.let {
            viewModel.init(it)
        }
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
    }

    private fun withViewModel() {
        with(viewModel) {
            loading()
            error()
        }
    }

    private fun UserLookUpViewModel.error() {
        errorStrEvent.observe(this@UserLookUpActivity, Observer {
            showShortToast(it)
            finish()
        })
    }

    private fun UserLookUpViewModel.loading() {
        loading.observe(this@UserLookUpActivity, Observer {
            if (it) loadingProgress.show() else loadingProgress.dismiss()
        })
    }

    companion object {
        const val USER_ID_KEY = "UserIDKey"
    }
}