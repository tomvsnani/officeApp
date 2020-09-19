package com.example.myfirstofficeappecommerce.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CategoriesViewModelFactory(var id:String) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return   CategoriesViewModel(id) as T
    }
}