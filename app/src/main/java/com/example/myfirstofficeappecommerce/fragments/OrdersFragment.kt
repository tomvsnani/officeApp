package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.OrdersAdaptes
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.buy3.Storefront.CustomerQuery.OrdersArguments
import com.shopify.graphql.support.ID


class OrdersFragment(var orderList: List<VariantsModelClass>) : Fragment() {

    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var ordersAdapters: OrdersAdaptes? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = inflater.inflate(R.layout.fragment_orders, container, false)
        (activity as MainActivity).lockDrawer()

        toolbar = view?.findViewById(R.id.ordersToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recyclerView = view?.findViewById(R.id.ordersRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        ordersAdapters = OrdersAdaptes(context!!)
        recyclerView?.adapter = ordersAdapters
        ordersAdapters!!.submitList(orderList)

        return view
    }

    override fun onStart() {
        var token = activity!!.getPreferences(Activity.MODE_PRIVATE).getString("token", "")
        if (token == "") {
            Toast.makeText(context, "Please Login", Toast.LENGTH_SHORT).show()
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment())
                .commit()
        } else {
            getOrders(token)
        }
        super.onStart()
    }

    private fun getOrders(token: String?) {
        val query = query { root: QueryRootQuery ->
            root
                .customer(
                    token
                ) { customer: CustomerQuery ->
                    customer
                        .orders(
                            { arg: OrdersArguments -> arg.first(10) }
                        ) { connection: OrderConnectionQuery ->
                            connection
                                .edges { edge: OrderEdgeQuery ->
                                    edge
                                        .node { node: OrderQuery ->
                                            node
                                                .orderNumber()

                                                .totalPrice()
                                                .shippingAddress { _queryBuilder ->
                                                    _queryBuilder.address1().city()
                                                        .province().zip()
                                                }
                                                .lineItems({ args: OrderQuery.LineItemsArguments? ->
                                                    args!!.first(
                                                        10
                                                    )
                                                }, { _queryBuilder ->
                                                    _queryBuilder.edges { _queryBuilder ->
                                                        _queryBuilder.node {

                                                                _queryBuilder ->
                                                            _queryBuilder.title().variant {

                                                                    _queryBuilder ->
                                                                _queryBuilder.title().price()
                                                                    .image { _queryBuilder -> _queryBuilder.src() }
                                                            }.quantity()
                                                        }
                                                    }
                                                })


                                        }
                                }
                        }
                }
        }
        var call =
            CategoriesDataProvider.graphh!!.queryGraph(query)
        call.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
            var ordersList = ArrayList<VariantsModelClass>()
            override fun onResponse(response: GraphResponse<QueryRoot>) {
                var orders = response.data()!!.customer.orders.edges
                for (i in orders) {

                    for (j in i.node.lineItems.edges) {

                        var variantsModel = VariantsModelClass(
                            price = j.node.variant.price.toFloat(),
                            id = j.node.variant.id.toString(),
                            orderId = i.node.orderNumber.toString(),
                            name = j.node.title,
                            size = j.node.variant.title.split("/")[1],
                            color = j.node.variant.title.split("/")[0],
                            quantityOfItem = j.node.quantity,
                            location = i.node.shippingAddress.address1+ " " + i.node.shippingAddress.city+" "

                                    +i.node.shippingAddress.province,
                            imgSrc = j.node.variant.image.src
                        )
                        ordersList.add(variantsModel)
                    }
                    Log.d(
                        "address",
                        i.node.shippingAddress.address2 + " " + i.node.shippingAddress.city+" "

                    +i.node.shippingAddress.province
                    )
                }
                ordersAdapters!!.submitList(ordersList)


            }

            override fun onFailure(error: GraphError) {

            }
        })
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
