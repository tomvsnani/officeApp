package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CategoriesEachRecyclerAdapter
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModel
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModelFactory
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import kotlinx.android.synthetic.main.fragment_cart.view.*
import kotlinx.coroutines.*


class CategoryEachViewPagerFragment(var get: CategoriesModelClass?, var callback: () -> Unit) :
    Fragment() {
    var recyclerView: RecyclerView? = null
    var adapterr: CategoriesEachRecyclerAdapter? = null
    var productList: MutableList<CategoriesModelClass> = ArrayList()
    var progressbar: ProgressBar? = null
    var value = get!!.itemName
    var recyclerviewLastLayout: ConstraintLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_category_each_viewpager,
            container,
            false
        )

        recyclerView = view.findViewById(R.id.categoriesRecyclerview)
        recyclerviewLastLayout = view.findViewById(R.id.loadingconstraint)
        progressbar = view.findViewById(R.id.eachcategoryfragprogressbar)
        progressbar!!.visibility = View.GONE
        adapterr = CategoriesEachRecyclerAdapter( this)
        (recyclerView as RecyclerView).layoutManager =
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        (recyclerView as RecyclerView).adapter = adapterr
        recyclerView!!.itemAnimator = null


        val categoriesModelClass: CategoriesViewModel =
            ViewModelProvider(this, CategoriesViewModelFactory(get!!.id))
                .get(CategoriesViewModel::class.java)
        progressbar!!.visibility = View.VISIBLE
        categoriesModelClass.getData()
        categoriesModelClass.mutableLiveData?.observe(viewLifecycleOwner, Observer {

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
                    categoriesModelClass.loadmore(adapterr!!.currentList[adapterr!!.currentList.size - 1])
                }

            }
        })




        return view
    }


}