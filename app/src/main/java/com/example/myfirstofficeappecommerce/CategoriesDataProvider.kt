package com.example.myfirstofficeappecommerce

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.shopify.buy3.*
import com.shopify.graphql.support.ID
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

object CategoriesDataProvider {
    var graphh: GraphClient? = null

    var mutableCollectionList: MutableLiveData<MutableList<CategoriesModelClass>> =
        MutableLiveData()


    var date1 = "2020:09:24 06:15:00"
    var date2 = "2020:09:25 08:18:00"

    var mutableproductList: MutableLiveData<List<CategoriesModelClass>>? = MutableLiveData()


    fun getListDataForHorizontalScroll(): List<UserDetailsModelClass> {
        return listOf(
            UserDetailsModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Laptop",
                "ok",
                "null",
                "null",
                id = ""
            ),
            UserDetailsModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Phone",
                "ok",
                "null",
                "null",
                id = ""
            ),
            UserDetailsModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Light",
                "ok",
                "null",
                "null",
                id = ""
            ),
            UserDetailsModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Toys",
                "ok",
                "null",
                "null",
                id = ""
            ),
            UserDetailsModelClass(
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


//

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
                for (collectionEdge in response.data()!!.shop.collections.edges) {
                    var collectionModelClass = CategoriesModelClass(
                        itemName = collectionEdge.node.title,
                        id = collectionEdge.node.id.toString(), itemCategory = "collection",
                        imageUrl = collectionEdge.node.image.src
                    )

                    collectionList.add(collectionModelClass)


                }
                mutableCollectionList.postValue(collectionList)


            }

            override fun onFailure(error: GraphError) {

            }
        }, null,
            RetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
                .whenResponse<Storefront.QueryRoot> { responsee: GraphResponse<Storefront.QueryRoot> ->
                    responsee.data()!!.shop.collections.edges == null

                }
                .maxCount(12)
                .build())


    }



    fun getMillies(): Long {


        var simpleDateFormatStartDate =
            SimpleDateFormat("yyyy:mm:dd hh:mm:ss", Locale.getDefault()).parse(date1)


        var simpleDateFormatEndDate =
            SimpleDateFormat("yyyy:mm:dd hh:mm:ss", Locale.getDefault()).parse(date2)

        return simpleDateFormatStartDate.time - simpleDateFormatEndDate.time


    }



}