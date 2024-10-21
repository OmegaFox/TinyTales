package com.example.tinytales

data class Book(
    val title: String = "",
    val author: String = "",
    val category: String = "",
    val price: String = "",
    val imageUrl: String = ""
)

data class CartItem(
    val title: String = "",
    val author: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val userId: String = "",
    var quantity: Int = 1, // Thêm thuộc tính số lượng
    var documentId: String = "" // Thêm documentId để lưu ID của sách trong Firestore
)



//data class TopBook(
//    val title: String = "",
//    val author: String = "",
//    val category: String = "",
//    val price: String = "",
//    val imageUrl: String = ""
//)
//
//data class UpcomingBook(
//    val title: String = "",
//    val author: String = "",
//    val category: String = "",
//    val price: String = "",
//    val imageUrl: String = ""
//)
