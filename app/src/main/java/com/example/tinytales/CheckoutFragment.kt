package com.example.tinytales

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tinytales.databinding.FragmentCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentAddress: String = "" // Biến lưu địa chỉ hiện tại
    private var userPhoneNumber: String = "" // Biến lưu số điện thoại người dùng

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Lấy địa chỉ và số điện thoại từ Firestore
        loadUserData()

        // Thay đổi địa chỉ trên giao diện khi nhấn "Change a New Delivery Address"
        binding.addNewAddress.setOnClickListener {
            showChangeAddressDialog()
        }

        // Xử lý sự kiện nhấn nút Pay
        binding.pay.setOnClickListener {
            if (validatePayment()) {
                saveCartToFirestore()
            }
            val intent = Intent(requireActivity(), PaymentReceivedActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    // Hàm lấy thông tin người dùng (địa chỉ, số điện thoại) từ Firestore
    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("User").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        currentAddress = document.getString("address") ?: "No address available"
                        userPhoneNumber = document.getString("phone") ?: "No phone number available"
                        binding.AddressTV.text = currentAddress
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error loading user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Hộp thoại cho phép thay đổi địa chỉ
    private fun showChangeAddressDialog() {
        val editText = EditText(requireContext())
        editText.setText(currentAddress)  // Hiển thị địa chỉ hiện tại

        AlertDialog.Builder(requireContext())
            .setTitle("Enter New Address")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                currentAddress = editText.text.toString() // Cập nhật địa chỉ trên giao diện
                binding.AddressTV.text = currentAddress
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Kiểm tra xem người dùng đã chọn phương thức thanh toán chưa
    private fun validatePayment(): Boolean {
        val selectedPaymentMethod = binding.radioGroup.checkedRadioButtonId
        if (selectedPaymentMethod == -1) {
            Toast.makeText(requireContext(), "Please select a payment method", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Hàm lưu giỏ hàng vào Firestore
    private fun saveCartToFirestore() {
        val userId = auth.currentUser?.uid ?: return

        // Lấy thông tin giỏ hàng
        firestore.collection("cart_items")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val cartItems = result.toObjects(CartItem::class.java)

                // Thời gian tạo đơn hàng
                val orderTimestamp = com.google.firebase.Timestamp.now()

                // Lưu lại giỏ hàng cùng địa chỉ giao hàng
                for (cartItem in cartItems) {
                    val orderData = hashMapOf(
                        "userId" to userId,
                        "itemId" to cartItem.itemId,
                        "title" to cartItem.title,
                        "quantity" to cartItem.quantity,
                        "price" to cartItem.price,
                        "address" to currentAddress,
                        "phoneNumber" to userPhoneNumber,
                        "paymentMethod" to getSelectedPaymentMethod(),
                        "orderTimestamp" to orderTimestamp // Lưu thời gian tạo đơn hàng
                    )

                    firestore.collection("orders")
                        .add(orderData)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error placing order", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error loading cart items", Toast.LENGTH_SHORT).show()
            }
    }

    // Lấy phương thức thanh toán đã chọn
    private fun getSelectedPaymentMethod(): String {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioButton1 -> "CreditCard"
            R.id.radioButton2 -> "Cash on Delivery"
            else -> "Unknown"
        }
    }
}
