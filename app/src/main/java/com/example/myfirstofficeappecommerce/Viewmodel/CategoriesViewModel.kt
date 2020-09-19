package com.example.myfirstofficeappecommerce.Viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.shopify.buy3.*
import com.shopify.graphql.support.ID

class CategoriesViewModel(var id: String) : ViewModel() {
    var mutableLiveData: MutableLiveData<MutableList<CategoriesModelClass>>? = MutableLiveData()
    var productList: MutableList<CategoriesModelClass> = ArrayList()

    fun getData() {


        val query1 = Storefront.query { rootQuery: Storefront.QueryRootQuery ->
            rootQuery.node(ID(id)) { _queryBuilderr ->
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
                                                100
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
        var calldata: QueryGraphCall? = null
        if (productList.isEmpty()) {

            calldata = CategoriesDataProvider.graphh!!.queryGraph(query1)

            calldata!!.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
                override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                    Log.d("iscalled", "hey")

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
                                    variantEdge.node.price.toFloat(),
                                    name = productEdge.node.title


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

                    mutableLiveData!!.postValue(productList)

                }

                override fun onFailure(error: GraphError) {
                    Log.e("graphvalueerror", error.toString())
                }
            })

        } else {
            Log.d("indata", mutableLiveData.toString())

            mutableLiveData!!.postValue(productList)
        }

    }

    inner class PagingSource(
        var viewModel: CategoriesViewModel,
        var isFirstRequest: String? = null
    ) : androidx.paging.PagingSource<String, CategoriesModelClass>() {
        override suspend fun load(params: LoadParams<String>): LoadResult<String, CategoriesModelClass> {
            var prev: String = params.key ?: isFirstRequest!!
            var next: String = params.key ?: isFirstRequest!!
            viewModel.getData()
            var list: MutableList<CategoriesModelClass>? = ArrayList()
            viewModel.mutableLiveData!!.observeForever {
                list!!.addAll(it)
            }
            return LoadResult.Page(list!!, prev, next)
        }


    }

}