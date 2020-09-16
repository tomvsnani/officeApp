package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CategoriesEachRecyclerAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.shopify.buy3.*
import com.shopify.graphql.support.ID


class CategoryEachViewPagerFragment(var get: CategoriesModelClass?, var callback: () -> Unit) :
    Fragment() {
    var recyclerView: RecyclerView? = null
    var adapterr: CategoriesEachRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_category_each_viewpager,
            container,
            false
        )
        recyclerView = view.findViewById(R.id.categoriesRecyclerview)
        adapterr = CategoriesEachRecyclerAdapter(callback, this)
        (recyclerView as RecyclerView).layoutManager =
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        (recyclerView as RecyclerView).adapter = adapterr
        recyclerView!!.itemAnimator = null

        Log.d("calledat", get!!.itemName)
        //  CategoriesDataProvider.getProductDataBasedOnCollectionId(get!!.id)

        var a = fun(colllectionId: String) {
            Log.d("calledd", colllectionId)


            val query1 = Storefront.query { rootQuery: Storefront.QueryRootQuery ->
                rootQuery.node(ID(colllectionId)) { _queryBuilderr ->
                    _queryBuilderr.onCollection { _queryBuilder ->
                        _queryBuilder
                            .title()
                            .products({ args: Storefront.CollectionQuery.ProductsArguments? ->
                                args!!.first(
                                    10
                                )
                            }, { _queryBuilder ->
                                _queryBuilder.edges { _queryBuilder ->
                                    _queryBuilder.node { _queryBuilder ->
                                        _queryBuilder.title()

                                            .description()

                                            .images({ args: Storefront.ProductQuery.ImagesArguments? ->
                                                args!!.first(
                                                    1
                                                )
                                            }, { _queryBuilder ->
                                                _queryBuilder.edges { _queryBuilder ->
                                                    _queryBuilder.node { _queryBuilder ->
                                                        _queryBuilder.src().id()
                                                    }
                                                }
                                            })


                                            .variants({ args: Storefront.ProductQuery.VariantsArguments? ->
                                                args!!.first(
                                                    1
                                                )
                                            },
                                                { _queryBuilder ->
                                                    _queryBuilder.edges { _queryBuilder ->
                                                        _queryBuilder.node { _queryBuilder ->
                                                            _queryBuilder.price()
                                                                .selectedOptions { _queryBuilder ->
                                                                    _queryBuilder.name().value()
                                                                }


                                                        }
                                                    }
                                                })


                                    }
                                }
                            })


                    }
                }
            }


            val calldata: QueryGraphCall = CategoriesDataProvider.graphh!!.queryGraph(query1)

            calldata.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
                override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                    var productList: MutableList<CategoriesModelClass> = ArrayList()
                    var storefront: Storefront.Collection =
                        response.data()!!.node as Storefront.Collection
                    for (productEdge in storefront.products.edges) {
                        var productImageSrcList: MutableList<ModelClass> = ArrayList()

                        // adding aa images to product imagelist
                        for (imageedge in productEdge.node.images.edges)
                            productImageSrcList.add(ModelClass(imageUrl = imageedge.node.src))

                        var variantList: MutableList<VariantsModelClass> = ArrayList()

                        //adding all variant data to product variant list
                        for (variantEdge in productEdge.node.variants.edges) {
                            var sizeIndex = 2;
                            if (variantEdge.node.selectedOptions.size > 1 && variantEdge.node.selectedOptions[1].name == "Size")
                                sizeIndex = 1
                            variantList.add(
                                VariantsModelClass(
                                    variantEdge.node.id.toString(),
                                    productEdge.node.id.toString(),
                                    if (variantEdge.node.selectedOptions.size > 0) variantEdge.node.selectedOptions[0].value else null,
                                    if (variantEdge.node.selectedOptions.size > 1 && sizeIndex < variantEdge.node.selectedOptions.size) variantEdge.node.selectedOptions[sizeIndex].value else null,
                                    variantEdge.node.price.toFloat()


                                )
                            )
                        }

                        var productmodelclass = CategoriesModelClass(
                            id = productEdge.node.id.toString(),
                            itemName = productEdge.node.title,
                            itemDescriptionText = productEdge.node.descriptionHtml,
                            imageSrc = productImageSrcList,
                            realTimeMrp = productEdge.node.variants.edges[0].node.price.precision(),
                            variantsList = variantList,
                            groupId = storefront.id.toString()
                        )

                        productList.add(productmodelclass)

                    }
                    // CategoriesDataProvider.mutableproductList!!.postValue(productList)
                    // CategoriesDataProvider.mutableproductList!!.removeObserver(this)
                   activity!!.runOnUiThread {

//                        var list = productList.filter { categoriesModelClass ->
//                            Log.d("responsedataa","${categoriesModelClass.id} ${categoriesModelClass.groupId}")
//                            ApplicationClass.selectedItemsList?.filter {
//                                if (it.id == categoriesModelClass.id && it.groupId==categoriesModelClass.groupId) {
//                                    categoriesModelClass.quantityOfItem = it.quantityOfItem
//                                    categoriesModelClass.itemQueueNumber = it.itemQueueNumber
//                                    return@filter true
//                                }
//                                return@filter true
//                            }
//                            return@filter true
//                        }

                        (adapterr as CategoriesEachRecyclerAdapter)
                            .submitList(
                                productList)
                   }
                    Log.d("responsedata","${ ApplicationClass.selectedItemsList?.getOrNull(0)?.id}  ${ ApplicationClass.selectedItemsList?.getOrNull(0)?.groupId}")

                }


                override fun onFailure(error: GraphError) {
                    Log.e("graphvalueerror", error.toString())
                }
            })

        }

//        CategoriesDataProvider.mutableproductList!!.observe(viewLifecycleOwner,
//
//            object : Observer<List<CategoriesModelClass>> {
//                override fun onChanged(t: List<CategoriesModelClass>?) {
//
//                }
//            }
//        )

        a(get!!.id)
        return view
    }


}