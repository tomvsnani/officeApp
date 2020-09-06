package com.example.myfirstofficeappecommerce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CartItemsSelectedRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass


class CartFragment(var selectedItemsList:List<CategoriesModelClass>) : Fragment() {

    var toolbar: Toolbar? = null
    var slecetdItemsRecycler:RecyclerView?=null
    var recommendedItemsRecycler:RecyclerView?=null
    var itemsSelectedAdapter:CartItemsSelectedRecyclerViewAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_cart, container, false)
        toolbar = view.findViewById(R.id.cartToolbar)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)

        slecetdItemsRecycler=view.findViewById(R.id.cartSelecetedRecyclerview)
        recommendedItemsRecycler=view.findViewById(R.id.cartRecyclerviewRecommondedItems)
        itemsSelectedAdapter= CartItemsSelectedRecyclerViewAdapter()
        slecetdItemsRecycler!!.adapter=itemsSelectedAdapter
        slecetdItemsRecycler!!.layoutManager=LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        itemsSelectedAdapter!!.submitList(selectedItemsList)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}