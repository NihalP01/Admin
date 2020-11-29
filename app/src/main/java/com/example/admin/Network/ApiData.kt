package com.example.admin.Network

import java.io.Serializable

data class ApiData(
    val products: String,
    val orders: String,
    val pendingOrders: String
)

data class Orders(
    val data: List<Data>,
    val lastPage: Int,
    val page: Int,
    val perPage: Int,
    val total: Int
) : Serializable

data class Data(
    val address_id: Int,
    val amount: Int,
    val created_at: String,
    val id: Int,
    val order_uid: String,
    val orderitems: List<Orderitem>,
    val payment_mode: String,
    val payment_status: String,
    val status: String,
    val updated_at: String,
    val user: Any,
    val user_id: Int
): Serializable

data class Orderitem(
    val created_at: String,
    val id: Int,
    val mrp: Int,
    val order_id: Int,
    val price: Int,
    val product_id: Int,
    val size_id: Int,
    val updated_at: String
) : Serializable

data class login(
    val type: String,
    val token: String
) : Serializable
