package com.like.drive.motorfeed.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.like.drive.motorfeed.ui.home.data.HomeTab
import com.like.drive.motorfeed.ui.home.fragment.NewsFeedFragment
import com.like.drive.motorfeed.ui.home.fragment.UserFilterFragment

class HomeViewPagerAdapter (activity:FragmentActivity,private val homeTabs:Array<HomeTab>):
    FragmentStateAdapter(activity){
    override fun getItemCount() = homeTabs.size

    override fun createFragment(position: Int): Fragment {
      return when(homeTabs[position]){
            HomeTab.NEWS_FEED-> NewsFeedFragment()
            HomeTab.FILTER->UserFilterFragment()
        }
    }

}