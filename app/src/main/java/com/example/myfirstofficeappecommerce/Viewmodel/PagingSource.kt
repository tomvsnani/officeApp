package com.example.myfirstofficeappecommerce.Viewmodel

import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass

 class PagingSource(
    var viewModel: CategoriesViewModel,
    var isFirstRequest: String? = null
) : androidx.paging.PagingSource<String, CategoriesModelClass>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, CategoriesModelClass> {
        var prev: String = params.key ?: isFirstRequest!!
        var next: String = params.key ?: isFirstRequest!!
        viewModel.getProductDataBasedOnColletionId()
        var list: MutableList<CategoriesModelClass>? = ArrayList()
        viewModel.mutableLiveData!!.observeForever {
            list!!.addAll(it)
        }
        return LoadResult.Page(list!!, prev, next)
    }


}