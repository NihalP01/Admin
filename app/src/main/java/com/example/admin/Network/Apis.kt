package com.example.admin.Network

class Apis {

    companion object {
        private const val base = /*"http://aiishop.in/admin/api/v1/"*/ "http://8ff93849e34b.ngrok.io/admin/api/v1/"
        const val categories = "${base}categories"
        const val addProduct = "${base}add/product"
        const val productDesc = "${base}product/desc/"
        const val imageUpload = "${base}addImage"
    }

}