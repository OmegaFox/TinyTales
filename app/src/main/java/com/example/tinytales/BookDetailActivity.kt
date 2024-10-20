package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.tinytales.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}