package com.example.tinytales

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinytales.databinding.ActivityDeliveryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class DeliveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveryBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var deliveryAdapter: DeliveryAdapter
    private val orderList = mutableListOf<Order>() // Danh sách đơn hàng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Thiết lập RecyclerView
        setupRecyclerView()

        // Lấy danh sách đơn hàng từ Firestore
        loadUserOrders()
    }

    private fun setupRecyclerView() {
        deliveryAdapter = DeliveryAdapter(orderList)
        binding.recyclerViewDelivery.apply {
            layoutManager = LinearLayoutManager(this@DeliveryActivity)
            adapter = deliveryAdapter
        }
    }

    private fun loadUserOrders() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            firestore.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null) {
                        val orders = documents.toObjects<Order>() // Chuyển đổi dữ liệu Firestore thành đối tượng Order
                        orderList.clear()
                        orderList.addAll(orders) // Cập nhật danh sách đơn hàng
                        deliveryAdapter.notifyDataSetChanged() // Thông báo Adapter cập nhật dữ liệu
                    }
                }
                .addOnFailureListener { e ->
                    // Xử lý khi có lỗi xảy ra
                    e.printStackTrace()
                }
        }
    }
}
