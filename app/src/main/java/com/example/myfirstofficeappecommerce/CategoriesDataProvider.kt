package com.example.myfirstofficeappecommerce

import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment

class CategoriesDataProvider {
    fun getMapDataForCategories():LinkedHashMap<String,List<CategoriesModelClass>>?{
        var map:LinkedHashMap<String,List<CategoriesModelClass>> = LinkedHashMap()
        map.put("Phones" , listOf(CategoriesModelClass("Phones","Samsung phone","too hot" ,
        "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=1),
            CategoriesModelClass("Phones","Samsung phone","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=2),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=3),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=4),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=5),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=6),
            CategoriesModelClass("Phones","Samsung","too hot" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=7)

            ))

           map?.put( "Laptops" ,listOf(CategoriesModelClass("Laptops","Samsung laptop","too hot Laptop" ,
               "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=1),
                CategoriesModelClass("Laptops","Samsung laptop","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=2),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=3),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=4),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=5),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=6),
                CategoriesModelClass("Laptops","Samsung","too hot Laptop" ,
                    "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=7)

            ))

       map?.put( "Soap" , listOf(CategoriesModelClass("Soap","Santoor","too hot Laptop" ,
           "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=1),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=2),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=3),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=4),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=5),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=6),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=7)

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

    fun getSearhItemsData(): List<CategoriesModelClass> {
        return listOf(CategoriesModelClass("Soap","Santoor","too hot Laptop" ,
            "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",id=1),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",quantityOfItem = 28,id=2),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",quantityOfItem = 32,id=3),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",quantityOfItem = 25,id=4),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",quantityOfItem = 8,id=5),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",quantityOfItem = 5,id=6),
            CategoriesModelClass("Soap","Samsung","too hot Laptop" ,
                "Gross Wt. 840gms","Net Wt.450gms","220","500","2","1",quantityOfItem = 1,id=7)

        )
    }
}