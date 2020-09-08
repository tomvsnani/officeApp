package com.example.myfirstofficeappecommerce

class Utils {
    companion object{
        fun getItemCount():String{
            var itemCount = 0
            ApplicationClass.selectedItemsList?.filter {
                itemCount += it.quantityOfItem
                return@filter true
            }
            return itemCount.toString()
        }
    }
}