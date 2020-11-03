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
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.ChangeShippingRatesLayoutBinding
import com.example.myfirstofficeappecommerce.databinding.CheckoutOverviewLayoutBinding
import com.shopify.buy3.*
import com.shopify.graphql.support.ID


class CheckoutOverViewFragment(
    var webUrl: String,
    var shippingPrice: Float,
    var taxPrice: Float,
    var userDetailsModelClass: UserDetailsModelClass,
    var list: MutableList<VariantsModelClass>,
    var shippingList: List<UserDetailsModelClass>,
    var checkoutId: String,
    var shippingRatesAdapter: ShippingRatesAdapter? = null

) : Fragment() {
    var adapter: CheckoutOverViewItemsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.checkout_overview_layout, container, false)
        val binding = CheckoutOverviewLayoutBinding.bind(v)
        var a = 0f;
        for (i in ApplicationClass.selectedVariantList!!)
            a += i.price!!
        binding!!.backbuttonimage.setOnClickListener { parentFragment!!.childFragmentManager.popBackStackImmediate() }

        binding!!.continuetopaymentbutton.setOnClickListener {
            parentFragment!!.childFragmentManager.beginTransaction()
                .replace(R.id.container1, PaymentFragment(shippingList, checkoutId))
                .addToBackStack(null).commit()
        }






        binding.shippingAddresseditButton.setOnClickListener {
            selectShippingProviders(binding, a)
        }


        binding.checkoutOverViewPriceTextView.text =
            a.toString()
        binding.checkoutOverViewShippingCostTextView.text =
            (shippingPrice + taxPrice).toString()
        binding.checkoutOverViewTotalPriceTextView.text =
            (a.toFloat() + shippingPrice + taxPrice).toString()

        binding.chooseAddressnameTextView.text =
            userDetailsModelClass.title + " " + userDetailsModelClass.subTitle
        binding.chooseAddressPhoneNumber.text =
            userDetailsModelClass.phoneNumber
        binding.chooseAddressaddressTextView.text =
            userDetailsModelClass.hnum + " , " + userDetailsModelClass.city + " - " + userDetailsModelClass.pinCode + " \n \n" + userDetailsModelClass.state + " , " + userDetailsModelClass.country

        binding.checkoutoverviewrecyclerview.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = CheckoutOverViewItemsAdapter(context!!)

        binding.checkoutoverviewrecyclerview.adapter = adapter
        //  binding.checkoutoverviewrecyclerview.addItemDecoration(DividerItemDecoration(context!!,RecyclerView.VERTICAL))
        adapter!!.submitList(list)


        binding.changeaddressbutton.setOnClickListener {

            activity!!.onBackPressed()

        }
        return v
    }


    private fun selectShippingProviders(
        binding: CheckoutOverviewLayoutBinding,
        a: Float
    ) {
        var alertDialog: AlertDialog = AlertDialog.Builder(context!!).create()
        var view = LayoutInflater.from(context!!)
            .inflate(R.layout.change_shipping_rates_layout, null, false)
        var binding1 = ChangeShippingRatesLayoutBinding.bind(view)
        alertDialog.setView(view)
        shippingRatesAdapter = ShippingRatesAdapter(this, checkoutId) { clickedPos ->
            var modelClass = shippingList[clickedPos]
            binding.checkoutOverViewShippingCostTextView.text =
                (modelClass.shippingPrice.toInt() + taxPrice).toString()
            binding.checkoutOverViewTotalPriceTextView.text =
                (a.toFloat() + modelClass.shippingPrice.toInt() + taxPrice).toString()
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Continue"
        ) { p0, p1 ->

            shippingRatesAdapter!!.getNewWebAddressBasedOnShippingProvider(shippingRatesAdapter!!.currentList.find { it.isSelectedAddress }!!)
        }

        binding1.changeshippingaddressrecycler.adapter = shippingRatesAdapter
        binding1.changeshippingaddressrecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        shippingRatesAdapter!!.submitList(shippingList as MutableList<UserDetailsModelClass>)

        alertDialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)


        super.onCreate(savedInstanceState)
    }
}