package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
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
import kotlinx.android.synthetic.main.fragment_cart.*
import java.util.concurrent.TimeUnit


class CartFragment(var selectedItemsList: List<VariantsModelClass>?) : Fragment() {

    var toolbar: Toolbar? = null
    private var slecetdItemsRecycler: RecyclerView? = null
    private var recommendedItemsRecycler: RecyclerView? = null
    private var itemsSelectedAdapter: CartItemsSelectedRecyclerViewAdapter? = null
    var totalAmountTextView: TextView? = null
    private var recommendedAdapter: CartItemRecommendedAdapter? = null
    private var proceedTextViewCart: TextView? = null
    var list: MutableList<VariantsModelClass> = ArrayList()
    private var emptycartlayout: ConstraintLayout? = null
    private var cartNestedScroll: NestedScrollView? = null
    var progressbar: ProgressBar? = null
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
        setUpToolBar(view)

        initializeViews(view)

        setUpItemsSelectedRecyclerView()

        list = selectedItemsList!!.toMutableList()

        setUpRecommendedItemsRecyclerView()


        setUpOnClickListener()


        return view
    }

    private fun setUpOnClickListener() {
        proceedTextViewCart!!.setOnClickListener {

            var token = activity!!.getPreferences(Activity.MODE_PRIVATE).getString("token", "")
            if (token == "") {
                var alert = AlertDialog.Builder(context!!).create()

                alert.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "Signin"
                ) { p0, p1 -> createCheckout(Constants.NORMAL_SIGN_IN) }


                alert.setButton(
                    AlertDialog.BUTTON_POSITIVE, "Continue as Guest"
                ) { p0, p1 -> createCheckout(Constants.GUEST_SIGN_IN) }


                alert.setCancelable(false)

                alert.show()
            } else
                createCheckout(Constants.NORMAL_SIGN_IN)


        }
    }

    private fun setUpRecommendedItemsRecyclerView() {
        recommendedItemsRecycler!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)



        recommendedAdapter = CartItemRecommendedAdapter(this) { modelClass ->

            modelClass.quantityOfItem++


            var count =
                list.filter { it.id == modelClass.id && it.parentProductId == modelClass.parentProductId }
            if (count.isNotEmpty()) {
                list.find { it.id == modelClass.id && it.parentProductId == modelClass.parentProductId }!!.quantityOfItem =
                    modelClass.quantityOfItem

                (ApplicationClass.selectedVariantList as MutableList).find { it.id == modelClass.id && it.parentProductId == modelClass.parentProductId }!!.quantityOfItem =
                    modelClass.quantityOfItem


            } else {
                Log.d("recommendedqu", modelClass.quantityOfItem.toString())
                list.add(modelClass)
                (ApplicationClass.selectedVariantList as MutableList).add(modelClass)

            }

            itemsSelectedAdapter!!.submitList(list.filter { it.quantityOfItem > 0 } as MutableList<VariantsModelClass>)


        }
        recommendedItemsRecycler!!.adapter = recommendedAdapter
        recommendedAdapter!!.submitList(
            CategoriesDataProvider.getRecommendedData() as MutableList<VariantsModelClass>
        )
    }

    private fun setUpItemsSelectedRecyclerView() {
        itemsSelectedAdapter = CartItemsSelectedRecyclerViewAdapter(this) {
            this.list.clear()
            this.list = it as MutableList<VariantsModelClass>

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

        itemsSelectedAdapter!!.submitList(ApplicationClass.selectedVariantList!!.filter { it.quantityOfItem > 0 } as MutableList<VariantsModelClass>)
    }

    private fun initializeViews(view: View) {
        progressbar = view.findViewById<ProgressBar>(R.id.cardfragmentprogressbar)
        totalAmountTextView = view.findViewById(R.id.totalamounttextviewcart)
        emptycartlayout = view.findViewById(R.id.emptycartlayout)
        slecetdItemsRecycler = view.findViewById(R.id.cartSelecetedRecyclerview)
        recommendedItemsRecycler = view.findViewById(R.id.cartRecyclerviewRecommondedItems)
        proceedTextViewCart = view.findViewById(R.id.proceedTextViewCart)
        cartNestedScroll = view.findViewById(R.id.include2)
    }

    private fun setUpToolBar(view: View) {
        (activity as MainActivity).lockDrawer()
        setHasOptionsMenu(true)
        toolbar = view.findViewById(R.id.cartToolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)
    }

    private fun createCheckout(signinType: String) {
        ApplicationClass.signInType = signinType
        progressbar!!.visibility = View.VISIBLE
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
                                .totalTax()
                                .totalPrice()
                                .subtotalPrice()
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

                    if (signinType == Constants.NORMAL_SIGN_IN) {
                        if (activity?.getPreferences(Activity.MODE_PRIVATE)
                                ?.getString("token", "") != ""
                        )
                            associateWithUserQuery(checkoutId)
                        else
                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.container,

                                    ProfileFragment(
                                        Constants.NORMAL_SIGN_IN,
                                        fragment = this@CartFragment
                                    )
                                ).addToBackStack(null).commit()
                    } else {
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.container,
                                CheckOutMainWrapperFragment(
                                    checkoutId,
                                    response.data()!!.checkoutCreate.checkout.totalTax.precision()
                                        .toFloat()
                                )
                            ).addToBackStack(null).commit()

                    }
                }
            }


            override fun onFailure(error: GraphError) {

            }
        })
    }

    private fun associateWithUserQuery(checkoutId: String) {
        var associateCustomerQuery = mutation { mutationQuery: MutationQuery ->
            mutationQuery

                .checkoutCustomerAssociate(
                    ID(checkoutId),
                    activity!!.getPreferences(Activity.MODE_PRIVATE)
                        .getString("token", "")
                ) {

                        _queryBuilder ->
                    _queryBuilder.checkout { _queryBuilder ->
                        _queryBuilder.totalTax()

                    }
                }
        }

        CategoriesDataProvider.graphh!!.mutateGraph(associateCustomerQuery)
            .enqueue(object :
                GraphCall.Callback<Mutation> {
                override fun onResponse(response: GraphResponse<Mutation>) {
                    val checkoutIdd =
                        response.data()!!.checkoutCustomerAssociate.checkout.id.toString()
                    val checkoutWebUrl =
                        response.data()!!.checkoutCustomerAssociate.checkout.webUrl
                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            CheckOutMainWrapperFragment(
                                checkoutIdd,
                                response.data()!!.checkoutCustomerAssociate.checkout.totalTax.precision()
                                    .toFloat()
                            )
                        )
                        .addToBackStack(null).commit()

                }

                override fun onFailure(error: GraphError) {

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