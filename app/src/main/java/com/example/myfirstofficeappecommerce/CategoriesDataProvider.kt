package com.example.myfirstofficeappecommerce

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment
import com.example.myfirstofficeappecommerce.fragments.MainFragment
import com.shopify.buy3.*

object CategoriesDataProvider {
    var graphh: GraphClient? = null
    var mutablehashmap: MutableLiveData<LinkedHashMap<String, List<CategoriesModelClass>>> =
        MutableLiveData()
    var mutableCollectionList: MutableLiveData<MutableList<CategoriesModelClass>> =
        MutableLiveData()
    var hashMap: LinkedHashMap<String, List<CategoriesModelClass>> = LinkedHashMap()

    fun getMapDataForCategories(): LinkedHashMap<String, List<CategoriesModelClass>>? {
        var map: LinkedHashMap<String, List<CategoriesModelClass>> = LinkedHashMap()

        map.put(
            "Phones", listOf(
                CategoriesModelClass(
                    "Phones",
                    "Samsung phone",
                    "Brezzycloud Multi-function Travel Cosmetic Make Up Bag With Small Mirror Adjustable Dividers for Cosmetics Makeup Brushes (Multi color)\n" +
                            "\n" +
                            "Waterproof and easy to clean.\n" +
                            "easy to carry whether in home or going out.\n" +
                            "Built-in mirror is very convenient for you to makeup at any time\n" +
                            "Inner slip pockets are great for holding small necessaries\n" +
                            "deal for home,travel,business trip or whenever you need to carry it with you\n" +
                            "This cosmetic case is perfect for keeping all of your favorite makeup and toiletries organized and fashionable.",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "dt",
                    groupId = 1,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Phones",
                    "Samsung phone",
                    "too hot",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "df",
                    groupId = 1,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Phones",
                    "Samsung",
                    "too hot",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "de",
                    groupId = 1,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Phones",
                    "Samsung",
                    "too hot",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "dd",
                    groupId = 1,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Phones",
                    "Samsung",
                    "too hot",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "dc",
                    groupId = 1,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Phones",
                    "Samsung",
                    "too hot",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "db",
                    groupId = 1,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Phones",
                    "Samsung",
                    "too hot",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "da",
                    groupId = 1,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                )

            )
        )

        map?.put(
            "Laptops", listOf(
                CategoriesModelClass(
                    "Laptops",
                    "Samsung laptop",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "cg",
                    groupId = 2,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Laptops",
                    "Samsung laptop",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "cf",
                    groupId = 2,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Laptops",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "ce",
                    groupId = 2,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Laptops",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220", 500,
                    "2",
                    "1",
                    id = "cd",
                    groupId = 2,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Laptops",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "cc",
                    groupId = 2,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Laptops",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "cb",
                    groupId = 2,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Laptops",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "ca",
                    groupId = 2,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                )

            )
        )

        map?.put(
            "Soap", listOf(
                CategoriesModelClass(
                    "Soap",
                    "Santoor",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "bg",
                    groupId = 3,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Soap",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "bf",
                    groupId = 3,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Soap",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "be",
                    groupId = 3,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Soap",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "bd",
                    groupId = 3,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Soap",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "bc",
                    groupId = 3,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Soap",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "bb",
                    groupId = 3,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                ),
                CategoriesModelClass(
                    "Soap",
                    "Samsung",
                    "too hot Laptop",
                    "Gross Wt. 840gms",
                    "Net Wt.450gms",
                    "220",
                    500,
                    "2",
                    "1",
                    id = "ba",
                    groupId = 3,
                    dateOrdered = "07-09-2020",
                    location = "Bodupppal Hyderabad"
                )

            )
        )
        return map
    }

    fun getListDataForHorizontalScroll(): List<ModelClass> {
        return listOf(
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Laptop",
                "ok",
                "null",
                "null",
                id = 1
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Phone",
                "ok",
                "null",
                "null",
                id = 1
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Light",
                "ok",
                "null",
                "null",
                id = 1
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Toys",
                "ok",
                "null",
                "null",
                id = 1
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Mugs",
                "ok",
                "null",
                "null",
                id = 1
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Phone",
                "ok",
                "null",
                "null",
                id = 1
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Light",
                "ok",
                "null",
                "null",
                id = 1
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Toys",
                "ok",
                "null",
                "null",
                id = 1
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Mugs",
                "ok",
                "null",
                "null",
                id = 1
            )

        )
    }

    fun getSearhItemsData(): List<CategoriesModelClass> {
        return listOf(
            CategoriesModelClass(
                "Soap", "Santoor", "too hot Laptop",
                "Gross Wt. 840gms", "Net Wt.450gms", "220", 500, "2", "1", id = "ab"
            ),
            CategoriesModelClass(
                "Soap",
                "Samsung",
                "too hot Laptop",
                "Gross Wt. 840gms",
                "Net Wt.450gms",
                "220",
                500,
                "2",
                "1",
                quantityOfItem = 28,
                id = "ac"
            ),
            CategoriesModelClass(
                "Soap",
                "Samsung",
                "too hot Laptop",
                "Gross Wt. 840gms",
                "Net Wt.450gms",
                "220",
                500,
                "2",
                "1",
                quantityOfItem = 32,
                id = "ad"
            ),
            CategoriesModelClass(
                "Soap",
                "Samsung",
                "too hot Laptop",
                "Gross Wt. 840gms",
                "Net Wt.450gms",
                "220",
                500,
                "2",
                "1",
                quantityOfItem = 25,
                id = "ae"
            ),
            CategoriesModelClass(
                "Soap",
                "Samsung",
                "too hot Laptop",
                "Gross Wt. 840gms",
                "Net Wt.450gms",
                "220",
                500,
                "2",
                "1",
                quantityOfItem = 8,
                id = "af"
            ),
            CategoriesModelClass(
                "Soap",
                "Samsung",
                "too hot Laptop",
                "Gross Wt. 840gms",
                "Net Wt.450gms",
                "220",
                500,
                "2",
                "1",
                quantityOfItem = 5,
                id = "ag"
            ),
            CategoriesModelClass(
                "Soap",
                "Samsung",
                "too hot Laptop",
                "Gross Wt. 840gms",
                "Net Wt.450gms",
                "220",
                500,
                "2",
                "1",
                quantityOfItem = 1,
                id = "ah"
            )

        )
    }

    fun getRecommendedData(): List<CategoriesModelClass> {
        return listOf(
            CategoriesModelClass(
                "Phones", "Samsung phone", "too hot",
                "Gross Wt. 840gms", "Net Wt.450gms", "220", 500, "2", "1", id = "a", groupId = 8
            ),
            CategoriesModelClass(
                "Phones", "Samsung phone", "too hot",
                "Gross Wt. 840gms", "Net Wt.450gms", "220", 500, "2", "1", id = "b", groupId = 9
            ),
            CategoriesModelClass(
                "Phones", "Samsung", "too hot",
                "Gross Wt. 840gms", "Net Wt.450gms", "220", 500, "2", "1", id = "c", groupId = 10
            ),
            CategoriesModelClass(
                "Phones", "Samsung", "too hot",
                "Gross Wt. 840gms", "Net Wt.450gms", "220", 50, "2", "1", id = "d", groupId = 11
            ),
            CategoriesModelClass(
                "Phones", "Samsung", "too hot",
                "Gross Wt. 840gms", "Net Wt.450gms", "220", 500, "2", "1", id = "e", groupId = 12
            ),
            CategoriesModelClass(
                "Phones", "Samsung", "too hot",
                "Gross Wt. 840gms", "Net Wt.450gms", "220", 500, "2", "1", id = "f", groupId = 13
            ),
            CategoriesModelClass(
                "Phones", "Samsung", "too hot",
                "Gross Wt. 840gms", "Net Wt.450gms", "220", 500, "2", "1", id = "g", groupId = 14
            )

        )
    }


    fun getRemoteData(context: Context) {

        var collectionList: MutableList<CategoriesModelClass> = ArrayList()


        Log.d("remoteinre", "before")
        graphh = GraphClient.builder(context)
            .accessToken(context.getString(R.string.storefront_api_key))
            .shopDomain(context.getString(R.string.shopify_domain))
            .build()


        val query = Storefront.query { rootQuery: Storefront.QueryRootQuery ->
            rootQuery.shop { shopQuery: Storefront.ShopQuery ->
                shopQuery.collections({ args: Storefront.ShopQuery.CollectionsArguments? ->
                    args!!.first(
                        100
                    )
                },
                    { _queryBuilder ->
                        _queryBuilder.edges { _queryBuilder: Storefront.CollectionEdgeQuery? ->
                            _queryBuilder!!.node { _queryBuilder: Storefront.CollectionQuery? ->
                                _queryBuilder!!.title()
                                _queryBuilder.image { _queryBuilder -> _queryBuilder.src() }
                                _queryBuilder.products({ args: Storefront.CollectionQuery.ProductsArguments? ->
                                    args!!.first(
                                        50
                                    )
                                }, { _queryBuilder ->
                                    _queryBuilder.edges { _queryBuilder ->
                                        _queryBuilder.node { _queryBuilder ->
                                            _queryBuilder.title()

                                                .description()

                                                .images( {args: Storefront.ProductQuery.ImagesArguments? ->args!!.first(100)  },{ _queryBuilder ->
                                                    _queryBuilder.edges { _queryBuilder ->_queryBuilder.node { _queryBuilder ->_queryBuilder.src().id().altText()  }  }
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


                                                                    .image { _queryBuilder: Storefront.ImageQuery? -> _queryBuilder!!.src()
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
                )
            }
        }


        val call: QueryGraphCall = graphh!!.queryGraph(query)



        call.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
            override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {

                for (collectionEdge in response.data()!!.shop.collections.edges) {
                    var collectionModelClass = CategoriesModelClass(
                        itemName = collectionEdge.node.title,
                        id = collectionEdge.node.id.toString(),
                        imageSrc = collectionEdge.node.image.src

                    )
                    var productList: MutableList<CategoriesModelClass> = ArrayList()
                    collectionList.add(collectionModelClass)
                    for (productEdge in collectionEdge.node.products.edges) {
                        var productmodelclass = CategoriesModelClass(
                            id = productEdge.node.id.toString(),
                            itemName = productEdge.node.title,
                            itemDescriptionText = productEdge.node.descriptionHtml,
                            imageSrc = productEdge.node.variants.edges[0].node.image.src,
                            realTimeMrp = productEdge.node.variants.edges[0].node.price.precision()


                        )
                        productList.add(productmodelclass)

                    }

                    hashMap[collectionEdge.node.title.toString()] = productList
                    mutablehashmap.postValue(hashMap as LinkedHashMap<String, List<CategoriesModelClass>>?)

                }


                mutableCollectionList.postValue(collectionList)

            }


            override fun onFailure(error: GraphError) {
                Log.e("graphvalueerror", error.toString())
            }
        })

    }
}