package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Adapters.MainRecyclerAdapter
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.Adapters.ViewPagerAdapter
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : Fragment() {

    var recyclerView: RecyclerView? = null;
    var adapterr: MainRecyclerAdapter? = null;
    var list: List<ModelClass>? = null
    var viewPager2: ViewPager2? = null
    var tablayout: TabLayout? = null;
    var categoryMap: LinkedHashMap<String, List<CategoriesModelClass>>? = null;
    var isScrollForward: Boolean = true

    private val SCROLL_TIMEOUT = 4000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)
        firstLayoutHoriZontalScrollItemNames(view)
        viewPager2 = view.findViewById(R.id.viewpager)
        (viewPager2 as ViewPager2).adapter =
            ViewPagerAdapter(this)
        tablayout = view.findViewById(R.id.tablayout)

        TabLayoutMediator(
            (tablayout as TabLayout),
            (viewPager2 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

        }.attach()

        (viewPager2 as ViewPager2).registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("tabcount", tablayout!!.tabCount.toString() + " " + position)
                if (position < tablayout!!.tabCount)
                    tablayout!!.postDelayed({
                        if (position == tablayout?.tabCount?.minus(1)) {
                            isScrollForward = false

                            Log.d(
                                "tabbackword",
                                tablayout!!.tabCount.toString() + " " + position + "   "
                            )
                        }
                        if(position==0) {
                            isScrollForward=true
                            Log.d("tabforward", tablayout!!.tabCount.toString() + " " + position)
                        }
                        if (!isScrollForward) {
                            tablayout?.getTabAt(position - 1)?.select()
                        }
                        else
                            tablayout?.getTabAt(position + 1)?.select()


                    }, SCROLL_TIMEOUT)

                super.onPageSelected(position)
            }
        })

        return view
    }


    private fun firstLayoutHoriZontalScrollItemNames(view: View) {
        categoryMap =
            CategoriesDataProvider().getMapDataForCategories()
        recyclerView = view.findViewById(R.id.mainfragmentrecyclerhorizontalscrollitemnames)
        adapterr =
            MainRecyclerAdapter(activity as MainActivity, categoryMap)
        list = CategoriesDataProvider().getListDataForHorizontalScroll()
        (recyclerView as RecyclerView).adapter = adapterr
        (recyclerView as RecyclerView).layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        (adapterr as MainRecyclerAdapter).submitList(list)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragmentmenu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}