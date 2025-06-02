package com.example.tinytales

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.tinytales.databinding.FragmentCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentAddress: String = ""
    private var userPhoneNumber: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Kiểm tra trạng thái giỏ hàng khi load fragment
        checkCartStatusAndUpdateUI()

        // Lấy địa chỉ và số điện thoại từ Firestore
        loadUserData()

        // Thay đổi địa chỉ
        binding.addNewAddress.setOnClickListener {
            showChangeAddressDialog()
        }

        // Xử lý sự kiện nhấn nút Pay
        binding.pay.setOnClickListener {
            performCheckout()
        }

        binding.backBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frame_dashbroad, CartFragment())
                commit()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Kiểm tra lại trạng thái giỏ hàng mỗi khi fragment được hiển thị
        checkCartStatusAndUpdateUI()
    }

    // Kiểm tra trạng thái giỏ hàng và cập nhật UI
    private fun checkCartStatusAndUpdateUI() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            // Nếu chưa đăng nhập, disable nút thanh toán
            disablePaymentButton("Please login to checkout")
            return
        }

        firestore.collection("cart_items")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val cartItems = result.toObjects(CartItem::class.java)

                if (cartItems.isEmpty()) {
                    // Giỏ hàng trống - disable nút thanh toán
                    disablePaymentButton("Cart is empty")
                } else {
                    // Giỏ hàng có sản phẩm - enable nút thanh toán
                    enablePaymentButton()
                }
            }
            .addOnFailureListener { e ->
                disablePaymentButton("Error checking cart")
                Toast.makeText(requireContext(), "Error checking cart status", Toast.LENGTH_SHORT).show()
            }
    }

    // Disable nút thanh toán
    private fun disablePaymentButton(message: String) {
        binding.pay.isEnabled = false
        binding.pay.alpha = 0.5f
        binding.pay.text = message
    }

    // Enable nút thanh toán
    private fun enablePaymentButton() {
        binding.pay.isEnabled = true
        binding.pay.alpha = 1.0f
        binding.pay.text = "Pay Now"
    }

    // Hàm thực hiện checkout với các kiểm tra tuần tự
    private fun performCheckout() {
        // Kiểm tra 1: Người dùng đã đăng nhập chưa
        if (!isUserLoggedIn()) {
            showLoginDialog()
            return
        }

        // Kiểm tra 2: Giỏ hàng có trống không (kiểm tra trước khi tiếp tục)
        checkCartBeforeCheckout()
    }

    // Kiểm tra giỏ hàng trước khi cho phép checkout
    private fun checkCartBeforeCheckout() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("cart_items")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val cartItems = result.toObjects(CartItem::class.java)

                if (cartItems.isEmpty()) {
                    Toast.makeText(requireContext(), "Cart is empty. Please add items to cart before checkout", Toast.LENGTH_LONG).show()
                    disablePaymentButton("Cart is empty")
                    return@addOnSuccessListener
                }

                // Nếu giỏ hàng không trống, tiếp tục kiểm tra các điều kiện khác
                proceedWithOtherValidations()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error checking cart", Toast.LENGTH_SHORT).show()
            }
    }

    // Tiếp tục với các kiểm tra khác sau khi đã xác nhận giỏ hàng không trống
    private fun proceedWithOtherValidations() {
        // Kiểm tra 3: Có địa chỉ giao hàng chưa
        if (!hasValidAddress()) {
            Toast.makeText(requireContext(), "Please enter shipping address", Toast.LENGTH_SHORT).show()
            return
        }

        // Kiểm tra 4: Đã chọn phương thức thanh toán chưa
        if (!validatePayment()) {
            return // validatePayment() đã hiển thị thông báo
        }

        // Nếu tất cả điều kiện đều thỏa mãn, tiến hành checkout
        saveCartToFirestore()
        val intent = Intent(requireActivity(), PaymentReceivedActivity::class.java)
        startActivity(intent)
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    private fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Hiển thị dialog hỏi đăng nhập
    private fun showLoginDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Login required")
            .setMessage("You need to log in to make a payment. Do you want to log in?")
            .setPositiveButton("Login") { _, _ ->
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Sign up") { _, _ ->
                val intent = Intent(requireActivity(), RegisterActivity::class.java)
                startActivity(intent)
            }
            .setNeutralButton("Cancel", null)
            .setCancelable(false)
            .show()
    }

    // Kiểm tra địa chỉ có hợp lệ không
    private fun hasValidAddress(): Boolean {
        return currentAddress.isNotEmpty() &&
                currentAddress != "No address available" &&
                currentAddress.trim().isNotEmpty()
    }

    // Hàm lấy thông tin người dùng từ Firestore
    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("User").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        currentAddress = document.getString("address") ?: ""
                        userPhoneNumber = document.getString("phone") ?: ""

                        if (currentAddress.isEmpty()) {
                            binding.AddressTV.text = "No shipping address yet"
                        } else {
                            binding.AddressTV.text = currentAddress
                        }
                    } else {
                        binding.AddressTV.text = "No shipping address yet"
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error loading user information", Toast.LENGTH_SHORT).show()
                    binding.AddressTV.text = "Error loading address"
                }
        } else {
            binding.AddressTV.text = "Please login"
        }
    }

    private fun showChangeAddressDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_address_custom, null)
        val editText = dialogView.findViewById<EditText>(R.id.dialog_edit_text)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        editText.setText(currentAddress)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnConfirm.setOnClickListener {
            val newAddress = editText.text.toString().trim()
            if (newAddress.isNotEmpty()) {
                currentAddress = newAddress
                binding.AddressTV.text = currentAddress
                saveAddressToFirestore(newAddress)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Address cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



    // Lưu địa chỉ mới vào Firestore
    private fun saveAddressToFirestore(address: String) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("User").document(userId)
            .update("address", address)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Address updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error updating address", Toast.LENGTH_SHORT).show()
            }
    }

    // Kiểm tra phương thức thanh toán
    private fun validatePayment(): Boolean {
        val selectedPaymentMethod = binding.radioGroup.checkedRadioButtonId
        if (selectedPaymentMethod == -1) {
            Toast.makeText(requireContext(), "Please select payment method", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveCartToFirestore() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("cart_items")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val cartItems = result.toObjects(CartItem::class.java)
                val orderTimestamp = com.google.firebase.Timestamp.now()

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
                        "orderTimestamp" to orderTimestamp
                    )

                    firestore.collection("orders")
                        .add(orderData)
                        .addOnSuccessListener {
                            clearCartAfterOrder(userId)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error placing order", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error loading cart", Toast.LENGTH_SHORT).show()
            }
    }

    // Xóa giỏ hàng sau khi đặt hàng thành công
    private fun clearCartAfterOrder(userId: String) {
        firestore.collection("cart_items")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    document.reference.delete()
                }
                Toast.makeText(requireContext(), "Order successful", Toast.LENGTH_SHORT).show()
            }
    }

    // Lấy phương thức thanh toán đã chọn
    private fun getSelectedPaymentMethod(): String {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioButton1 -> "Credit Card"
            R.id.radioButton2 -> "Cash on Delivery"
            else -> "Unknown"
        }
    }
}
