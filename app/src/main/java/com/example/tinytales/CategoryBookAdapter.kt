package com.example.tinytales

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tinytales.databinding.SearchRowBinding

class CategoryBookAdapter(private var books: List<Book>) : RecyclerView.Adapter<CategoryBookAdapter.BookViewHolder>() {

    class BookViewHolder(private val binding: SearchRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book,onItemClicked: (Book) -> Unit) {
            binding.titleBook.text = book.title
            binding.authorBook.text = book.author
            binding.priceBook.text = book.price

            // Sử dụng Glide để tải hình ảnh từ URL
            Glide.with(binding.imageBook.context)
                .load(book.imageUrl)
                .into(binding.imageBook)

            // Xử lý sự kiện click
            binding.root.setOnClickListener {
                onItemClicked(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = SearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book) { selectedBook ->
            // Chuyển dữ liệu sang BookDetailActivity khi sách được click
            val context = holder.itemView.context
            val intent = Intent(context, BookDetailActivity::class.java).apply {
                putExtra("title", selectedBook.title)
                putExtra("author", selectedBook.author)
                putExtra("category", selectedBook.category)
                putExtra("price", selectedBook.price)
                putExtra("imageUrl", selectedBook.imageUrl)
//                putExtra("description", selectedBook.description) // nếu bạn có thêm thông tin miêu tả
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged() // Cập nhật RecyclerView với danh sách mới
    }
}
