package com.example.tinytales

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinytales.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var cartAdapter: CartAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Thiết lập RecyclerView
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        // Khởi tạo adapter với một danh sách rỗng và truyền callback cho việc thay đổi số lượng
        cartAdapter = CartAdapter(emptyList(), { documentId ->
            removeCartItem(documentId) // Xử lý xóa sách
        }, {
            updateTotalPrice() // Cập nhật tổng giá khi số lượng thay đổi
        })
        binding.recyclerViewCart.adapter = cartAdapter

        loadCartItems()

        binding.checkout.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frame_dashbroad, CheckoutFragment())
                commit()
            }
        }
        return binding.root
    }

    // Truy vấn dữ liệu từ Firestore
    private fun loadCartItems() {
        val userId = auth.currentUser?.uid ?: "Users" // Lấy userId nếu có

        firestore.collection("cart_items")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val cartItems = result.toObjects(CartItem::class.java)
                // Lưu document ID cho từng item
                cartItems.forEachIndexed { index, item ->
                    item.documentId = result.documents[index].id
                }
                cartAdapter.updateCartItems(cartItems)
                updateTotalPrice() // Tính tổng giá ban đầu
            }
            .addOnFailureListener { e ->
                // Xử lý lỗi khi không lấy được dữ liệu
               Toast.makeText(requireContext(), "Error loading cart items", Toast.LENGTH_SHORT).show()
            }
    }

    // Hàm xóa sách khỏi Firestore
    private fun removeCartItem(documentId: String) {
        firestore.collection("cart_items").document(documentId)
            .delete()
            .addOnSuccessListener {
                // Xóa thành công, tải lại dữ liệu giỏ hàng
                loadCartItems()
            }


            .addOnFailureListener { e ->
                // Xử lý lỗi khi xóa thất bại
                Toast.makeText(requireContext(), "Error removing item", Toast.LENGTH_SHORT).show()
            }
    }

    private var cartItems = listOf<CartItem>()
    // Hàm tính tổng giá
    private fun updateTotalPrice() {
        var subtotal = 0.0

        for (item in cartItems) {
            subtotal += item.price.toDouble() * item.quantity
        }
        // Phí giao hàng là 10% của subtotal
        val shipping = subtotal * 0.10

        val total = subtotal + shipping

        binding.SubPrice.text = "$%.2f".format(subtotal)
        binding.shipPrice.text = "$%.2f".format(shipping)
        binding.TotalPrice.text = "$%.2f".format(total)
    }

}