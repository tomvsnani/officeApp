package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CartItemRecommendedAdapter
import com.example.myfirstofficeappecommerce.Adapters.CartItemsSelectedRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import com.shopify.buy3.*
import com.shopify.buy3.CreditCard
import com.shopify.buy3.CreditCardVaultCall.Callback
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import com.shopify.graphql.support.Input
import java.io.IOException
import java.util.concurrent.TimeUnit


class CartFragment(var selectedItemsList: List<CategoriesModelClass>?) : Fragment() {

    var toolbar: Toolbar? = null
    var slecetdItemsRecycler: RecyclerView? = null
    var recommendedItemsRecycler: RecyclerView? = null
    var itemsSelectedAdapter: CartItemsSelectedRecyclerViewAdapter? = null
    var totalAmountTextView: TextView? = null
    var count = 0
    var recommendedAdapter: CartItemRecommendedAdapter? = null
    var proceedTextViewCart: TextView? = null
    var list: MutableList<CategoriesModelClass> = ArrayList()
    var emptycartlayout: ConstraintLayout? = null
    var cartNestedScroll: NestedScrollView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).lockDrawer()

        selectedItemsList = ApplicationClass.selectedItemsList
        var view: View = inflater.inflate(
            R.layout.fragment_cart,
            container,
            false
        )
        (activity as MainActivity).lockDrawer()
        totalAmountTextView = view.findViewById(R.id.totalamounttextviewcart)
        toolbar = view.findViewById(R.id.cartToolbar)
        setHasOptionsMenu(true)
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
            this.list = it as MutableList<CategoriesModelClass>

        }
        if (selectedItemsList!!.isEmpty()) {
            emptycartlayout!!.visibility = View.VISIBLE
            cartNestedScroll!!.visibility = View.GONE
        } else {
            emptycartlayout!!.visibility = View.GONE
            cartNestedScroll!!.visibility = View.VISIBLE
        }
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
            if (count.isNotEmpty()) {
                list[list.indexOf(modelClass)] = modelClass

                (ApplicationClass.selectedItemsList as MutableList)[list.indexOf(modelClass)] =
                    modelClass
            } else {
                list.add(modelClass)
                (ApplicationClass.selectedItemsList as MutableList).add(modelClass)
            }

            itemsSelectedAdapter!!.submitList(list)


        }

        recommendedItemsRecycler!!.adapter = recommendedAdapter
        recommendedAdapter!!.submitList(
            CategoriesDataProvider.getRecommendedData() as MutableList<CategoriesModelClass>
        )

        proceedTextViewCart!!.setOnClickListener {
//            activity!!.supportFragmentManager.beginTransaction()
//                .replace(
//                    R.id.container, OrdersFragment(
//                        ApplicationClass.selectedItemsList!!.map {
//                            it.isOrdered = true
//                            return@map it
//                        }
//                    )
//                )
//                .addToBackStack(null).commit()
            var checkoutLineItemInput: MutableList<CheckoutLineItemInput>? = null
            for (i in ApplicationClass.selectedItemsList!!) {
                checkoutLineItemInput?.add(CheckoutLineItemInput(i.quantityOfItem, ID(i.id)))
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

                        val queryy = query { rootQuery: QueryRootQuery ->
                            rootQuery
                                .node(
                                    ID(checkoutId)
                                ) { nodeQuery: NodeQuery ->
                                    nodeQuery
                                        .onCheckout { checkoutQuery: CheckoutQuery ->
                                            checkoutQuery
                                                .availableShippingRates { availableShippingRatesQuery: AvailableShippingRatesQuery ->
                                                    availableShippingRatesQuery
                                                        .ready()
                                                        .shippingRates { shippingRateQuery: ShippingRateQuery ->
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
                            object : GraphCall.Callback<QueryRoot> {
                                override fun onResponse(response: GraphResponse<QueryRoot>) {
                                    val checkout = response.data()!!.node as Checkout
                                    val shippingRates =
                                        checkout.availableShippingRates.shippingRates
                                }

                                override fun onFailure(error: GraphError) {}
                            },
                            null,
                            RetryHandler.exponentialBackoff(800, TimeUnit.MILLISECONDS, 1.2f)
                                .build()

                        )

//                        var checkout = Checkout(ID(checkoutId))
//                        Log.d("checkout",checkout.webUrl)




                        Log.d("checkoutsuccess", "$checkoutId $checkoutWebUrl")
                    }
                }


                override fun onFailure(error: GraphError) {
                    Log.d("checkouterror", error.message.toString())
                }
            })
        }


        return view
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