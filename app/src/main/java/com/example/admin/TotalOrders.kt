package com.example.admin

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.Adapters.OrdersAdapter
import com.example.admin.Network.ApiAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TotalOrders : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.total_orders)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.getOrders("", 1)

                if (response.isSuccessful && response.body() != null) {
                    val ordersRecycler = findViewById<RecyclerView>(R.id.ordersRecycler)
                    val progressBar = findViewById<ProgressBar>(R.id.progressBar)
                    ordersRecycler.apply {
                        progressBar.visibility = View.GONE
                        layoutManager = LinearLayoutManager(this@TotalOrders)
                        setHasFixedSize(true)
                        adapter = response.body()?.data?.let {
                            OrdersAdapter(context, it)
                        }
                    }
                } else {
                    Toast.makeText(this@TotalOrders, response.message(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TotalOrders, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}