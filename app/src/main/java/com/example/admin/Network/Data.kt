package com.example.admin.Network

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
)