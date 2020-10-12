package layout

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CheckoutOverViewItemsAdapter
import com.example.myfirstofficeappecommerce.Adapters.ShippingRatesAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.ChangeShippingRatesLayoutBinding
import com.example.myfirstofficeappecommerce.databinding.CheckoutOverviewLayoutBinding
import com.example.myfirstofficeappecommerce.fragments.WebViewFragment
import com.shopify.buy3.Storefront

class CheckoutOverViewFragment(
    var webUrl: String,
    var shippingPrice: Float,
    var taxPrice: Float,
    var modelClass: ModelClass,
    var list: MutableList<VariantsModelClass>,
    var shippingList: List<ModelClass>,
    var checkoutId: String,
    var shippingRatesAdapter:ShippingRatesAdapter?=null

) : Fragment() {
    var adapter: CheckoutOverViewItemsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.checkout_overview_layout, container, false)
        val binding = CheckoutOverviewLayoutBinding.bind(v)
        if (ApplicationClass.weburl!=null && ApplicationClass.weburl!!.isNotBlank() && ApplicationClass!!.weburl!!.isNotEmpty())
            binding.otherpaymenttextview.setOnClickListener {
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
        else
        {
            shippingRatesAdapter!!.getNewWebAddressBasedOnShippingProvider(shippingList[0])
        }


        binding.creditcardpaymenttextview.setOnClickListener {

            val query = Storefront.mutation { mutationQuery: Storefront.MutationQuery ->
                mutationQuery

                // .checkoutCompleteWithCreditCard(checkoutId,)
            }
        }

        var a = 0f;
        for (i in ApplicationClass.selectedVariantList!!)
            a += i.price!!


        binding.shippingAddresseditButton.setOnClickListener {
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
            shippingRatesAdapter!!.submitList(shippingList as MutableList<ModelClass>)

            alertDialog.show()
        }


        binding.checkoutOverViewPriceTextView.text =
            a.toString()
        binding.checkoutOverViewShippingCostTextView.text =
            (shippingPrice + taxPrice).toString()
        binding.checkoutOverViewTotalPriceTextView.text =
            (a.toFloat() + shippingPrice + taxPrice).toString()

        binding.chooseAddressnameTextView.text =
            modelClass.title + " " + modelClass.subTitle
        binding.chooseAddressPhoneNumber.text =
            modelClass.phoneNumber
        binding.chooseAddressaddressTextView.text =
            modelClass.hnum + " " + modelClass.city + " " + modelClass.state + " " + " " + modelClass.pinCode + " " + modelClass.country

        binding.checkoutoverviewrecyclerview.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = CheckoutOverViewItemsAdapter(context!!)

        binding.checkoutoverviewrecyclerview.adapter = adapter
        adapter!!.submitList(list)




        binding.changeaddressbutton.setOnClickListener {

            activity!!.onBackPressed()

        }
        return v
    }
}