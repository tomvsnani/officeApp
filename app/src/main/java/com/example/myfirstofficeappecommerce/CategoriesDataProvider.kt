package com.example.myfirstofficeappecommerce

import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass

class CategoriesDataProvider {
    fun getMapDataForCategories():LinkedHashMap<String,List<CategoriesModelClass>>?{
        var map:LinkedHashMap<String,List<CategoriesModelClass>> = LinkedHashMap()
        map.put("Phones" , listOf(CategoriesModelClass("Phones","Samsung phone","too hot" ,
        "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Phones","Samsung phone","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1")

            ))

           map?.put( "Laptops" ,listOf(CategoriesModelClass("Laptops","Samsung laptop","too hot Laptop" ,
               "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
                CategoriesModelClass("Laptops","Samsung laptop","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1")

            ))

       map?.put( "Soap" , listOf(CategoriesModelClass("Soap","Santoor","too hot Laptop" ,
           "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1"),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1")

        )
        )
        return map
    }

    fun getListDataForHorizontalScroll():List<ModelClass>{
      return listOf(
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Laptop",
                "ok",
                "null",
                "null"
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Phone",
                "ok",
                "null",
                "null"
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Light",
                "ok",
                "null",
                "null"
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Toys",
                "ok",
                "null",
                "null"
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Mugs",
                "ok",
                "null",
                "null"
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Phone",
                "ok",
                "null",
                "null"
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Light",
                "ok",
                "null",
                "null"
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Toys",
                "ok",
                "null",
                "null"
            ),
            ModelClass(
                Constants.HORIZONTAL_LIST_ITEM_NAMES,
                "Mugs",
                "ok",
                "null",
                "null"
            )

        )
    }
}