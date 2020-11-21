package com.example.admin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.Network.ApiAdapter
import com.example.admin.Network.Data
import com.example.admin.Network.Orderitem

class OrdersDetails : AppCompatActivity(){
    lateinit var data: Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_details)
        val intent = intent
        data = intent.getSerializableExtra("orders") as Data
        supportActionBar?.title = data.order_uid
   
        val id = findViewById<TextView>(R.id.id)
        val productID = findViewById<TextView>(R.id.productID)
        val sizeID = findViewById<TextView>(R.id.sizeID)
        val productPrice = findViewById<TextView>(R.id.productPrice)
        val productMrp = findViewById<TextView>(R.id.productMRP)
        val createdAt = findViewById<TextView>(R.id.createdAt)
        val updatedAt = findViewById<TextView>(R.id.updatedAt)
    }
}