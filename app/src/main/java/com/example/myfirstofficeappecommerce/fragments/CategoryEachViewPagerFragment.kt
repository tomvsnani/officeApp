package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Adapters.CategoriesEachRecyclerAdapter
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModel
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModelFactory
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import kotlinx.coroutines.*


class CategoryEachViewPagerFragment(var get: CategoriesModelClass?, var callback: () -> Unit) :
    Fragment() {
    private var loginfrag: loginFragment?=null
    var recyclerView: RecyclerView? = null
    var toolbar: Toolbar? = null
    var adapterr: CategoriesEachRecyclerAdapter? = null
    var productList: MutableList<CategoriesModelClass> = ArrayList()
    var progressbar: ProgressBar? = null

    var recyclerviewLastLayout: ConstraintLayout? = null
    var itemSelectedCountTextView: TextView? = null
    var checkoutButton: Button? = null
    var menu: Menu? = null
    var selectedItemDisplayCardView: CardView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_category_each_viewpager,
            container,
            false
        )
        setHasOptionsMenu(true)
        Log.d("clicked", "came")
        itemSelectedCountTextView = view.findViewById(R.id.numberOfItemSelectedTextVieweach)
        checkoutButton = view.findViewById(R.id.checkoutButtoneachfrag)
        selectedItemDisplayCardView = view.findViewById(R.id.itemsselectedindicatorcardvieweachfrag)
        toolbar = view.findViewById(R.id.categoriesEachViewPagerFragmenttoolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).actionBarToggle = ActionBarDrawerToggle(
            activity, (activity as MainActivity).drawerLayout, R.string.openDrawerLayout,
            R.string.closeDrawerLayout
        )

        ((activity as MainActivity).drawerLayout as DrawerLayout).addDrawerListener((activity as MainActivity).actionBarToggle!!)
        ((activity as MainActivity).actionBarToggle as ActionBarDrawerToggle).syncState()

        recyclerView = view.findViewById(R.id.categoriesRecyclerview)
        recyclerviewLastLayout = view.findViewById(R.id.loadingconstraint)
        progressbar = view.findViewById(R.id.eachcategoryfragprogressbar)
        progressbar!!.visibility = View.GONE
        adapterr = CategoriesEachRecyclerAdapter(this)
        (recyclerView as RecyclerView).layoutManager =
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        (recyclerView as RecyclerView).adapter = adapterr
        recyclerView!!.itemAnimator = null


        val categoriesDataProvider: CategoriesViewModel =
            ViewModelProvider(this, CategoriesViewModelFactory(get!!.id))
                .get(CategoriesViewModel::class.java)
        progressbar!!.visibility = View.VISIBLE
        categoriesDataProvider.getProductDataBasedOnColletionId()
            .observe(viewLifecycleOwner, Observer {

                adapterr!!.submitList(it)
                CoroutineScope(Dispatchers.Main).launch {
                    recyclerviewLastLayout!!.visibility = View.GONE
                    progressbar!!.visibility = View.GONE
                }
            })

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == adapterr!!.itemCount - 1 &&
                    adapterr!!.currentList[adapterr!!.itemCount - 1].hasNextPage
                ) {
                    recyclerviewLastLayout!!.visibility = View.VISIBLE
                    categoriesDataProvider.LoadMoreDataBasedOnCollectionId(adapterr!!.currentList[adapterr!!.currentList.size - 1])
                }

            }
        })

        checkoutButton!!.setOnClickListener {
            openCartFragment()
        }

        return view
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("clicked", "hello")
        if (item.itemId == android.R.id.home) {
           // if(loginfrag!=null)
              //  loginfrag!!.dismiss()
            Log.d("clicked", "hello")
            if ((activity as MainActivity).drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                (activity as MainActivity).drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                (activity as MainActivity).drawerLayout?.openDrawer(GravityCompat.START)
            }
            return true


        }
        if (item.itemId == R.id.search_menu) {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment()).addToBackStack(null).commit()
            return true
        }

        if (item.itemId == R.id.profilemenu) {
            loginfrag=  loginFragment(Constants.NORMAL_SIGN_IN, fragment = this)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, loginFragment(Constants.NORMAL_SIGN_IN, fragment = this))
                ?.addToBackStack("ok")?.commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragmentmenu, menu)
        this.menu = menu
        var item: MenuItem = menu.findItem(R.id.cartmenu)
        item.actionView.findViewById<ImageView>(R.id.cartmenuitem).setOnClickListener {
            openCartFragment()
        }
        showOrHideItemCountIndicator()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun openCartFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                CartFragment(
                    ApplicationClass.selectedVariantList?.filter { it.quantityOfItem > 0 })
            ).addToBackStack(null)
            .commit()
    }


    private fun showOrHideItemCountIndicator() {

        var itemCount = Utils.getItemCount()
        itemSelectedCountTextView!!.text = itemCount.toString()
        if (itemCount.toInt() > 0) {
            selectedItemDisplayCardView!!.visibility = View.VISIBLE
            menu!!.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu)
                .apply {
                    text = itemCount.toString()
                    visibility = View.VISIBLE
                }

        } else {
            menu!!.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu).visibility =
                View.INVISIBLE

            selectedItemDisplayCardView!!.visibility = View.INVISIBLE
        }
    }


}