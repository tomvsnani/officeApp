package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myfirstofficeappecommerce.R

class CheckOutMainWrapperFragment(var checkoutid: String, var totaltax: Float) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.activity_checkout_layout, container, false)
        setHasOptionsMenu(true)
        childFragmentManager.beginTransaction()
            .replace(R.id.container1, FinalisingOrderFragment(checkoutid, totaltax))
            .addToBackStack(null).commit()
        activity!!.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (this@CheckOutMainWrapperFragment.isAdded && childFragmentManager.backStackEntryCount > 1) {

                    childFragmentManager.popBackStackImmediate();
                } else {

                    isEnabled = false
                    if (activity != null)
                        activity!!.onBackPressed()

                }
            }

        })
        return v
    }

    fun clearAllFragmets() {
        childFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        activity!!.supportFragmentManager.popBackStackImmediate(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            activity!!.supportFragmentManager.popBackStackImmediate()
        return super.onOptionsItemSelected(item)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        Log.d("onprepareaccount", "yess")
        for (i in 0..1) {
            menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }
}