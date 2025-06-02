package com.example.tinytales

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tinytales.databinding.ActivityBookDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Lấy dữ liệu sách từ Intent
        val title = intent.getStringExtra("title")
        val rate = intent.getStringExtra("rate")
        val author = intent.getStringExtra("author")
        val category = intent.getStringExtra("category")
        val price = intent.getStringExtra("price")
        val imageUrl = intent.getStringExtra("imageUrl")
        val description = intent.getStringExtra("descriptionBook")
        findViewById<TextView>(R.id.descriptionBook).text = description ?: "No description available"

        // Hiển thị dữ liệu sách lên giao diện
        binding.titleBook.text = title
        binding.authorBook.text = "Author: $author"
        binding.categoryBook.text = "Category: $category"
        binding.priceBook.text = "Price: $price VND"
        binding.ratingBook.text = "Rating: $rate"
        binding.descriptionBook.text = "$description"

        // Load hình ảnh sử dụng Glide
        Glide.with(this)
            .load(imageUrl)
            .into(binding.imageBook)

        binding.backButton.setOnClickListener {
            val intent = Intent(this, DashbroadUserActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.cartButton.setOnClickListener {
            val intent = Intent(this, DashbroadUserActivity::class.java)
            intent.putExtra("open_cart", true)
            startActivity(intent)
            finish()
        }

        // Nút "Add to Cart" với kiểm tra đăng nhập
        binding.addToCartButton.setOnClickListener {
            if (isUserLoggedIn()) {
                // Người dùng đã đăng nhập, thêm vào giỏ hàng
                addToCart(title!!, author!!, price!!, imageUrl!!)
            } else {
                // Người dùng chưa đăng nhập, hiển thị hộp thoại
                showLoginDialog()
            }
        }
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    private fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Hiển thị hộp thoại yêu cầu đăng nhập
    private fun showLoginDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login required")
            .setMessage("You need to login to add products to cart. Do you want to login?")
            .setPositiveButton("Login") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Sign up") { _, _ ->
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
            .setNeutralButton("Cancel", null)
            .setCancelable(true)
            .show()
    }

    // Thêm sách vào giỏ hàng và lưu vào Firestore (chỉ khi đã đăng nhập)
    private fun addToCart(title: String, author: String, price: String, imageUrl: String) {
        val userId = auth.currentUser?.uid ?: return // Không thêm nếu không có userId

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        firestore.collection("cart_items")
            .whereEqualTo("userId", userId)
            .whereEqualTo("title", title)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    // Sản phẩm chưa có trong giỏ hàng, thêm mới
                    val cartItem = hashMapOf(
                        "title" to title,
                        "author" to author,
                        "price" to price,
                        "imageUrl" to imageUrl,
                        "userId" to userId,
                        "quantity" to 1,
                        "documentId" to ""
                    )

                    firestore.collection("cart_items")
                        .add(cartItem)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Add to cart successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Add to cart failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Sản phẩm đã có trong giỏ hàng, tăng số lượng
                    val document = result.documents[0]
                    val currentQuantity = document.getLong("quantity")?.toInt() ?: 1

                    document.reference.update("quantity", currentQuantity + 1)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Quantity in cart updated", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error updating cart", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error while checking cart", Toast.LENGTH_SHORT).show()
            }
    }
}
