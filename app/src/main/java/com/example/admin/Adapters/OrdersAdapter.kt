package com.example.admin.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.Network.Data
import com.example.admin.Network.Orders
import com.example.admin.R

class OrdersAdapter(private val orders: List<Data>) : RecyclerView.Adapter<OrdersAdapter.myViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_item, parent, false)
        return myViewholder(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: myViewholder, position: Int) {
        holder.bind(orders[position])
    }
    class myViewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val orderID: TextView = itemView.findViewById(R.id.orderID)
        private val paymentMode: TextView = itemView.findViewById(R.id.paymentMode)
        private val paymentStatus: TextView = itemView.findViewById(R.id.paymentStatus)
        private val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        private val orderAmount: TextView = itemView.findViewById(R.id.orderAmount)
        private val itemCount: TextView = itemView.findViewById(R.id.itemCount)

        fun bind(order: Data){
            orderID.text = order.order_uid
        }

    }
}