package com.example.myfirstofficeappecommerce.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.CategoriesEachRecyclerAdapter
import com.example.myfirstofficeappecommerce.Adapters.CollectionsAdapter
import com.example.myfirstofficeappecommerce.Adapters.DifferentProductsCategoriesRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Adapters.HorizontalScrollViewPagerAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.databinding.FragmentMainBinding
import com.example.myfirstofficeappecommerce.databinding.TimerBannerHorizontalLayoutBinding
import com.example.myfirstofficeappecommerce.databinding.TimerBannerVerticalLayoutBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import kotlin.math.abs


class MainFragment() : Fragment() {
    private var millies: Long? = null
    private var recyclerView: RecyclerView? = null;
    private var adapterr: CollectionsAdapter? = null;
    var list: List<ModelClass>? = null
    private var viewPager2Banner1: ViewPager2? = null
    var tablayoutBanner1: TabLayout? = null;
    var categoryMap: LinkedHashMap<String, List<CategoriesModelClass>>? = null;
    private var searchEditText: EditText? = null
    var isScrollForward: Boolean = true
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var selectedItemsList: List<CategoriesModelClass>? = ApplicationClass.selectedItemsList
    var navigationView: NavigationView? = null
    var a: MutableList<CategoriesModelClass> = ArrayList()
    private var binding: FragmentMainBinding? = null
    private var viewpager2Banner2: ViewPager2? = null
    var tablayoutBanner2: TabLayout? = null
    var menu: Menu? = null
    private var featuredItemsRecycler: RecyclerView? = null
    private var typeOfFeaturedList: String = "Grid"
    private var featuredProductsAdapter: CategoriesEachRecyclerAdapter? = null
    private var bannerDiffProductsCatgoriesRecycler: RecyclerView? = null
    private var daysTextView1: TextView? = null
    private var daysTextView2: TextView? = null
    private var hoursTextView1: TextView? = null
    private var hoursTextView2: TextView? = null
    private var minutesTextView1: TextView? = null
    private var minutesTextView2: TextView? = null
    private var secondsTextView1: TextView? = null
    private var secondsTextView2: TextView? = null


    private var daysTextView1H: TextView? = null
    private var daysTextView2H: TextView? = null
    private var hoursTextView1H: TextView? = null
    private var hoursTextView2H: TextView? = null
    private var minutesTextView1H: TextView? = null
    private var minutesTextView2H: TextView? = null
    private var secondsTextView1H: TextView? = null
    private var secondsTextView2H: TextView? = null

    private var daysConstant = 60 * 60 * 1000 * 24

    private val hourConstant = 60 * 60 * 1000

    private val minConst = 60 * 1000

    val secondConst = 1000
    private var repeatJobhorizontal: Job? = null
    private var repeatJobvertial: Job? = null


    private val SCROLL_TIMEOUT = 4000L

    private var linearlayoutFlipFront: LinearLayout? = null
    private var linearlayoutFlipBack: LinearLayout? = null
    private var textViewFlipFront: TextView? = null
    private var textViewFlipBack: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(context)
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
        millies = CategoriesDataProvider.getMillies()

        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        binding = FragmentMainBinding.bind(view)
        linearlayoutFlipFront = binding!!.flipinclude.cardflipfront
        textViewFlipFront = binding!!.flipinclude.textfront
        linearlayoutFlipBack = binding!!.flipinclude.cardflipback
        textViewFlipBack = binding!!.flipinclude.textback
        (activity as MainActivity).unlockDrawer()

        setHasOptionsMenu(true)

        setUpToolbar(view)

//activity!!.supportFragmentManager.beginTransaction().replace(R.id.container,testfrag()).commit()
        etUpDrawerLayout()

        setUpSearchEditText(view)

        firstLayoutHoriZontalScrollItemNames(view)

        setUpScrollingBanners(view)

        setUpFeaturedProducts()


        bannerDiffProductsCatgoriesRecycler = binding!!.bennerWithDiffProductsCategoriesRecycler
        bannerDiffProductsCatgoriesRecycler!!.adapter =
            DifferentProductsCategoriesRecyclerViewAdapter()
        bannerDiffProductsCatgoriesRecycler!!.layoutManager =
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)

        featuredProductsAdapter!!.submitList(CategoriesDataProvider.getCategoryData() as MutableList<CategoriesModelClass>)

        return view
    }

    override fun onResume() {

        setUpVerticalBannerTimer()

        setUpHoriZontalTimer()
        super.onResume()
    }


    override fun onStart() {

        setFlipAnimation()

        super.onStart()
    }

    private fun setFlipAnimation() {
        val scale = resources.displayMetrics.density
        linearlayoutFlipFront!!.cameraDistance = 9000 * scale
        linearlayoutFlipBack!!.cameraDistance = 9000 * scale

        linearlayoutFlipFront!!.setOnClickListener {
            ObjectAnimator.ofFloat(linearlayoutFlipFront, "rotationY", 0f, 180f).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
            }.start()

            ObjectAnimator.ofFloat(linearlayoutFlipBack, "rotationY", -180f, 0f).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
            }.start()

            ObjectAnimator.ofFloat(linearlayoutFlipFront, "alpha", 1f, 0f).apply {
                duration = 1
                interpolator = AccelerateDecelerateInterpolator()
                startDelay = 700
            }.start()

            ObjectAnimator.ofFloat(linearlayoutFlipBack, "alpha", 0f, 1f).apply {
                duration = 1
                interpolator = AccelerateDecelerateInterpolator()
                startDelay = 700
            }.start()

        }
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

    private fun setUpVerticalBannerTimer() {
        var verticaltimerbinding =
            TimerBannerVerticalLayoutBinding.bind(binding!!.timerverticalinclude.root)
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

    private fun horizontalTimer(millies: Long) {
        repeatJobhorizontal = CoroutineScope(Dispatchers.Main).launch {
            repeat(1) {
                if (isActive) {

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

                    hoursTextView1H!!.text =
                        if (hours < 10) 0.toString() else (hours / 10).toString()
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
    }

    override fun onStop() {
        repeatJobhorizontal!!.cancel()
        repeatJobvertial!!.cancel()
        super.onStop()
    }

    private fun verticaltimer(millies: Long) {
        repeatJobvertial = CoroutineScope(Dispatchers.Main).launch {
            repeat(1) {
                if (isActive) {
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

                    hoursTextView1!!.text =
                        if (hours < 10) 0.toString() else (hours / 10).toString()
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