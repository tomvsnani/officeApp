package com.example.myfirstofficeappecommerce.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment
import com.example.myfirstofficeappecommerce.fragments.CategoryEachViewPagerFragment

class CategoryViewPagerAdapter(
    fragment: FragmentManager,
    var map: List<CategoriesModelClass>,lifecycle:Lifecycle,
    var callback: () -> Unit
) : FragmentStateAdapter(fragment,lifecycle) {
    override fun getItemCount(): Int {

        return map.size
    }

    override fun createFragment(position: Int): Fragment {

        return CategoryEachViewPagerFragment(map[position],callback)
        
    }
}