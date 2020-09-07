package com.example.myfirstofficeappecommerce

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CartItemRecommendedAdapter
import com.example.myfirstofficeappecommerce.Adapters.CartItemsSelectedRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass


class CartFragment(var selectedItemsList: List<CategoriesModelClass>?) : Fragment() {

    var toolbar: Toolbar? = null
    var slecetdItemsRecycler: RecyclerView? = null
    var recommendedItemsRecycler: RecyclerView? = null
    var itemsSelectedAdapter: CartItemsSelectedRecyclerViewAdapter? = null
    var totalAmountTextView: TextView? = null
    var count = 0
    var recommendedAdapter: CartItemRecommendedAdapter? = null
    var list: MutableList<CategoriesModelClass> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_cart, container, false)
        totalAmountTextView = view.findViewById(R.id.totalamounttextviewcart)
        toolbar = view.findViewById(R.id.cartToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)


        slecetdItemsRecycler = view.findViewById(R.id.cartSelecetedRecyclerview)
        recommendedItemsRecycler = view.findViewById(R.id.cartRecyclerviewRecommondedItems)
        itemsSelectedAdapter = CartItemsSelectedRecyclerViewAdapter(this)
        slecetdItemsRecycler!!.itemAnimator = null
        slecetdItemsRecycler!!.adapter = itemsSelectedAdapter
        slecetdItemsRecycler!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        itemsSelectedAdapter!!.submitList(selectedItemsList as MutableList<CategoriesModelClass>?)



        recommendedItemsRecycler!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        list = selectedItemsList!!.toMutableList()
        recommendedAdapter = CartItemRecommendedAdapter(this) { modelClass ->
            modelClass.quantityOfItem++
            var count = list.filter { it.id == modelClass.id && it.groupId == modelClass.groupId }
            if (count.isNotEmpty())
                list[list.indexOf(modelClass)] = modelClass
            else
                list.add(modelClass)

            itemsSelectedAdapter!!.submitList(list)
        }
        recommendedItemsRecycler!!.adapter = recommendedAdapter
        recommendedAdapter!!.submitList(CategoriesDataProvider().getRecommendedData())




        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0..1) {
            menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }
}