package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.HorizontalScrollViewPagerAdapter
import com.example.myfirstofficeappecommerce.Adapters.CollectionsAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment() : Fragment() {

    var recyclerView: RecyclerView? = null;
    var adapterr: CollectionsAdapter? = null;
    var list: List<ModelClass>? = null
    var viewPager2Banner1: ViewPager2? = null
    var tablayoutBanner1: TabLayout? = null;
    var categoryMap: LinkedHashMap<String, List<CategoriesModelClass>>? = null;
    var searchEditText: EditText? = null
    var isScrollForward: Boolean = true
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    var selectedItemsList: List<CategoriesModelClass>? = ApplicationClass.selectedItemsList
    var navigationView: NavigationView? = null
    var a: MutableList<CategoriesModelClass> = ArrayList()
    private var binding:FragmentMainBinding?=null
    var viewpager2Banner2:ViewPager2?=null
    var tablayoutBanner2:TabLayout?=null



    private val SCROLL_TIMEOUT = 4000L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        binding=FragmentMainBinding.bind(view)
        (activity as MainActivity).unlockDrawer()

        setHasOptionsMenu(true)

        toolbar = view.findViewById(R.id.maintoolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).actionBarToggle = ActionBarDrawerToggle(
            activity, (activity as MainActivity).drawerLayout, R.string.openDrawerLayout,
            R.string.closeDrawerLayout
        )
//activity!!.supportFragmentManager.beginTransaction().replace(R.id.container,testfrag()).commit()
        ((activity as MainActivity).drawerLayout as DrawerLayout).addDrawerListener((activity as MainActivity).actionBarToggle!!)
        ((activity as MainActivity).actionBarToggle as ActionBarDrawerToggle).syncState()
        searchEditText = view.findViewById(R.id.mainfragmentsearchedittext)

        searchEditText!!.setOnFocusChangeListener { view, b ->
            if (view.hasFocus()) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, SearchFragment())
                    ?.addToBackStack(null)?.commit()

            }
        }
        firstLayoutHoriZontalScrollItemNames(view)

        Glide.with(context!!).load(R.drawable.bird).into(binding!!.animatedimageview)

        viewPager2Banner1 = view.findViewById(R.id.viewpager)

        tablayoutBanner1 = view.findViewById(R.id.tablayout)

        viewpager2Banner2=binding!!.viewpagerBanner2

        tablayoutBanner2=binding!!.tablayoutBanner2

        banner_1_ViewPager()

        banner_2_ViewPager()

        return view
    }

    private fun banner_1_ViewPager() {
        (viewPager2Banner1 as ViewPager2).adapter =
            HorizontalScrollViewPagerAdapter(
                this,
                CategoriesDataProvider.getListDataForHorizontalScroll(),
                Constants.HORIZONTAL_SCROLL_TYPE_BANNER1
            )


        TabLayoutMediator(
            (tablayoutBanner1 as TabLayout),
            (viewPager2Banner1 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

        }.attach()


        (viewPager2Banner1 as ViewPager2).registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("tabcount", tablayoutBanner1!!.tabCount.toString() + " " + position)
                if (position < tablayoutBanner1!!.tabCount)
                    tablayoutBanner1!!.postDelayed({
                        if (position == tablayoutBanner1?.tabCount?.minus(1)) {
                            isScrollForward = false

                            Log.d(
                                "tabbackword",
                                tablayoutBanner1!!.tabCount.toString() + " " + position + "   "
                            )
                        }
                        if (position == 0) {
                            isScrollForward = true
                            Log.d(
                                "tabforward",
                                tablayoutBanner1!!.tabCount.toString() + " " + position
                            )
                        }
                        if (!isScrollForward) {
                            tablayoutBanner1?.getTabAt(position - 1)?.select()
                        } else
                            tablayoutBanner1?.getTabAt(position + 1)?.select()


                    }, SCROLL_TIMEOUT)

                super.onPageSelected(position)
            }
        })
    }



    private fun banner_2_ViewPager() {
        (viewpager2Banner2 as ViewPager2).adapter =
            HorizontalScrollViewPagerAdapter(
                this,
                CategoriesDataProvider.getListDataForHorizontalScroll(),
                Constants.HORIZONTAL_SCROLL_TYPE_BANNER2
            )


        TabLayoutMediator(
            (tablayoutBanner2 as TabLayout),
            (viewpager2Banner2 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

        }.attach()


        (viewpager2Banner2 as ViewPager2).registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("tabcount", tablayoutBanner1!!.tabCount.toString() + " " + position)
                if (position < tablayoutBanner2!!.tabCount)
                    tablayoutBanner2!!.postDelayed({
                        if (position == tablayoutBanner2?.tabCount?.minus(1)) {
                            isScrollForward = false

                            Log.d(
                                "tabbackword",
                                tablayoutBanner1!!.tabCount.toString() + " " + position + "   "
                            )
                        }
                        if (position == 0) {
                            isScrollForward = true
                            Log.d(
                                "tabforward",
                                tablayoutBanner1!!.tabCount.toString() + " " + position
                            )
                        }
                        if (!isScrollForward) {
                            tablayoutBanner2?.getTabAt(position - 1)?.select()
                        } else
                            tablayoutBanner2?.getTabAt(position + 1)?.select()


                    }, SCROLL_TIMEOUT)

                super.onPageSelected(position)
            }
        })
    }



    private fun firstLayoutHoriZontalScrollItemNames(view: View) {

        recyclerView = view.findViewById(R.id.mainfragmentrecyclerhorizontalscrollitemnames)

        adapterr =
            CollectionsAdapter(
                this
            )

        (recyclerView as RecyclerView).adapter = adapterr
        (recyclerView as RecyclerView).layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        CategoriesDataProvider.mutableCollectionList.observeForever { t: MutableList<CategoriesModelClass>? ->
            adapterr!!.submitList(t)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if ((activity as MainActivity).drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                (activity as MainActivity).drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                (activity as MainActivity).drawerLayout?.openDrawer(GravityCompat.START)
            }
            return true
        }
        if (item.itemId == R.id.cartmenu) {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    CartFragment(
                        ApplicationClass.selectedVariantList
                    )
                )
                .addToBackStack(null)
                .commit()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("incheck", selectedItemsList!!.size.toString())
        menu.findItem(R.id.cartmenu).actionView.findViewById<ImageView>(R.id.cartmenuitem)
            .setOnClickListener {
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        CartFragment(
                            ApplicationClass.selectedVariantList
                        )
                    )
                    .addToBackStack(null)
                    .commit()

            }
        if (Utils.getItemCount().toInt() > 0) {
            Log.d("incheck", "yes")
            menu.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu)
                .apply {
                    visibility = View.VISIBLE
                    text = Utils.getItemCount()
                }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(context)
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
        super.onCreate(savedInstanceState)
    }



}