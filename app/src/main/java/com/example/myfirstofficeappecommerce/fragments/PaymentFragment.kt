package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentPaymentBinding
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID


class PaymentFragment(var shippingList: List<UserDetailsModelClass>, var checkoutId: String) :
    Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentPaymentBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_payment, container, false)

        binding = FragmentPaymentBinding.bind(view)
        setHasOptionsMenu(true)

        binding!!.backbuttonimage.setOnClickListener { parentFragment!!.childFragmentManager.popBackStackImmediate() }
        binding!!.otherpaymenttextview.setOnClickListener {
            if (ApplicationClass.weburl != null && ApplicationClass.weburl!!.isNotBlank() && ApplicationClass!!.weburl!!.isNotEmpty())
                openWebView()
            else {
                if (shippingList.isNotEmpty())
                    getNewWebAddressBasedOnShippingProvider(shippingList[0])


            }
        }


        binding!!.creditcardpaymenttextview.setOnClickListener {

            parentFragment!!.childFragmentManager.beginTransaction()
                .replace(R.id.container1, CreditcardDetailsFragment())
                .addToBackStack(null)
                .commit()


        }

        return view
    }

    fun getNewWebAddressBasedOnShippingProvider(modelclass: UserDetailsModelClass) {
        val query1 = Storefront.mutation { m: Storefront.MutationQuery ->
            m.checkoutShippingLineUpdate(
                ID(checkoutId), modelclass.subTitle,
                Storefront.CheckoutShippingLineUpdatePayloadQueryDefinition { update: Storefront.CheckoutShippingLineUpdatePayloadQuery ->

                    update.userErrors { errors: Storefront.UserErrorQuery ->
                        errors.field().message()
                    }.checkout { _queryBuilder ->
                        _queryBuilder.webUrl()
                    }
                }
            )
        }

        var call1 =
            CategoriesDataProvider.graphh!!.mutateGraph(query1)
        call1.enqueue(object : GraphCall.Callback<Storefront.Mutation> {


            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {

                ApplicationClass.weburl =
                    response.data()!!.checkoutShippingLineUpdate.checkout.webUrl
                openWebView()

            }

            override fun onFailure(error: GraphError) {

            }

        })
    }

    private fun openWebView() {
        parentFragment!!.childFragmentManager.beginTransaction()
            .replace(
                R.id.container1,
                WebViewFragment(
                    ApplicationClass.weburl!!,
                    "checkout"
                )
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)

        super.onCreate(savedInstanceState)
    }


}