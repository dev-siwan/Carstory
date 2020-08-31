package com.like.drive.motorfeed.ui.home.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentHomeBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.home.adapter.HomeViewPagerAdapter
import com.like.drive.motorfeed.ui.home.data.HomeTab
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_common_tab.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModel()
    private val mainVm: MainViewModel by sharedViewModel()
    private val inflater by lazy { LayoutInflater.from(requireContext()) }
    private val tabState by lazy {
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.tvTabTitle?.typeface = Typeface.DEFAULT
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.run {
                    customView?.tvTabTitle?.typeface = Typeface.DEFAULT_BOLD
                    //setViewPagerPosition(position)
                }
            }

        }
    }

    override fun onBind(dataBinding: FragmentHomeBinding) {
        super.onBind(dataBinding)
        dataBinding.mainVm = mainVm
        dataBinding.vm = viewModel
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()
    }

    fun initView() {

        vpHome.run {
            adapter = HomeViewPagerAdapter(requireActivity(), HomeTab.values())
        }

        initTab()
    }

    private fun initTab() {
        tabHome.addOnTabSelectedListener(tabState)
        TabLayoutMediator(tabHome, vpHome) { tab, position ->
            inflater.inflate(R.layout.layout_common_tab, tabHome, false)
                .apply { tvTabTitle?.text = HomeTab.values()[position].getTitle() }
                .run { tab.customView = this }
        }.attach()
    }

    fun withViewModel() {
        with(viewModel) {
            searchClick()
        }
    }

    private fun HomeViewModel.searchClick() {
        moveSearchEvent.observe(viewLifecycleOwner, Observer {
            (requireActivity() as MainActivity).navBottomView.selectedItemId = R.id.action_search
        })
    }


}

