package com.example.admin

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.Network.ApiAdapter
import com.example.admin.Network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class TotalOrders : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.total_orders)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.getOrders(1)
                if (response.isSuccessful && response.body() != null){
                        val data = response.body()!!
                    data.order_uid.let{
                        val orderID: TextView = findViewById(R.id.orderID)
                        orderID.text = it
                    }

                }else{
                    Toast.makeText(this@TotalOrders, response.message(), Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(this@TotalOrders, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}