package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.toUpperCase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytales.databinding.ActivitySearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var recyclerViewSearch: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Thiết lập RecyclerView
        recyclerViewSearch = binding.recyclerViewSearch
        recyclerViewSearch.layoutManager = LinearLayoutManager(this)

        searchAdapter = SearchAdapter(emptyList()) // Adapter ban đầu không có dữ liệu
        recyclerViewSearch.adapter = searchAdapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val queryText = s.toString()
                if (queryText.isNotEmpty()) {
                    performSearch(queryText)
                } else {
                    searchAdapter.updateBooks(emptyList()) // Không có từ khóa thì không hiển thị gì
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    // Hàm tìm kiếm sách từ Firestore
    private fun performSearch(queryText: String) {
        val lowerCaseQuery = queryText.toUpperCase()

        firestore.collection("Book")
            .orderBy("title")
            .startAt(lowerCaseQuery)
            .endAt(queryText + "\uf8ff")
            .get()
            .addOnSuccessListener { result ->
                val books = result.toObjects(Book::class.java)
                searchAdapter.updateBooks(books) // Cập nhật danh sách sách vào Adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}