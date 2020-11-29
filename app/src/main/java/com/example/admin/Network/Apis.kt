package com.example.admin.Network

class Apis {

    companion object {
        private const val base = "http://aiishop.in/admin/api/v1/" /*"http://2b4cdc943388.ngrok.io/admin/api/v1/"*/
        const val categories = "${base}categories"
        const val addProduct = "${base}add/product"
        const val productDesc = "${base}product/desc/"
        const val imageUpload = "${base}product/addImage"
        const val tmpImage = "${base}content/"
        const val liveImage = "${base}content/"
        const val sizes = "${base}sizes"
        const val addSize = "${base}size"
        const val addStock = "${base}addStock"
        const val addCategory = "${base}category/add"
        const val uploadImage = "${base}upload"
    }

}