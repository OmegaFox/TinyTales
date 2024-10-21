package com.example.tinytales

import android.content.Intent
import android.os.Bundle
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
        val author = intent.getStringExtra("author")
        val category = intent.getStringExtra("category")
        val price = intent.getStringExtra("price")
        val imageUrl = intent.getStringExtra("imageUrl")
        val description = intent.getStringExtra("description")

        // Hiển thị dữ liệu sách lên giao diện
        binding.titleBook.text = title
        binding.authorBook.text = "Author: $author"
        binding.categoryBook.text = "Category: $category"
        binding.priceBook.text = "Price: $price"
        binding.descriptionBook.text = "Description: \n$description"

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

        // Nút "Add to Cart"
        binding.addToCartButton.setOnClickListener {
            addToCart(title!!, author!!, price!!, imageUrl!!)
        }
    }

    // Thêm sách vào giỏ hàng và lưu vào Firestore
    private fun addToCart(title: String, author: String, price: String, imageUrl: String) {
        val userId = auth.currentUser?.uid ?: "guest"  // Lấy userId nếu có, nếu không thì để là "guest"
        val cartItem = hashMapOf(
            "title" to title,
            "author" to author,
            "price" to price,
            "imageUrl" to imageUrl,
            "userId" to userId,
            "quantity" to 1,
            "documentId" to ""
        )

        // Lưu sách vào collection "cart_items"
        firestore.collection("cart_items")
            .add(cartItem)
            .addOnSuccessListener {
                // Hiển thị thông báo khi thêm thành công
                Toast.makeText(this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Xử lý lỗi
                Toast.makeText(this,"Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show()
            }
    }
}