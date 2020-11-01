package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
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
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Adapters.CartItemRecommendedAdapter
import com.example.myfirstofficeappecommerce.Adapters.CartItemsSelectedRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.databinding.FragmentCartBinding


class CartFragment(var selectedItemsList: List<VariantsModelClass>?) : Fragment() {

    private var loginfrag: loginFragment?=null
    var toolbar: Toolbar? = null
    private var slecetdItemsRecycler: RecyclerView? = null
  //  private var recommendedItemsRecycler: RecyclerView? = null
    private var itemsSelectedAdapter: CartItemsSelectedRecyclerViewAdapter? = null
    private var recommendedAdapter: CartItemRecommendedAdapter? = null
    private var proceedTextViewCart: TextView? = null
    var list: MutableList<VariantsModelClass> = ArrayList()
    var emptycartlayout: ConstraintLayout? = null
    var cartNestedScroll: ConstraintLayout? = null
    var progressbar: ProgressBar? = null
    var discountApplyButton: Button? = null
    var binding: FragmentCartBinding? = null
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

        binding = FragmentCartBinding.bind(view)

        setUpToolBar(view)

        initializeViews(view)

        setUpItemsSelectedRecyclerView()

        list = selectedItemsList!!.toMutableList()

        setUpRecommendedItemsRecyclerView()


        setUpOnClickListener()


        return view
    }


    fun setTotalPriceAfterDiscount(price: String) {
        binding!!.ptotalAmountTextViewCart.text = "  ${activity!!.getString(R.string.Rs)} ${price}"
    }

    private fun setUpOnClickListener() {
        proceedTextViewCart!!.setOnClickListener {

            var token = activity!!.getPreferences(Activity.MODE_PRIVATE).getString("token", "")
            if (token == "") {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, loginFragment(Constants.GUEST_SIGN_IN, fragment = this))
                    ?.addToBackStack("ok")?.commit()



            } else {
                (activity as MainActivity).createCheckout(Constants.NORMAL_SIGN_IN)
            }


        }
//        discountApplyButton!!.setOnClickListener {
//            binding!!.applycoupontextview.clearFocus()
//            var view = binding!!.applycoupontextview
//            var inputManager: InputMethodManager =
//                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
//
//            MainActivity.applyCoupon = true
//            if (binding!!.applycoupontextview.text.toString().isNotBlank())
//                (activity as MainActivity).DISCOUNT = binding!!.applycoupontextview.text.toString()
//            else
//                Toast.makeText(context!!, "Please enter a coupon code", Toast.LENGTH_SHORT).show()
//            (activity as MainActivity).createCheckout(Constants.GUEST_SIGN_IN)
//
//
//        }

    }

    private fun setUpRecommendedItemsRecyclerView() {
//        recommendedItemsRecycler!!.layoutManager =
//            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)



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
        //recommendedItemsRecycler!!.adapter = recommendedAdapter
        recommendedAdapter!!.submitList(
            CategoriesDataProvider.getRecommendedData() as MutableList<VariantsModelClass>
        )
    }

    private fun setUpItemsSelectedRecyclerView() {
        itemsSelectedAdapter = CartItemsSelectedRecyclerViewAdapter(this) {
            this.list.clear()
            this.list = it as MutableList<VariantsModelClass>

        }


        slecetdItemsRecycler!!.itemAnimator = null
        slecetdItemsRecycler!!.adapter = itemsSelectedAdapter
        slecetdItemsRecycler!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        itemsSelectedAdapter!!.submitList(ApplicationClass.selectedVariantList!!.filter { it.quantityOfItem > 0 } as MutableList<VariantsModelClass>)
    }

    private fun initializeViews(view: View) {
        progressbar = view.findViewById<ProgressBar>(R.id.cardfragmentprogressbar)

        emptycartlayout = view.findViewById(R.id.emptycartlayout)
        slecetdItemsRecycler = view.findViewById(R.id.cartSelecetedRecyclerview)
        //recommendedItemsRecycler = view.findViewById(R.id.cartRecyclerviewRecommondedItems)
        proceedTextViewCart = view.findViewById(R.id.proceedTextViewCart)
        cartNestedScroll = view.findViewById(R.id.constraint)
        //discountApplyButton = view.findViewById(R.id.discountapplybutton)


    }

    private fun setUpToolBar(view: View) {
        (activity as MainActivity).lockDrawer()
        setHasOptionsMenu(true)
        toolbar = view.findViewById(R.id.cartToolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
//            if(loginfrag!=null)
//                loginfrag!!.dismiss()
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