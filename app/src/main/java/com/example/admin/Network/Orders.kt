package com.example.admin.Network

data class Orders(
    val data: List<Data>,
    val lastPage: Int,
    val page: Int,
    val perPage: Int,
    val total: Int
)