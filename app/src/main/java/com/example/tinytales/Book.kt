package com.example.tinytales

data class Book(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val category: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val description: String = ""
)

data class CartItem(
    val title: String = "",
    val author: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val userId: String = "",
    val itemId: String = "",
    var quantity: Int = 1, // Thêm thuộc tính số lượng
    var documentId: String = "" // Thêm documentId để lưu ID của sách trong Firestore
)

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val books: List<Book> = listOf(),
    val orderTimestamp: com.google.firebase.Timestamp? = null,
    val address: String = "",
    val phoneNumber: String = "",
    val price: String = "",
    val title: String = ""
)