package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentCreditcardDetailsBinding
import com.shopify.buy3.*
import com.shopify.buy3.CreditCard
import com.shopify.buy3.Storefront.*
import java.io.IOException


class CreditcardDetailsFragment : Fragment() {
    var binding: FragmentCreditcardDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_creditcard_details, container, false)

        binding = FragmentCreditcardDetailsBinding.bind(v)

        binding!!.addCardButton.setOnClickListener {  addCard() }


        return v
    }

    private fun addCard() {
        if (binding!!.cardnumberEditText.toString().isNotBlank() &&
            binding!!.nameEditText.text.toString().isNotBlank() &&
            binding!!.expiremonthEditText.text.toString().isNotBlank() &&
            binding!!.expireYearEditText.text.toString().isNotBlank() &&
            binding!!.lastnameEditText.text.toString().isNotBlank() &&
            binding!!.cvvEditText.text.toString().isNotBlank()
        ) {
            var query = query { _queryBuilder ->
                _queryBuilder.shop { _queryBuilder ->
                    _queryBuilder.paymentSettings { _queryBuilder ->
                        _queryBuilder.cardVaultUrl()
                    }
                }
            }

            var call = CategoriesDataProvider.graphh!!.queryGraph(query)
            call.enqueue(object : GraphCall.Callback<QueryRoot> {

                override fun onResponse(response: GraphResponse<QueryRoot>) {
                    val creditCard: CreditCard = CreditCard.builder()
                        .firstName(binding!!.nameEditText.text.toString())
                        .lastName(binding!!.lastnameEditText.text.toString())
                        .number(binding!!.cardnumberEditText.text.toString())
                        .expireMonth(binding!!.expiremonthEditText.text.toString())
                        .expireYear(binding!!.expireYearEditText.text.toString())
                        .verificationCode(binding!!.cvvEditText.text.toString())
                        .build()

                    CardClient().vault(
                        creditCard,
                        response.data()!!.shop.paymentSettings.cardVaultUrl
                    )
                        .enqueue(object : CreditCardVaultCall.Callback {
                            override fun onResponse(token: String) {
                                Log.d("token", token)

    //                                val input = CreditCardPaymentInput(
    //                                    amount, idempotencyKey, billingAddress,
    //                                    creditCardVaultToken
    //                                )
    //
    //                                val query = mutation { mutationQuery: MutationQuery ->
    //                                    mutationQuery
    //                                        .checkoutCompleteWithCreditCard(
    //                                            checkoutId, input
    //                                        ) { payloadQuery: CheckoutCompleteWithCreditCardPayloadQuery ->
    //                                            payloadQuery
    //                                                .payment { paymentQuery: PaymentQuery ->
    //                                                    paymentQuery
    //                                                        .ready()
    //                                                        .errorMessage()
    //                                                }
    //                                                .checkout { checkoutQuery: CheckoutQuery ->
    //                                                    checkoutQuery
    //                                                        .ready()
    //                                                }
    //                                                .userErrors { userErrorQuery: UserErrorQuery ->
    //                                                    userErrorQuery
    //                                                        .field()
    //                                                        .message()
    //                                                }
    //                                        }
    //                                }
    //
    //                                client.mutateGraph(query)
    //                                    .enqueue(object : GraphCall.Callback<Mutation> {
    //                                        override fun onResponse(response: GraphResponse<Mutation>) {
    //                                            if (!response.data()!!.checkoutCompleteWithCreditCard.userErrors.isEmpty()) {
    //                                                // handle user friendly errors
    //                                            } else {
    //                                                val checkoutReady =
    //                                                    response.data()!!.checkoutCompleteWithCreditCard.checkout.ready
    //                                                val paymentReady =
    //                                                    response.data()!!.checkoutCompleteWithCreditCard.payment.ready
    //                                            }
    //                                        }
    //
    //                                        override fun onFailure(error: GraphError) {
    //                                            // handle errors
    //                                        }
    //                                    })

                                activity!!.runOnUiThread {
                                    Toast.makeText(
                                        context!!,
                                        "credit card not yet implemented",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            //
                            override fun onFailure(error: IOException) {

                            }
                        })
                }

                override fun onFailure(error: GraphError) {

                }
            })
        }
    }


}