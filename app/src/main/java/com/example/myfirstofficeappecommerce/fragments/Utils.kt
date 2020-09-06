package com.example.myfirstofficeappecommerce.fragments

import com.example.myfirstofficeappecommerce.ApplicationClass

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