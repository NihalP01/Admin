package com.example.admin

import UserPrefManager
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.admin.Network.ApiAdapter
import com.example.admin.activity.add_product_ui.AddProductUI
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val auth =
    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjEsImlhdCI6MTYwNjQwMzU0Mn0.6VuYV9kEKM_G9y-kYo0yuqcsTocu30xyongBUsCuUBY"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        val totalOrder = findViewById<CardView>(R.id.totalOrders)
        totalOrder.setOnClickListener {
            val intent = Intent(this, TotalOrders::class.java)
            startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.getHome(auth)
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    data.orders.let {
                        val totalOrders: TextView = findViewById(R.id.totalOrdersCount)
                        totalOrders.text = it
                    }
                    data.products.let {
                        val totalProducts: TextView = findViewById(R.id.totalProductsCount)
                        totalProducts.text = it
                    }
                    data.pendingOrders.let {
                        val pendingOrders: TextView = findViewById(R.id.pendingOrdersCount)
                        pendingOrders.text = it
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error Occurred!. ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error Occurred ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        addProductCard?.setOnClickListener {
            startActivity(Intent(this, AddProductUI::class.java))
        }

    }
}