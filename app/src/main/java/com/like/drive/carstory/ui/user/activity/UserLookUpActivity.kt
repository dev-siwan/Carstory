package com.like.drive.carstory.ui.user.activity

import android.os.Bundle
import com.like.drive.carstory.R
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.databinding.ActivityUserLookUpBinding
import com.like.drive.carstory.ui.base.BaseActivity
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
        getData()
        initView()
    }

    override fun onBinding(dataBinding: ActivityUserLookUpBinding) {
        dataBinding.vm = viewModel
        dataBinding.containerUserBoard.containerMoreItem.setOnClickListener {
            startAct(BoardListActivity::class, Bundle().apply {
                putParcelable(BoardListActivity.BOARD_DATE_KEY, viewModel.userData.value)
            })
        }
    }

    private fun getData() {
        intent.getParcelableExtra<UserData>(USER_DATA_KEY)?.let {
            viewModel.init(it)
        }
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
    }

    companion object {
        const val USER_DATA_KEY = "UserDataKey"
    }
}