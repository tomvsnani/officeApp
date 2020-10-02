package com.example.myfirstofficeappecommerce

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.OrdersAdaptes
import com.example.myfirstofficeappecommerce.Adapters.RecentsAdapter
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass


class RecentsFragment(var recentsList:List<VariantsModelClass>) : Fragment() {
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var recentsAdapter: RecentsAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.fragment_recents, container, false)
        (activity as MainActivity).lockDrawer()
        toolbar=view?.findViewById(R.id.recentsToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recyclerView = view?.findViewById(R.id.recentsRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recentsAdapter = RecentsAdapter()
        recyclerView?.adapter = recentsAdapter
        recentsAdapter!!.submitList(recentsList)

        return view
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0..1) {
            menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
        super.onCreate(savedInstanceState)
    }

}


