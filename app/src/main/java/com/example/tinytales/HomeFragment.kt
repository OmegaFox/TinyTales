package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var topBooksAdapter: TopBooksAdapter
    private lateinit var recyclerViewTopBook: RecyclerView

    private lateinit var recyclerBestDeal: RecyclerView
    private lateinit var bestDealAdapter: BestDealAdapter

    private lateinit var recyclerUpcomingBook: RecyclerView
    private lateinit var upcomingAdapter: UpcomingAdapter



    private val firestore = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewTopBook = view.findViewById(R.id.recyclerViewTopBook)
        recyclerViewTopBook.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerBestDeal = view.findViewById(R.id.recyclerViewBestDeal)
        recyclerBestDeal.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerUpcomingBook = view.findViewById(R.id.recyclerViewUpcomingBook)
        recyclerUpcomingBook.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Gọi hàm fetch dữ liệu từ Firestore
        fetchTopBooks()
        fetchBestDeal()
        fetchUpcomingBook()

        // xu ly search
        val searchButton = view.findViewById<ImageButton>(R.id.imageSearchButton)
        searchButton.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }



    }

    private fun fetchTopBooks() {
        firestore.collection("TopBook")
            .get()
            .addOnSuccessListener { result ->
                // Chuyển kết quả query Firestore thành danh sách Book
                val bookList = result.toObjects(Book::class.java)

//                // In dữ liệu vào log để kiểm tra
//                for (book in bookList) {
//                    // In ra thông tin của mỗi cuốn sách
//                    android.util.Log.d("FirestoreData", "Title: ${book.title}, Author: ${book.author}, Price: ${book.price}")
//                }

                // Khởi tạo Adapter và set dữ liệu vào RecyclerView
                topBooksAdapter = TopBooksAdapter(bookList)
                recyclerViewTopBook.adapter = topBooksAdapter

            }
            .addOnFailureListener { e ->

            }
    }

    private fun fetchBestDeal() {
        firestore.collection("BestBook")
            .get()
            .addOnSuccessListener { result ->
                // Chuyển kết quả query Firestore thành danh sách Book
                val bookList = result.toObjects(Book::class.java)

                // Khởi tạo Adapter và set dữ liệu vào RecyclerView
                bestDealAdapter = BestDealAdapter(bookList)
                recyclerBestDeal.adapter = bestDealAdapter

            }
            .addOnFailureListener { e ->
            }
    }

    private fun fetchUpcomingBook() {
        firestore.collection("UpcomingBook")
            .get()
            .addOnSuccessListener { result ->
                // Chuyển kết quả query Firestore thành danh sách Book
                val bookList = result.toObjects(Book::class.java)

                // Khởi tạo Adapter và set dữ liệu vào RecyclerView
                upcomingAdapter = UpcomingAdapter(bookList)
                recyclerUpcomingBook.adapter = upcomingAdapter

            }
            .addOnFailureListener { e ->
            }
    }
}