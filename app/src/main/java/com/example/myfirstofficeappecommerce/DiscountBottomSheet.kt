package com.example.myfirstofficeappecommerce

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.databinding.FragmentDiscountBottomSheetListDialogBinding
import com.example.myfirstofficeappecommerce.fragments.CheckoutOverViewFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID
import java.lang.Exception


class DiscountBottomSheet(var fragment: CheckoutOverViewFragment, var checkoutId: String) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,

        State: Bundle?
    ): View? {
        var view = inflater.inflate(
            R.layout.fragment_discount_bottom_sheet_list_dialog,
            container,
            false
        )
        var binding = FragmentDiscountBottomSheetListDialogBinding.bind(view)
        binding.applycouponedittextbutton.setOnClickListener {
            if (binding.applycouponedittext.text.toString().isNotEmpty()) {
                applyDiscountQuery(checkoutId)
            } else {
                Toast.makeText(context, "Please enter a coupon code", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog


        bottomSheetDialog.setOnShowListener { dia ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet =

                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            var behaviour = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).skipCollapsed = true
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).isHideable = true
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).peekHeight = 0

        }
        return bottomSheetDialog
    }

    private fun applyDiscountQuery(checkoutId: String) {
        dismiss()
        (activity as MainActivity).binding!!.mainactivityprogressbar.visibility = View.VISIBLE
        var queryy = Storefront.mutation { _queryBuilder ->
            _queryBuilder.checkoutDiscountCodeApply(
                (activity as MainActivity).DISCOUNT,
                ID(checkoutId)
            ) { _queryBuilder ->
                _queryBuilder.checkout { _queryBuilder ->
                    _queryBuilder.totalPrice().webUrl()
                }.userErrors { _queryBuilder ->
                    _queryBuilder.field().message()
                }
            }
        }

        CategoriesDataProvider.graphh!!.mutateGraph(queryy).enqueue(object :
            GraphCall.Callback<Storefront.Mutation> {

            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {

                if (!response.hasErrors() && response.data()!!.checkoutDiscountCodeApply.userErrors.isEmpty()) {

                    fragment.activity?.runOnUiThread {
                        (fragment.activity as MainActivity).binding!!.mainactivityprogressbar.visibility =
                            View.GONE

                        try {

                            Toast.makeText(fragment.context, "Discount Applied", Toast.LENGTH_SHORT)
                                .show()
                            var a = response
                                .data()!!.checkoutDiscountCodeApply
                                ?.checkout
                                ?.totalPrice!!.toEngineeringString()
                            Log.d("applied", a)
                            fragment?.binding?.checkoutOverViewTotalPriceTextView?.text = a
                            Log.d("applied", a)
                        } catch (e: Exception) {
                            Log.d("errorocc", e.printStackTrace().toString())
                        }


                    }


                } else {


                    if (response.data()!!.checkoutDiscountCodeApply.userErrors.isNotEmpty())

                        for (i in response.data()!!.checkoutDiscountCodeApply.userErrors)
                            (activity as MainActivity).runOnUiThread {
                                (activity as MainActivity).binding!!.mainactivityprogressbar.visibility =
                                    View.GONE
                                (activity as MainActivity).binding!!.mainactivityprogressbar.visibility =
                                    View.GONE
                                Toast.makeText(
                                    context,
                                    i.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                }
            }

            override fun onFailure(error: GraphError) {
                activity!!.runOnUiThread {
                    (activity as MainActivity).binding!!.mainactivityprogressbar.visibility =
                        View.GONE
                }

                Log.d("discounterr", error.message.toString())
            }
        })

    }
}