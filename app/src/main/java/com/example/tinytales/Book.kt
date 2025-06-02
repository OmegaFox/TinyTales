package com.example.tinytales

data class Book(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val category: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val descriptionBook: String = "",
    val rate: String = ""
)

data class CartItem(
    val title: String = "",
    val author: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val userId: String = "",
    val itemId: String = "",
    var quantity: Int = 1,
    var documentId: String = ""
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