package com.example.myfirstofficeappecommerce.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.fragments.HorizontalImageScrollFragment

class HorizontalScrollViewPagerAdapter(
    var mainFragment: Fragment,
    var listDataForHorizontalScroll: List<ModelClass>,
    var type:String
) : FragmentStateAdapter(mainFragment) {
    override fun getItemCount(): Int {
        return listDataForHorizontalScroll.size
    }

    override fun createFragment(position: Int): Fragment {
        return HorizontalImageScrollFragment( mainFragment,listDataForHorizontalScroll[position],type)
    }
}