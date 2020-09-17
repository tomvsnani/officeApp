package com.example.myfirstofficeappecommerce

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.shopify.buy3.*
import com.shopify.graphql.support.ID

object CategoriesDataProvider {
    var graphh: GraphClient? = null
    var mutablehashmap: MutableLiveData<LinkedHashMap<String, List<CategoriesModelClass>>> =
        MutableLiveData()
    var mutableCollectionList: MutableLiveData<MutableList<CategoriesModelClass>> =
        MutableLiveData()
    var hashMap: LinkedHashMap<String, List<CategoriesModelClass>> = LinkedHashMap()

    var mutableproductList: MutableLiveData<List<CategoriesModelClass>>? = MutableLiveData()

    //    fun getMapDataForCategories(): LinkedHashMap<String, List<CategoriesModelClass>>? {
//        var map: LinkedHashMap<String, List<CategoriesModelClass>> = LinkedHashMap()
//
//        map.put(
//            "Phones", listOf(
//                CategoriesModelClass(
//                    "Phones",
//                    "Samsung phone",
//                    "Brezzycloud Multi-function Travel Cosmetic Make Up Bag With Small Mirror Adjustable Dividers for Cosmetics Makeup Brushes (Multi color)\n" +
//                            "\n" +
//                            "Waterproof and easy to clean.\n" +
//                            "easy to carry whether in home or going out.\n" +
//                            "Built-in mirror is very convenient for you to makeup at any time\n" +
//                            "Inner slip pockets are great for holding small necessaries\n" +
//                            "deal for home,travel,business trip or whenever you need to carry it with you\n" +
//                            "This cosmetic case is perfect for keeping all of your favorite makeup and toiletries organized and fashionable.",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "dt",
//                    groupId = "a",
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Phones",
//                    "Samsung phone",
//                    "too hot",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "df",
//                    groupId = "b",
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Phones",
//                    "Samsung",
//                    "too hot",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "de",
//                    groupId = "c",
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Phones",
//                    "Samsung",
//                    "too hot",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "dd",
//                    groupId = 1.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Phones",
//                    "Samsung",
//                    "too hot",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "dc",
//                    groupId = 1.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Phones",
//                    "Samsung",
//                    "too hot",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "db",
//                    groupId = 1.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Phones",
//                    "Samsung",
//                    "too hot",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "da",
//                    groupId = 1.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                )
//
//            )
//        )
//
//        map?.put(
//            "Laptops", listOf(
//                CategoriesModelClass(
//                    "Laptops",
//                    "Samsung laptop",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "cg",
//                    groupId = 2.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Laptops",
//                    "Samsung laptop",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "cf",
//                    groupId = 2.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Laptops",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "ce",
//                    groupId = 2.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Laptops",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220", 500,
//                    "2",
//                    "1",
//                    id = "cd",
//                    groupId = 2.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Laptops",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "cc",
//                    groupId = 2.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Laptops",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "cb",
//                    groupId = 2.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Laptops",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "ca",
//                    groupId = 2.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                )
//
//            )
//        )
//
//        map?.put(
//            "Soap", listOf(
//                CategoriesModelClass(
//                    "Soap",
//                    "Santoor",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "bg",
//                    groupId = 3.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Soap",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "bf",
//                    groupId = 3.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Soap",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "be",
//                    groupId = 3.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Soap",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "bd",
//                    groupId = 3.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Soap",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "bc",
//                    groupId = 3.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Soap",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "bb",
//                    groupId = 3.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                ),
//                CategoriesModelClass(
//                    "Soap",
//                    "Samsung",
//                    "too hot Laptop",
//                    "Gross Wt. 840gms",
//                    "Net Wt.450gms",
//                    "220",
//                    500,
//                    "2",
//                    "1",
//                    id = "ba",
//                    groupId = 3.toString(),
//                    dateOrdered = "07-09-2020",
//                    location = "Bodupppal Hyderabad"
//                )
//
//            )
//        )
//        return map
//    }
//
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

//    fun getSearhItemsData(): List<CategoriesModelClass> {
//        return listOf(
//            CategoriesModelClass(
//                "Soap", "Santoor", "too hot Laptop",
//                "Gross Wt. 840gms", "Net Wt.450gms", "220", 500, "2", "1", id = "ab"
//            ),
//            CategoriesModelClass(
//                "Soap",
//                "Samsung",
//                "too hot Laptop",
//                "Gross Wt. 840gms",
//                "Net Wt.450gms",
//                "220",
//                500,
//                "2",
//                "1",
//                quantityOfItem = 28,
//                id = "ac"
//            ),
//            CategoriesModelClass(
//                "Soap",
//                "Samsung",
//                "too hot Laptop",
//                "Gross Wt. 840gms",
//                "Net Wt.450gms",
//                "220",
//                500,
//                "2",
//                "1",
//                quantityOfItem = 32,
//                id = "ad"
//            ),
//            CategoriesModelClass(
//                "Soap",
//                "Samsung",
//                "too hot Laptop",
//                "Gross Wt. 840gms",
//                "Net Wt.450gms",
//                "220",
//                500,
//                "2",
//                "1",
//                quantityOfItem = 25,
//                id = "ae"
//            ),
//            CategoriesModelClass(
//                "Soap",
//                "Samsung",
//                "too hot Laptop",
//                "Gross Wt. 840gms",
//                "Net Wt.450gms",
//                "220",
//                500,
//                "2",
//                "1",
//                quantityOfItem = 8,
//                id = "af"
//            ),
//            CategoriesModelClass(
//                "Soap",
//                "Samsung",
//                "too hot Laptop",
//                "Gross Wt. 840gms",
//                "Net Wt.450gms",
//                "220",
//                500,
//                "2",
//                "1",
//                quantityOfItem = 5,
//                id = "ag"
//            ),
//            CategoriesModelClass(
//                "Soap",
//                "Samsung",
//                "too hot Laptop",
//                "Gross Wt. 840gms",
//                "Net Wt.450gms",
//                "220",
//                500,
//                "2",
//                "1",
//                quantityOfItem = 1,
//                id = "ah"
//            )
//
//        )
//    }

    fun getRecommendedData(): List<CategoriesModelClass> {
        return listOf(
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
                id = "a",
                groupId = 8.toString()
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
                id = "b",
                groupId = 9.toString()
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
                id = "c",
                groupId = 10.toString()
            ),
            CategoriesModelClass(
                "Phones",
                "Samsung",
                "too hot",
                "Gross Wt. 840gms",
                "Net Wt.450gms",
                "220",
                50,
                "2",
                "1",
                id = "d",
                groupId = 11.toString()
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
                id = "e",
                groupId = 12.toString()
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
                id = "f",
                groupId = 13.toString()
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
                id = "g",
                groupId = 14.toString()
            )

        )
    }


//    fun getRemoteData(context: Context) {
//
//        var collectionList: MutableList<CategoriesModelClass> = ArrayList()
//
//        val query = Storefront.query { rootQuery: Storefront.QueryRootQuery ->
//            rootQuery.shop { shopQuery: Storefront.ShopQuery ->
//                shopQuery.collections({ args: Storefront.ShopQuery.CollectionsArguments? ->
//                    args!!.first(
//                        4
//                    )
//                },
//                    { _queryBuilder ->
//                        _queryBuilder.edges { _queryBuilder: Storefront.CollectionEdgeQuery? ->
//                            _queryBuilder!!.node { _queryBuilder: Storefront.CollectionQuery? ->
//                                _queryBuilder!!.title()
//                                _queryBuilder.image { _queryBuilder -> _queryBuilder.src() }
//                                _queryBuilder.products({ args: Storefront.CollectionQuery.ProductsArguments? ->
//                                    args!!.first(
//                                        100
//                                    )
//                                }, { _queryBuilder ->
//                                    _queryBuilder.edges { _queryBuilder ->
//                                        _queryBuilder.node { _queryBuilder ->
//                                            _queryBuilder.title()
//
//                                                .description()
//
//                                                .images({ args: Storefront.ProductQuery.ImagesArguments? ->
//                                                    args!!.first(
//                                                        5
//                                                    )
//                                                }, { _queryBuilder ->
//                                                    _queryBuilder.edges { _queryBuilder ->
//                                                        _queryBuilder.node { _queryBuilder ->
//                                                            _queryBuilder.src().id()
//                                                        }
//                                                    }
//                                                })
//
//
//                                                .variants({ args: Storefront.ProductQuery.VariantsArguments? ->
//                                                    args!!.first(
//                                                        20
//                                                    )
//                                                },
//                                                    { _queryBuilder ->
//                                                        _queryBuilder.edges { _queryBuilder ->
//                                                            _queryBuilder.node { _queryBuilder ->
//                                                                _queryBuilder.price()
//                                                                    .selectedOptions { _queryBuilder ->
//                                                                        _queryBuilder.name().value()
//                                                                    }
//
//
//                                                            }
//                                                        }
//                                                    })
//
//
//                                        }
//                                    }
//                                })
//                            }
//                        }
//                    }
//                )
//            }
//        }
//
//        graphh = GraphClient.builder(context)
//            .accessToken(context.getString(R.string.storefront_api_key))
//            .shopDomain(context.getString(R.string.shopify_domain))
//            .build()
//
//        val call: QueryGraphCall = graphh!!.queryGraph(query)
//
//
//
//        call.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
//            override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
//
//                for (collectionEdge in response.data()!!.shop.collections.edges) {
//                    var collectionModelClass = CategoriesModelClass(
//                        itemName = collectionEdge.node.title,
//                        id = collectionEdge.node.id.toString()
//
//
//                    )
//                    var productList: MutableList<CategoriesModelClass> = ArrayList()
//                    collectionList.add(collectionModelClass)
//                    for (productEdge in collectionEdge.node.products.edges) {
//                        var productImageSrcList: MutableList<ModelClass> = ArrayList()
//
//                        // adding aa images to product imagelist
//                        for (imageedge in productEdge.node.images.edges)
//                            productImageSrcList.add(ModelClass(imageUrl = imageedge.node.src))
//
//                        var variantList: MutableList<VariantsModelClass> = ArrayList()
//
//                        //adding all variant data to product variant list
//                        for (variantEdge in productEdge.node.variants.edges) {
//                            var sizeIndex = 2;
//                            if (variantEdge.node.selectedOptions.size > 1 && variantEdge.node.selectedOptions[1].name == "Size")
//                                sizeIndex = 1
//                            variantList.add(
//                                VariantsModelClass(
//                                    variantEdge.node.id.toString(),
//                                    productEdge.node.id.toString(),
//                                    if (variantEdge.node.selectedOptions.size > 0) variantEdge.node.selectedOptions[0].value else null,
//                                    if (variantEdge.node.selectedOptions.size > 1 && sizeIndex < variantEdge.node.selectedOptions.size) variantEdge.node.selectedOptions[sizeIndex].value else null,
//                                    variantEdge.node.price.toFloat()
//
//
//                                )
//                            )
//                        }
//
//                        var productmodelclass = CategoriesModelClass(
//                            id = productEdge.node.id.toString(),
//                            itemName = productEdge.node.title,
//                            itemDescriptionText = productEdge.node.descriptionHtml,
//                            imageSrc = productImageSrcList,
//                            realTimeMrp = productEdge.node.variants.edges[0].node.price.precision(),
//                            variantsList = variantList,
//                            groupId = collectionEdge.node.id.toString()
//                        )
//
//                        productList.add(productmodelclass)
//
//                    }
//
//                    hashMap[collectionEdge.node.title.toString()] = productList
//                    mutablehashmap.postValue(hashMap as LinkedHashMap<String, List<CategoriesModelClass>>?)
//
//                }
//
//
//                mutableCollectionList.postValue(collectionList)
//
//            }
//
//
//            override fun onFailure(error: GraphError) {
//                Log.e("graphvalueerror", error.toString())
//            }
//        })
//
//
//    }


    fun getAllTheCollections(context: Context) {

        graphh = GraphClient.builder(context)
            .accessToken(context.getString(R.string.storefront_api_key))
            .shopDomain(context.getString(R.string.shopify_domain))
            .build()

        Log.d("connection", "started")

        val query = Storefront.query { rootQuery: Storefront.QueryRootQuery ->
            rootQuery.shop { shopQuery: Storefront.ShopQuery ->
                shopQuery.collections({ args: Storefront.ShopQuery.CollectionsArguments? ->
                    args!!.first(
                        4
                    )
                },
                    { _queryBuilder ->
                        _queryBuilder.edges { _queryBuilder: Storefront.CollectionEdgeQuery? ->
                            _queryBuilder!!.node { _queryBuilder: Storefront.CollectionQuery? ->
                                _queryBuilder!!.title()
                                _queryBuilder.image { _queryBuilder -> _queryBuilder.src() }
                            }
                        }
                    }
                )
            }
        }

        val call: QueryGraphCall = graphh!!.queryGraph(query)
        call.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
            var collectionList: MutableList<CategoriesModelClass> = ArrayList()
            override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                Log.d("connection", "received")


                for (collectionEdge in response.data()!!.shop.collections.edges) {
                    var collectionModelClass = CategoriesModelClass(
                        itemName = collectionEdge.node.title,
                        id = collectionEdge.node.id.toString(), itemCategory = "collection"
                    )

                    collectionList.add(collectionModelClass)
                    Log.d("connection", collectionList.toString())

                }
                mutableCollectionList.postValue(collectionList)


            }

            override fun onFailure(error: GraphError) {
                Log.d("connectionfail", error.message.toString())
            }
        })

        mutableCollectionList.observeForever { Log.d("connectionreceived", it.toString()) }
    }


    fun getProductDataBasedOnCollectionId(colllectionId: String) {
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


        val calldata: QueryGraphCall = graphh!!.queryGraph(query1)

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
                mutableproductList!!.postValue(productList)
                Log.d("responsedata", "${storefront.title} ${storefront.description}")

            }


            override fun onFailure(error: GraphError) {
                Log.e("graphvalueerror", error.toString())
            }
        })

    }

}