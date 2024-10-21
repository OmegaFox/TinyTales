package com.example.tinytales

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tinytales.databinding.FragmentEditUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditUserFragment : Fragment(R.layout.fragment_edit_user) {

    private lateinit var binding: FragmentEditUserBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditUserBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Lấy dữ liệu người dùng từ Firestore để hiển thị
        loadUserData()

        // Cho phép người dùng thay đổi từng trường thông tin
        binding.NameTV.setOnClickListener { showInputDialog("Name", binding.NameTV) }
        binding.emailTV.setOnClickListener { showInputDialog("Email", binding.emailTV) }
        binding.PhoneTV.setOnClickListener { showInputDialog("Phone", binding.PhoneTV) }
        binding.AddressTV.setOnClickListener { showInputDialog("Address", binding.AddressTV) }

        // Lưu dữ liệu mới khi nhấn nút Save
        binding.saveButton.setOnClickListener {
            saveUserData()
        }

        return binding.root
    }

    // Hàm hiển thị hộp thoại để thay đổi thông tin
    private fun showInputDialog(field: String, textView: TextView) {
        val editText = EditText(requireContext())
        editText.inputType = InputType.TYPE_CLASS_TEXT

        // Sử dụng textView.text.toString() để lấy giá trị cũ và setText() bằng CharSequence
        editText.setText(textView.text.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Update $field")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                // Khi người dùng nhấn OK, cập nhật giá trị mới vào textView
                textView.text = editText.text.toString() // Trả về String từ EditText
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    // Lấy dữ liệu người dùng từ Firestore để hiển thị
    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("User").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        user = document.toObject(User::class.java) ?: User()
                        // Hiển thị dữ liệu người dùng lên giao diện
                        binding.NameTV.text = user.name
                        binding.emailTV.text = user.email
                        binding.PhoneTV.text = user.phone
                        binding.AddressTV.text = user.address
                    }
                }
                .addOnFailureListener { e ->
                    // Xử lý lỗi khi tải dữ liệu
                }
        }
    }

    // Hàm lưu dữ liệu mới lên Firestore
    private fun saveUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            user.name = binding.NameTV.text.toString()
            user.email = binding.emailTV.text.toString()
            user.phone = binding.PhoneTV.text.toString()
            user.address = binding.AddressTV.text.toString()

            firestore.collection("User").document(userId).set(user)
                .addOnSuccessListener {
                    // Hiển thị thông báo khi lưu thành công
                }
                .addOnFailureListener { e ->
                    // Xử lý lỗi khi lưu thất bại
                }
        }
    }
}
