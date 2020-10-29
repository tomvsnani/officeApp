package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.WishlistAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.R


class WishListFragment() : Fragment() {
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var ordersAdapters: WishlistAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition= inflater.inflateTransition(R.transition.fragment_fade_trans)
        super.onCreate(savedInstanceState)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
            activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0..1) {
            menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }


    override fun onStart() {
        var token = activity!!.getPreferences(Activity.MODE_PRIVATE).getString("token", "")
        if (token == "") {
            Toast.makeText(context, "Please Login", Toast.LENGTH_SHORT).show()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.container, loginFragment(
                Constants.NORMAL_SIGN_IN,fragment = this))
             .commit()
        }
        super.onStart()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View? = inflater.inflate(R.layout.fragment_wish_list, container, false)
        (activity as MainActivity).lockDrawer()

        toolbar=view?.findViewById(R.id.wishlistToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recyclerView = view?.findViewById(R.id.wishlistRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        ordersAdapters = WishlistAdapter(this)
        recyclerView?.adapter = ordersAdapters
        ApplicationClass.mydb!!.dao().getAllFavVariants(true).observe(viewLifecycleOwner, Observer {
            ordersAdapters!!.submitList(it)
        })

        return view
    }

}