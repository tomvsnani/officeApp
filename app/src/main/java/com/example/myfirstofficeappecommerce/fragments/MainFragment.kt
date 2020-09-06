package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
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
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.MainRecyclerAdapter
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.Adapters.ViewPagerAdapter
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
    var searchEditText: EditText? = null
    var isScrollForward: Boolean = true
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var actionBarToggle: ActionBarDrawerToggle? = null
    var selectedItemsList: List<CategoriesModelClass>? = ApplicationClass.selectedItemsList

    private val SCROLL_TIMEOUT = 4000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)
        searchEditText = view.findViewById(R.id.mainfragmentsearchedittext)
        searchEditText!!.setOnFocusChangeListener { view, b ->
            if (view.hasFocus()) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, SearchFragment())
                    ?.addToBackStack(null)?.commit()

            }
        }
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



        toolbar = view.findViewById(R.id.maintoolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = null

        drawerLayout = view.findViewById(R.id.drawerlayout)
        actionBarToggle = ActionBarDrawerToggle(
            activity, drawerLayout, R.string.openDrawerLayout,
            R.string.closeDrawerLayout
        )
        (drawerLayout as DrawerLayout).addDrawerListener(actionBarToggle!!)
        (actionBarToggle as ActionBarDrawerToggle).syncState()

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
                        if (position == 0) {
                            isScrollForward = true
                            Log.d("tabforward", tablayout!!.tabCount.toString() + " " + position)
                        }
                        if (!isScrollForward) {
                            tablayout?.getTabAt(position - 1)?.select()
                        } else
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
            MainRecyclerAdapter(activity as MainActivity, categoryMap, Constants.SCROLL_TYPE)

        (recyclerView as RecyclerView).adapter = adapterr
        (recyclerView as RecyclerView).layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        (adapterr as MainRecyclerAdapter).submitList(categoryMap?.get("Phones"))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout?.openDrawer(GravityCompat.START)
            }
            return true
        }
        if (item.itemId == R.id.cartmenu) {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, CartFragment(ApplicationClass.selectedItemsList))
                .addToBackStack(null)
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("incheck", selectedItemsList!!.size.toString())
        menu.findItem(R.id.cartmenu).actionView.findViewById<ImageView>(R.id.cartmenuitem)
            .setOnClickListener {
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CartFragment(ApplicationClass.selectedItemsList))
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