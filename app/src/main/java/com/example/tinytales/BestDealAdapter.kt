package com.example.tinytales

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tinytales.databinding.BestDealRowBinding

class BestDealAdapter(private val books: List<Book>) : RecyclerView.Adapter<BestDealAdapter.BookViewHolder>() {

    class BookViewHolder(private val binding: BestDealRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, onItemClicked: (Book) -> Unit) {
            binding.titleBook.text = book.title
            binding.authorBook.text = book.author
            binding.categoryBook.text = book.category
            binding.priceBook.text = book.price

            // load image using Glide
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
        val binding = BestDealRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return books.size
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
                putExtra("descriptionBook", selectedBook.descriptionBook)
                putExtra("rate" , selectedBook.rate)
            }
            context.startActivity(intent)
        }
    }
}