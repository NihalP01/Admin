package com.example.admin.Network

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiClient {

    @GET("home")
    suspend fun getHome(@Header("Authorization") BearerToken: String): Response<ApiData>

    @GET("orders/{page}")
    suspend fun getOrders(
        @Header("Authorization") BearerToken: String,
        @Path("page") page: Int
    ): Response<Orders>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<login>
}


object ApiAdapter {
    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("http://staging.aiishop.in/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
        .create(ApiClient::class.java)
}
