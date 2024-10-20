package com.example.tinytales

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinytales.databinding.ActivityCategoryBookBinding
import com.google.firebase.firestore.FirebaseFirestore

class CategoryBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBookBinding

    private lateinit var firestore: FirebaseFirestore
    private lateinit var categoryBookAdapter: CategoryBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Lấy thể loại từ Intent
        val category = intent.getStringExtra("category")

        // Thiết lập tiêu đề cho Activity dựa trên thể loại
        binding.category.text = category

        // Thiết lập RecyclerView
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(this)
        categoryBookAdapter = CategoryBookAdapter(emptyList())
        binding.recyclerViewCategory.adapter = categoryBookAdapter

        // Lấy sách theo thể loại từ Firestore
        fetchBooksByCategory(category!!)
    }

    private fun fetchBooksByCategory(category: String) {
        firestore.collection("TopBook")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                val books = result.toObjects(Book::class.java)
                categoryBookAdapter.updateBooks(books)
            }
            .addOnFailureListener { exception ->
                // Xử lý lỗi
            }
    }
}