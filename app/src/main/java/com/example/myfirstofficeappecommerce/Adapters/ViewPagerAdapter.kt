package com.example.myfirstofficeappecommerce.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstofficeappecommerce.fragments.EachViewPagerItemFragment
import com.example.myfirstofficeappecommerce.fragments.MainFragment

class ViewPagerAdapter(mainFragment: MainFragment) : FragmentStateAdapter(mainFragment) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return EachViewPagerItemFragment()
    }
}