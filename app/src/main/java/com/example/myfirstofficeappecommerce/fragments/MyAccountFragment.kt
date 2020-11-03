package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.EditUserDetailsFragment
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentMyAccountBinding


class MyAccountFragment : Fragment() {
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    var binding: FragmentMyAccountBinding? = null
    var loginfrag: loginFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_my_account, container, false)
        binding = FragmentMyAccountBinding.bind(view)

        setHasOptionsMenu(true)
        toolbar = binding!!.myaccountToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).lockDrawer()
        Log.d("onprepareaccount", "oncreateview")
        if ((activity!!.application as ApplicationClass).getCustomerToken(
                activity = activity as MainActivity
            ).isBlank()
        )
        //  loginfrag=  loginFragment(Constants.NORMAL_SIGN_IN, fragment = this)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, loginFragment(Constants.NORMAL_SIGN_IN, fragment = this))
                ?.addToBackStack("ok")?.commit()

        return view
    }

    override fun onStart() {
        super.onStart()
        binding!!.addressbookconstraint.setOnClickListener {

        }

        binding!!.favouritesconstraint.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.container,
                WishListFragment()
            ).addToBackStack(null)
                .commit()
        }

        binding!!.ordersconstraint.setOnClickListener {


            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.container,
                OrdersFragment(ApplicationClass.selectedVariantList!!)
            ).addToBackStack(null)
                .commit()
        }

        binding!!.profileconstraint.setOnClickListener {

            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.container,
                EditUserDetailsFragment()
            ).addToBackStack(null)
                .commit()
        }

        binding!!.recentlyviewedconstraint.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.container,
                RecentsFragment(ApplicationClass.recentsList!!)
            ).addToBackStack(null)
                .commit()
        }
        binding!!.signoutbutton.setOnClickListener {
            activity!!.getPreferences(Activity.MODE_PRIVATE).edit()
                .remove(Constants.LOGGED_IN_TOKEN).apply()
            ApplicationClass.selectedVariantList?.clear()
            ApplicationClass.addressList?.clear()
            ApplicationClass.recentsList?.clear()
            ApplicationClass.shippingratesAddressList?.clear()
            activity!!.supportFragmentManager.popBackStackImmediate()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
        Log.d("onprepareaccount", "oncreate")
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       // Log.d("onprepareaccount", "yess")
        if (item.itemId == android.R.id.home) {
//            if(loginfrag!=null) {
            Log.d("backcalled", "accountbackcalled")
//                loginfrag!!.dismiss()
//            }
            (activity as MainActivity)?.supportFragmentManager.popBackStackImmediate()

            return true
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
      //  Log.d("onprepareaccount", "yess")
        for (i in 0..1) {
            menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }


}