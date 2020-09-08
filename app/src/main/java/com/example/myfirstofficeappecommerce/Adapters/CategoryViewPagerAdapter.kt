package com.example.myfirstofficeappecommerce.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment
import com.example.myfirstofficeappecommerce.fragments.CategoryEachViewPagerFragment

class CategoryViewPagerAdapter(
    fragment: CategoriesFragment,
    var map: LinkedHashMap<String, List<CategoriesModelClass>>,
    var callback: (Pair<String,CategoriesModelClass>) -> Unit
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {

        return map.size
    }

    override fun createFragment(position: Int): Fragment {
        var list = map.values.toList()[position].filter { categoriesModelClass ->
            ApplicationClass.selectedItemsList?.filter {
                if (it.id == categoriesModelClass.id && it.groupId==categoriesModelClass.groupId) {
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
            else map.values.toList()[position], callback
        )
    }
}