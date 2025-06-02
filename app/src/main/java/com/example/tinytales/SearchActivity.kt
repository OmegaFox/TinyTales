package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thiết lập RecyclerView
        recyclerViewSearch = binding.recyclerViewSearch
        recyclerViewSearch.layoutManager = LinearLayoutManager(this)

        searchAdapter = SearchAdapter(emptyList())
        recyclerViewSearch.adapter = searchAdapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val queryText = s.toString().trim()
                if (queryText.isNotEmpty()) {
                    performSearch(queryText)
                } else {
                    searchAdapter.updateBooks(emptyList())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Hàm tìm kiếm sách từ Firestore (không phân biệt hoa thường)
    private fun performSearch(queryText: String) {
        firestore.collection("Book")
            .get()
            .addOnSuccessListener { result ->
                val allBooks = result.toObjects(Book::class.java)

                // Lọc sách theo title và author (không phân biệt hoa thường)
                val filteredBooks = allBooks.filter { book ->
                    book.title.contains(queryText, ignoreCase = true) ||
                            book.author.contains(queryText, ignoreCase = true)
                }

                searchAdapter.updateBooks(filteredBooks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
