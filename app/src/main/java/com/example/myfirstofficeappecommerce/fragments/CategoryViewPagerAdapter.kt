package com.example.myfirstofficeappecommerce.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass

class CategoryViewPagerAdapter(
    fragment: CategoriesFragment,
    var map: LinkedHashMap<String, List<CategoriesModelClass>>,
    var callback: (Pair<String,CategoriesModelClass>) -> Unit
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {

        return map.size
    }

    override fun createFragment(position: Int): Fragment {
        var list = map.get(map.keys.toTypedArray().get(position))?.filter { categoriesModelClass ->
            ApplicationClass.selectedItemsList?.filter {
                if (it.id == categoriesModelClass.id) {
                    categoriesModelClass.quantityOfItem = it.quantityOfItem
                    categoriesModelClass.itemQueueNumber = it.itemQueueNumber
                    return@filter true
                }
                return@filter true
            }
            return@filter true
        }
        return CategoryEachViewPagerFragment(
            if (

                ApplicationClass.selectedItemsList?.isNotEmpty()!!)
                list
            else map.get(map.keys.toTypedArray().get(position)), callback
        )
    }
}