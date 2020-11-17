package com.example.admin.Network

data class Orderitem(
    val created_at: String,
    val id: Int,
    val mrp: Int,
    val order_id: Int,
    val price: Int,
    val product_id: Int,
    val size_id: Int,
    val updated_at: String
)