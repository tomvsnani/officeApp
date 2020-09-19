package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CategoriesEachRecyclerAdapter
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModel
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModelFactory
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R


class CategoryEachViewPagerFragment(var get: CategoriesModelClass?, var callback: () -> Unit) :
    Fragment() {
    var recyclerView: RecyclerView? = null
    var adapterr: CategoriesEachRecyclerAdapter? = null
    var productList: MutableList<CategoriesModelClass> = ArrayList()
    var progressbar: ProgressBar? = null
    var value = get!!.itemName


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
        progressbar = view.findViewById(R.id.eachcategoryfragprogressbar)
        progressbar!!.visibility = View.GONE
        adapterr = CategoriesEachRecyclerAdapter(callback, this)
        (recyclerView as RecyclerView).layoutManager =
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        (recyclerView as RecyclerView).adapter = adapterr
        recyclerView!!.itemAnimator = null

        Log.d("valueoncreateview", value)

//      if(!isRemoteRequestMade)
        var categoriesModelClass: CategoriesViewModel =
            ViewModelProvider(this, CategoriesViewModelFactory(get!!.id))
                .get( CategoriesViewModel::class.java)
        categoriesModelClass.getData()
        categoriesModelClass.mutableLiveData?.observe(viewLifecycleOwner, Observer {
            adapterr!!.submitList(it)
        })
        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("valuecrate", value)
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        Log.d("valuepause", value)
        super.onPause()
    }

    override fun onStop() {
        Log.d("valuestop", value)
        super.onStop()
    }


    override fun onDestroyView() {
        Log.d("valuedestroyview", value)
        super.onDestroyView()
    }


    override fun onDestroy() {
        Log.d("valuedestroy", value)
        super.onDestroy()
    }

    override fun onStart() {
        Log.d("valuestart", value)
        super.onStart()
    }

    override fun onResume() {
        Log.d("valueresume", value)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("valueviewcreated", value)
        super.onViewCreated(view, savedInstanceState)
    }


}