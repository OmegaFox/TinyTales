package com.example.tinytales

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tinytales.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Lấy thông tin người dùng từ Firestore
        loadUserData()

        // Khi nhấn vào nút "Edit Profile", chuyển sang EditUserFragment
        binding.userProfileButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                Log.d("editbutton", "EditButton has been press")
                replace(R.id.frame_dashbroad, EditUserFragment())
                addToBackStack(null)
                commit()
            }
        }

        binding.userDeliveryButton.setOnClickListener {
            // Tạo Intent để chuyển sang DeliveryActivity
            val intent = Intent(requireActivity(), DeliveryActivity::class.java)
            startActivity(intent)
        }


        binding.logoutButton.setOnClickListener {

            logoutUser()
        }


        return binding.root
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid

        Log.d("AccountFragment", "User ID: $userId")

        if (userId != null) {
            firestore.collection("User").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {

                        val user = document.toObject(User::class.java)
                        Log.d("AccountFragment", "User Data: ${user?.name}, ${user?.email}")
                        if (user != null) {

                            Log.d("AccountFragment", "User Data: ${user.name}, ${user.email}")

                            // Hiển thị dữ liệu người dùng lên giao diện
                            binding.nameTV.text = user.name
                            binding.emailTV.text = user.email
                            binding.PhoneTV.text = user.phone
                            binding.AddressTV.text =user.address
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("AccountFragment", "Error loading user data", e)
                }
        }
    }

    private fun logoutUser() {
        // Xóa thông tin đăng nhập khỏi SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.clear()  // Xóa tất cả dữ liệu trong SharedPreferences
        editor.apply()

        // Đăng xuất khỏi Firebase Auth
        FirebaseAuth.getInstance().signOut()

        // Chuyển về màn hình đăng nhập
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Xóa Activity trước
        startActivity(intent)
        requireActivity().finish() // Kết thúc Activity hiện tại
    }


}
