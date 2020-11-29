package com.example.admin.Network

class DataClasses {

    data class Categories(var id: Int, var name: String)
    data class Sizes(var id: Int, var name: String)
    data class Stock(
        var id: Int,
        var sizeId: Int,
        var stock: Int,
        var price: String,
        var mrp: String,
        var discount: String
    )

}