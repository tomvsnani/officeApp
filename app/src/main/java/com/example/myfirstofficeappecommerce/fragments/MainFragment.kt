package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.contains
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.CategoriesEachRecyclerAdapter
import com.example.myfirstofficeappecommerce.Adapters.HorizontalScrollViewPagerAdapter
import com.example.myfirstofficeappecommerce.Adapters.CollectionsAdapter
import com.example.myfirstofficeappecommerce.Adapters.DifferentProductsCategoriesRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentMainBinding
import com.example.myfirstofficeappecommerce.databinding.TimerBannerHorizontalLayoutBinding
import com.example.myfirstofficeappecommerce.databinding.TimerBannerVerticalLayoutBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.math.abs


class MainFragment() : Fragment() {
    var millies :Long?=null
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
    private var binding: FragmentMainBinding? = null
    var viewpager2Banner2: ViewPager2? = null
    var tablayoutBanner2: TabLayout? = null
    var menu: Menu? = null
    var featuredItemsRecycler: RecyclerView? = null
    var typeOfFeaturedList: String = "List"
    var featuredProductsAdapter: CategoriesEachRecyclerAdapter? = null
    var bannerDiffProductsCatgoriesRecycler: RecyclerView? = null
    var daysTextView1: TextView? = null
    var daysTextView2: TextView? = null
    var hoursTextView1: TextView? = null
    var hoursTextView2: TextView? = null
    var minutesTextView1: TextView? = null
    var minutesTextView2: TextView? = null
    var secondsTextView1: TextView? = null
    var secondsTextView2: TextView? = null


    var daysTextView1H: TextView? = null
    var daysTextView2H: TextView? = null
    var hoursTextView1H: TextView? = null
    var hoursTextView2H: TextView? = null
    var minutesTextView1H: TextView? = null
    var minutesTextView2H: TextView? = null
    var secondsTextView1H: TextView? = null
    var secondsTextView2H: TextView? = null

    var daysConstant = 60 * 60 * 1000 * 24

    val hourConstant = 60 * 60 * 1000

    val minConst = 60 * 1000

    val secondConst = 1000


    private val SCROLL_TIMEOUT = 4000L


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(context)
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
        millies=CategoriesDataProvider.getMillies()

        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        binding = FragmentMainBinding.bind(view)
        (activity as MainActivity).unlockDrawer()

        setHasOptionsMenu(true)

        setUpToolbar(view)

//activity!!.supportFragmentManager.beginTransaction().replace(R.id.container,testfrag()).commit()
        etUpDrawerLayout()

        setUpSearchEditText(view)

        firstLayoutHoriZontalScrollItemNames(view)

        setUpScrollingBanners(view)

        setUpFeaturedProducts()

        setUpVerticalBannerTimer(view)


        setUpHoriZontalTimer()


        bannerDiffProductsCatgoriesRecycler = binding!!.bennerWithDiffProductsCategoriesRecycler
        bannerDiffProductsCatgoriesRecycler!!.adapter =
            DifferentProductsCategoriesRecyclerViewAdapter()
        bannerDiffProductsCatgoriesRecycler!!.layoutManager =
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)

        featuredProductsAdapter!!.submitList(CategoriesDataProvider.getCategoryData() as MutableList<CategoriesModelClass>)

        return view
    }


    private fun setUpHoriZontalTimer() {
        var horizontalTimerBinding =
            TimerBannerHorizontalLayoutBinding.bind(binding!!.timerhorizontalinclude.root)
        hoursTextView1H = horizontalTimerBinding.hoursTextView1
        hoursTextView2H = horizontalTimerBinding.hoursTextView2
        daysTextView1H = horizontalTimerBinding.daysTextView1
        daysTextView2H = horizontalTimerBinding.daysTextView2
        minutesTextView1H = horizontalTimerBinding.minutesTextView1
        minutesTextView2H = horizontalTimerBinding.minutesTextView2
        secondsTextView1H = horizontalTimerBinding.secondsTextView1
        secondsTextView2H = horizontalTimerBinding.secondsTextView2
        horizontalTimer(millies!!)
    }

    private fun setUpVerticalBannerTimer(view: View) {
        var verticaltimerbinding =
            TimerBannerVerticalLayoutBinding.bind(view.findViewById(R.id.timerverticalinclude))
        hoursTextView1 = verticaltimerbinding.hoursTextView1
        hoursTextView2 = verticaltimerbinding.hoursTextView2
        daysTextView1 = verticaltimerbinding.daysTextView1
        daysTextView2 = verticaltimerbinding.daysTextView2
        minutesTextView1 = verticaltimerbinding.minutesTextView1
        minutesTextView2 = verticaltimerbinding.minutesTextView2
        secondsTextView1 = verticaltimerbinding.secondsTextView1
        secondsTextView2 = verticaltimerbinding.secondsTextView2



        verticaltimer(millies!!)
    }

    fun horizontalTimer(millies: Long) {
        var repeatJob = CoroutineScope(Dispatchers.Main).launch {
            repeat(1) {

                var reducedTime = abs(millies - 1000)

                var tempreducuedTime = abs(millies - 1000)

                var days = reducedTime / daysConstant

                reducedTime -= days * daysConstant

                var hours = reducedTime / (hourConstant)

                reducedTime -= (hours * (hourConstant))

                val minutes = (reducedTime) / (minConst)
                reducedTime -= (minutes * minConst)
                val seconds = (reducedTime) / 1000

                daysTextView1H!!.text = if (days < 10) 0.toString() else (days / 10).toString()

                daysTextView2H!!.text = (days % 10).toString()

                hoursTextView1H!!.text = if (hours < 10) 0.toString() else (hours / 10).toString()
                hoursTextView2H!!.text = (hours % 10).toString()
                minutesTextView1H!!.text =
                    if (minutes < 10) 0.toString() else (minutes / 10).toString()
                minutesTextView2H!!.text = (minutes % 10).toString()
                secondsTextView1H!!.text =
                    if (seconds < 10) 0.toString() else (seconds / 10).toString()
                secondsTextView2H!!.text = (seconds % 10).toString()
                delay(1000)
                horizontalTimer(tempreducuedTime)

            }

        }
    }

    private fun verticaltimer(millies: Long) {
        var repeatJob = CoroutineScope(Dispatchers.Main).launch {
            repeat(1) {

                var reducedTime = abs(millies - 1000)

                var tempreducuedTime = abs(millies - 1000)

                var days = reducedTime / daysConstant

                reducedTime -= days * daysConstant

                var hours = reducedTime / (hourConstant)

                reducedTime -= (hours * (hourConstant))

                val minutes = (reducedTime) / (minConst)
                reducedTime -= (minutes * minConst)
                val seconds = (reducedTime) / 1000

                daysTextView1!!.text = if (days < 10) 0.toString() else (days / 10).toString()

                daysTextView2!!.text = (days % 10).toString()

                hoursTextView1!!.text = if (hours < 10) 0.toString() else (hours / 10).toString()
                hoursTextView2!!.text = (hours % 10).toString()
                minutesTextView1!!.text =
                    if (minutes < 10) 0.toString() else (minutes / 10).toString()
                minutesTextView2!!.text = (minutes % 10).toString()
                secondsTextView1!!.text =
                    if (seconds < 10) 0.toString() else (seconds / 10).toString()
                secondsTextView2!!.text = (seconds % 10).toString()
                delay(1000)
                verticaltimer(tempreducuedTime)


            }
        }
    }


    private fun setUpSearchEditText(view: View) {
        searchEditText = view.findViewById(R.id.mainfragmentsearchedittext)

        searchEditText!!.setOnFocusChangeListener { view, b ->
            if (view.hasFocus()) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, SearchFragment())
                    ?.addToBackStack(null)?.commit()

            }
        }
    }

    private fun etUpDrawerLayout() {
        ((activity as MainActivity).drawerLayout as DrawerLayout).addDrawerListener((activity as MainActivity).actionBarToggle!!)
        ((activity as MainActivity).actionBarToggle as ActionBarDrawerToggle).syncState()
    }

    private fun setUpToolbar(view: View) {
        toolbar = view.findViewById(R.id.maintoolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).actionBarToggle = ActionBarDrawerToggle(
            activity, (activity as MainActivity).drawerLayout, R.string.openDrawerLayout,
            R.string.closeDrawerLayout
        )
    }

    private fun setUpScrollingBanners(view: View) {
        Glide.with(context!!).load(R.drawable.bird).into(binding!!.animatedimageview)

        viewPager2Banner1 = view.findViewById(R.id.viewpager)

        tablayoutBanner1 = view.findViewById(R.id.tablayout)

        viewpager2Banner2 = binding!!.viewpagerBanner2

        tablayoutBanner2 = binding!!.tablayoutBanner2

        banner_1_ViewPager()

        banner_2_ViewPager()
    }

    private fun setUpFeaturedProducts() {
        featuredItemsRecycler = binding!!.featureditems

        when (typeOfFeaturedList) {
            "List" -> {
                // featuredItemsRecycler!!.isNestedScrollingEnabled=false
                featuredProductsAdapter = CategoriesEachRecyclerAdapter(this, "list")
                featuredItemsRecycler!!.layoutManager =
                    LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

            }
            "Grid" -> {
                featuredProductsAdapter = CategoriesEachRecyclerAdapter(this)
                featuredItemsRecycler!!.layoutManager =
                    GridLayoutManager(this.context, 2, RecyclerView.VERTICAL, false)
            }
            "Horizontal" -> {
                featuredProductsAdapter = CategoriesEachRecyclerAdapter(this, "horizontal")
                featuredItemsRecycler!!.layoutManager =
                    LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
            }
        }
        featuredItemsRecycler!!.adapter = featuredProductsAdapter
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

        var categoryMenu: SubMenu? = if (menu != null) menu!!.addSubMenu("Category") else null

        adapterr!!.submitList(CategoriesDataProvider.getCategoryData() as MutableList<CategoriesModelClass>)
//        CategoriesDataProvider.mutableCollectionList.observeForever { t: MutableList<CategoriesModelClass>? ->
//
//            adapterr!!.submitList(t)
//        }

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
        this.menu = menu
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


}