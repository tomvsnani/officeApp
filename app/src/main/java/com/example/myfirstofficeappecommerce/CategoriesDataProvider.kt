package com.example.myfirstofficeappecommerce

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.shopify.buy3.*
import com.shopify.graphql.support.ID
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

object CategoriesDataProvider {
    var graphh: GraphClient? = null

    var mutableCollectionList: MutableLiveData<MutableList<CategoriesModelClass>> =
        MutableLiveData()


    var date1 = "2020:09:24 06:15:00"
    var date2 = "2020:09:25 08:18:00"

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
                id = ""
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Phone",
                "ok",
                "null",
                "null",
                id = ""
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Light",
                "ok",
                "null",
                "null",
                id = ""
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Toys",
                "ok",
                "null",
                "null",
                id = ""
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Mugs",
                "ok",
                "null",
                "null",
                id = ""
            )

        )
    }

    fun getCategoryData(): List<CategoriesModelClass> {
        return listOf(

            CategoriesModelClass(
                Constants.CIRCLE_SLIDER,
                Constants.CATEGORY_PRODUCT,
                "Product ",
                imageUrl =
                "https://rukminim1.flixcart.com/image/714/857/k65d18w0/shirt/p/4/t/48-bfrybluesht02ab-being-fab-original-imaecvnxndp3zbdn.jpeg?q=50"
            ),
            CategoriesModelClass(
                Constants.CIRCLE_SLIDER,
                Constants.CATEGORY_COLLECTION,
                "Collection",
                imageUrl = "https://kathmandu.imgix.net/catalog/product/1/5/15108_605_federatewomenslsshirt_v2_a.jpg"
            ),
            CategoriesModelClass(
                Constants.CIRCLE_SLIDER,
                Constants.CATEGORY_CUSTOM,
                "CustomName1",
                categoryLink = "https://www.google.com",
                imageUrl = "https://kathmandu.imgix.net/catalog/product/1/5/15108_605_federatewomenslsshirt_v2_a.jpg"
            ),
            CategoriesModelClass(
                Constants.CIRCLE_SLIDER,
                Constants.CATEGORY_CUSTOM,
                "CustomName2",
                categoryLink = "https://www.facebook.com",
                imageUrl = "https://5.imimg.com/data5/UC/TY/MY-9601095/100-25-cotton-fancy-casual-shirt-for-men-500x500.jpg"


            )
        )
    }

    fun getRecommendedData(): List<VariantsModelClass> {
        return listOf(
            VariantsModelClass(
                "Phonesg",
                "Samsung phone",
                "too hot",
                "Gross Wt. 840gms",
                400f,
                "220",
                isSelected = false,
                isfav = false,
                quantityOfItem = 1,
                name = "Phonesg"


            ),
            VariantsModelClass(
                "Phonesf",
                "Samsung phone",
                "too hot",
                "Gross Wt. 840gms",
                400f,
                "220",
                isSelected = false,
                isfav = false,
                quantityOfItem = 1,
                name = "Phonesg"
            ),
            VariantsModelClass(
                "Phonese",
                "Samsung phone",
                "too hot",
                "Gross Wt. 840gms",
                400f,
                "220",
                isSelected = false,
                isfav = false,
                quantityOfItem = 1,
                name = "Phonesg"
            ),
            VariantsModelClass(
                "Phonesd",
                "Samsung phone",
                "too hot",
                "Gross Wt. 840gms",
                400f,
                "220",
                isSelected = false,
                isfav = false,
                quantityOfItem = 1,
                name = "Phonesg"
            ),
            VariantsModelClass(
                "Phonesc",
                "Samsung phone",
                "too hot",
                "Gross Wt. 840gms",
                400f,
                "220",
                isSelected = false,
                isfav = false,
                quantityOfItem = 1,
                name = "Phonesg"
            ),
            VariantsModelClass(
                "Phonesb",
                "Samsung phone",
                "too hot",
                "Gross Wt. 840gms",
                400f,
                "220",
                isSelected = false,
                isfav = false,
                quantityOfItem = 1,
                name = "Phonesg"
            ),
            VariantsModelClass(
                "Phonesa",
                "Samsung phone",
                "too hot",
                "Gross Wt. 840gms",
                400f,
                "220",
                isSelected = false,
                isfav = false,
                quantityOfItem = 1,
                name = "Phonesg"
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
            .httpCache(File(context.applicationContext.cacheDir, "/http"), 10 * 1024 * 1024)
            .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES))
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
                        id = collectionEdge.node.id.toString(), itemCategory = "collection",
                        imageUrl = collectionEdge.node.image.src
                    )

                    collectionList.add(collectionModelClass)
                    Log.d("connection", collectionList.toString())

                }
                mutableCollectionList.postValue(collectionList)


            }

            override fun onFailure(error: GraphError) {
                Log.d("connectionfail", error.message.toString())
            }
        }, null,
            RetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
                .whenResponse<Storefront.QueryRoot> { responsee: GraphResponse<Storefront.QueryRoot> ->
                    responsee.data()!!.shop.collections.edges == null

                }
                .maxCount(12)
                .build())

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
                        imageSrcOfVariants = productImageSrcList,
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

    fun getMillies(): Long {


        var simpleDateFormatStartDate =
            SimpleDateFormat("yyyy:mm:dd hh:mm:ss", Locale.getDefault()).parse(date1)


        var simpleDateFormatEndDate =
            SimpleDateFormat("yyyy:mm:dd hh:mm:ss", Locale.getDefault()).parse(date2)

        return simpleDateFormatStartDate.time - simpleDateFormatEndDate.time


    }

    private fun getCalender(date1: String): Calendar {
        var calender1 = Calendar.getInstance()
        var date = date1.split(" ")[0]
        var time = date1.split(" ")[1]
        calender1.set(
            date.split(":")[0].toInt(),
            date.split(":")[1].toInt(),
            date.split(":")[2].toInt(),
            time.split(":")[0].toInt(),
            time.split(":")[1].toInt(),
            time.split(":")[2].toInt()
        )
        return calender1
    }

}