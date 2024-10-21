package com.example.tinytales

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tinytales.databinding.TopBookRowBinding

class TopBooksAdapter(private val books: List<Book>) : RecyclerView.Adapter<TopBooksAdapter.BookViewHolder>() {

    class BookViewHolder(private val binding: TopBookRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, onItemClicked: (Book) -> Unit) {
            // Gán dữ liệu cho các view
            binding.titleBook.text = book.title
            binding.authorBook.text = book.author
            binding.categoryBook.text = book.category
            binding.price.text = book.price


            // Sử dụng Glide để load ảnh
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
        val binding = TopBookRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                putExtra("descriptionBook", selectedBook.description) // nếu bạn có thêm thông tin miêu tả
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = books.size


}