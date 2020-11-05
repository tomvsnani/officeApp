package com.example.myfirstofficeappecommerce.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CheckoutOverViewItemsAdapter
import com.example.myfirstofficeappecommerce.Adapters.ShippingRatesAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.DiscountBottomSheet
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.ChangeShippingRatesLayoutBinding
import com.example.myfirstofficeappecommerce.databinding.CheckoutOverviewLayoutBinding
import com.shopify.buy3.*
import com.shopify.graphql.support.ID


class CheckoutOverViewFragment(

    var shippingList: MutableList<UserDetailsModelClass>,
    var shippingPrice: Float,
    var taxPrice: Float,
    var userDetailsModelClass: UserDetailsModelClass,

    var checkoutId: String

) : Fragment() {

    var binding: CheckoutOverviewLayoutBinding? = null
    var adapter: CheckoutOverViewItemsAdapter? = null
    var list = ApplicationClass.selectedVariantList
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.checkout_overview_layout, container, false)
      binding = CheckoutOverviewLayoutBinding.bind(v)
        var a = 0f;
        for (i in ApplicationClass.selectedVariantList!!)
            a += i.price!!
        binding!!.backbuttonimage.setOnClickListener { parentFragment!!.childFragmentManager.popBackStackImmediate() }

        binding!!.continuetopaymentbutton.setOnClickListener {
            parentFragment!!.childFragmentManager.beginTransaction()
                .replace(R.id.container1, PaymentFragment(shippingList, checkoutId))
                .addToBackStack(null).commit()
        }

        binding!!.discountapplyconstraint.setOnClickListener {
            DiscountBottomSheet(this, checkoutId).show(activity!!.supportFragmentManager, "")
        }


        binding!!.checkoutOverViewPriceTextView.text =
            a.toString()
        binding!!.checkoutOverViewShippingCostTextView.text =
            (shippingPrice + taxPrice).toString()
        binding!!.checkoutOverViewTotalPriceTextView.text =
            (a.toFloat() + shippingPrice + taxPrice).toString()

        binding!!.chooseAddressnameTextView.text =
            userDetailsModelClass.title + " " + userDetailsModelClass.subTitle
        binding!!.chooseAddressPhoneNumber.text =
            userDetailsModelClass.phoneNumber
        binding!!.chooseAddressaddressTextView.text =
            userDetailsModelClass.hnum + " , " + userDetailsModelClass.city + " - " + userDetailsModelClass.pinCode + " \n \n" + userDetailsModelClass.state + " , " + userDetailsModelClass.country

        binding!!.checkoutoverviewrecyclerview.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = CheckoutOverViewItemsAdapter(context!!)

        binding!!.checkoutoverviewrecyclerview.adapter = adapter
        //  binding.checkoutoverviewrecyclerview.addItemDecoration(DividerItemDecoration(context!!,RecyclerView.VERTICAL))
        adapter!!.submitList(list)


        binding!!.changeaddressbutton.setOnClickListener {

            activity!!.onBackPressed()

        }
        return v
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)


        super.onCreate(savedInstanceState)
    }
}