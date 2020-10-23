package com.example.myfirstofficeappecommerce

data class Item(
    val activity: String,
    val collection_items: List<CollectionItem>,
    val icon: String,
    val id: String,
    val name: String,
    val nesteditems: List<Nesteditem>,
    val text_align: String,
    val type: String,
    val typeid: String,
    val url: String,
    var parentIndexId:Int=-1,
    var indexid:Int=-1
)