package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.CartItemRecommendedAdapter
import com.example.myfirstofficeappecommerce.Adapters.CartItemsSelectedRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import com.shopify.graphql.support.Input
import java.util.concurrent.TimeUnit


class CartFragment(var selectedItemsList: List<VariantsModelClass>?) : Fragment() {

    var toolbar: Toolbar? = null
    var slecetdItemsRecycler: RecyclerView? = null
    var recommendedItemsRecycler: RecyclerView? = null
    var itemsSelectedAdapter: CartItemsSelectedRecyclerViewAdapter? = null
    var totalAmountTextView: TextView? = null
    var count = 0
    var recommendedAdapter: CartItemRecommendedAdapter? = null
    var proceedTextViewCart: TextView? = null
    var list: MutableList<VariantsModelClass> = ArrayList()
    var emptycartlayout: ConstraintLayout? = null
    var cartNestedScroll: NestedScrollView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).lockDrawer()
        selectedItemsList = ApplicationClass.selectedVariantList
        var view: View = inflater.inflate(
            R.layout.fragment_cart,
            container,
            false
        )
        (activity as MainActivity).lockDrawer()
        totalAmountTextView = view.findViewById(R.id.totalamounttextviewcart)
        setHasOptionsMenu(true)
        toolbar = view.findViewById(R.id.cartToolbar)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)


        emptycartlayout = view.findViewById(R.id.emptycartlayout)
        slecetdItemsRecycler = view.findViewById(R.id.cartSelecetedRecyclerview)
        recommendedItemsRecycler = view.findViewById(R.id.cartRecyclerviewRecommondedItems)
        proceedTextViewCart = view.findViewById(R.id.proceedTextViewCart)
        cartNestedScroll = view.findViewById(R.id.include2)

        itemsSelectedAdapter = CartItemsSelectedRecyclerViewAdapter(this) {
            this.list.clear()
            this.list = it as MutableList<VariantsModelClass>

        }
        if (selectedItemsList!!.isEmpty()) {
            Log.d("listtt", selectedItemsList.toString())
            emptycartlayout!!.visibility = View.VISIBLE
            cartNestedScroll!!.visibility = View.GONE
        } else {
            Log.d("listtt1", selectedItemsList.toString())
            emptycartlayout!!.visibility = View.GONE
            cartNestedScroll!!.visibility = View.VISIBLE
        }
        slecetdItemsRecycler!!.itemAnimator = null
        slecetdItemsRecycler!!.adapter = itemsSelectedAdapter
        slecetdItemsRecycler!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        itemsSelectedAdapter!!.submitList(ApplicationClass.selectedVariantList!!.filter { it.quantityOfItem > 0 } as MutableList<VariantsModelClass>)


        recommendedItemsRecycler!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        list = selectedItemsList!!.toMutableList()


        recommendedAdapter = CartItemRecommendedAdapter(this) { modelClass ->
            //   Log.d("recommendedqu",modelClass.quantityOfItem.toString())
            modelClass.quantityOfItem++
            //  Log.d("recommendedqu",modelClass.quantityOfItem.toString())

            var count =
                list.filter { it.id == modelClass.id && it.parentProductId == modelClass.parentProductId }
            if (count.isNotEmpty()) {
                list.find { it.id == modelClass.id && it.parentProductId == modelClass.parentProductId }!!.quantityOfItem =
                    modelClass.quantityOfItem

                (ApplicationClass.selectedVariantList as MutableList).find { it.id == modelClass.id && it.parentProductId == modelClass.parentProductId }!!.quantityOfItem =
                    modelClass.quantityOfItem

//                (ApplicationClass.selectedItemsList as MutableList).find { it.id==modelClass.parentProductId }!!.quantityOfItem++
            } else {
                Log.d("recommendedqu", modelClass.quantityOfItem.toString())
                list.add(modelClass)
                (ApplicationClass.selectedVariantList as MutableList).add(modelClass)

//                (ApplicationClass.selectedItemsList as MutableList).add( CategoriesModelClass(id=modelClass.parentProductId!!,quantityOfItem = modelClass.quantityOfItem))
//
            }

            itemsSelectedAdapter!!.submitList(list.filter { it.quantityOfItem > 0 } as MutableList<VariantsModelClass>)


        }

        recommendedItemsRecycler!!.adapter = recommendedAdapter
        recommendedAdapter!!.submitList(
            CategoriesDataProvider.getRecommendedData() as MutableList<VariantsModelClass>
        )

        proceedTextViewCart!!.setOnClickListener {
            var token = activity!!.getPreferences(Activity.MODE_PRIVATE).getString("token", "")
            if (token == "") {
                Toast.makeText(context, "Please Login", Toast.LENGTH_SHORT).show()
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment())
                    .addToBackStack(null).commit()
            } else {
                createCheckout()
            }


        }


        return view
    }

    private fun createCheckout() {
        var checkoutLineItemInput: MutableList<CheckoutLineItemInput>? = ArrayList()
        for (i in ApplicationClass.selectedVariantList!!) {
            checkoutLineItemInput?.add(CheckoutLineItemInput(i.quantityOfItem, ID(i.id)))
            i.isOrdered = true
        }
        val input = CheckoutCreateInput()
            .setLineItemsInput(
                Input.value(
                    checkoutLineItemInput
                )
            )

        val query = mutation { mutationQuery: MutationQuery ->
            mutationQuery
                .checkoutCreate(
                    input
                ) { createPayloadQuery: CheckoutCreatePayloadQuery ->
                    createPayloadQuery
                        .checkout { checkoutQuery: CheckoutQuery ->
                            checkoutQuery
                                .webUrl()
                        }
                        .userErrors { userErrorQuery: UserErrorQuery ->
                            userErrorQuery
                                .field()
                                .message()
                        }
                }
        }

        CategoriesDataProvider.graphh!!.mutateGraph(query).enqueue(object :
            GraphCall.Callback<Mutation> {
            override fun onResponse(response: GraphResponse<Mutation>) {
                if (response.data()!!.checkoutCreate.userErrors.isNotEmpty()) {
                    // handle user friendly errors
                } else {
                    val checkoutId = response.data()!!.checkoutCreate.checkout.id.toString()
                    val checkoutWebUrl = response.data()!!.checkoutCreate.checkout.webUrl


                    val queryy = query { rootQuery ->
                        rootQuery
                            .node(
                                ID(checkoutId)
                            ) { nodeQuery ->
                                nodeQuery
                                    .onCheckout { checkoutQuery ->
                                        checkoutQuery
                                            .availableShippingRates { availableShippingRatesQuery ->
                                                availableShippingRatesQuery
                                                    .ready()
                                                    .shippingRates { shippingRateQuery ->
                                                        shippingRateQuery
                                                            .handle()
                                                            .price()
                                                            .title()
                                                    }
                                            }
                                    }
                            }
                    }

                    CategoriesDataProvider.graphh!!.queryGraph(queryy).enqueue(
                        object :
                            GraphCall.Callback<Storefront.QueryRoot> {
                            override fun onResponse(response: GraphResponse<QueryRoot>) {
                                val checkout = response.data()!!.node as Checkout
                                val shippingRates = checkout.availableShippingRates.shippingRates
                                Log.d("ratess", shippingRates.toString())

                            }

                            override fun onFailure(error: GraphError) {
                                Log.d("ratessf", error.message.toString())
                            }
                        },
                        null,
                        RetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
                            .whenResponse<Storefront.QueryRoot> { responsee: GraphResponse<Storefront.QueryRoot> ->
                                ((responsee as GraphResponse<QueryRoot>).data()!!
                                    .node as Payment).ready == false
                            }
                            .maxCount(12)
                            .build()
                    )

                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, FinalisingOrderFragment(checkoutId))
                        .addToBackStack(null).commit()


                    Log.d("checkoutsuccess", "$checkoutId $checkoutWebUrl")
                }
            }

            override fun onFailure(error: GraphError) {
                Log.d("checkouterror", error.message.toString())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0 until menu.size()) {
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