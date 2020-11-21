package com.example.admin.Adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.Network.Data
import com.example.admin.OrdersDetails
import com.example.admin.R

class OrdersAdapter(private val context: Context, private val orders: List<Data>) :
    RecyclerView.Adapter<OrdersAdapter.MyViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_item, parent, false)
        return MyViewholder(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val list: Data = orders[position]
        holder.itemView.setOnClickListener {
            val intent = Intent(context, OrdersDetails::class.java)
            val bundle = Bundle()
            bundle.putSerializable("orders", list)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
        return holder.bind(orders[position])

    }

    class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderID: TextView = itemView.findViewById(R.id.orderID)
        private val paymentMode: TextView = itemView.findViewById(R.id.paymentMode)
        private val paymentStatus: TextView = itemView.findViewById(R.id.paymentStatus)
        private val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        private val orderAmount: TextView = itemView.findViewById(R.id.orderAmount)
        private val itemCount: TextView = itemView.findViewById(R.id.itemsCount)

        fun bind(order: Data) {
            orderID.text = order.order_uid
            paymentMode.text = order.payment_mode
            paymentStatus.text = " (${order.payment_status})"
            orderStatus.text = order.status
            orderAmount.text = order.amount.toString()
            itemCount.text = "Items Count: ${order.orderitems.size}"
        }
    }
}