package com.example.tinytales

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytales.databinding.ItemDeliveryBinding

class DeliveryAdapter(private val orderList: List<Order>) :
    RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = ItemDeliveryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orderList.size

    inner class DeliveryViewHolder(private val binding: ItemDeliveryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {

            // Hiển thị dữ liệu đơn hàng vào các TextView
            binding.orderTime.text = order.orderTimestamp?.toDate().toString()
            binding.titleBook.text = order.title
            binding.orderAddress.text = order.address
            binding.phoneOrder.text = order.phoneNumber
            binding.priceOrder.text = order.price
        }
    }
}
