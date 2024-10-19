package com.example.tinytales

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
        fun bind(book: Book) {
            binding.titleBook.text = book.title
            binding.authorBook.text = book.author
            binding.categoryBook.text = book.category
            binding.priceBook.text = book.price

            // load image using Glide
            Glide.with(binding.imageBook.context)
                .load(book.imageUrl)
                .into(binding.imageBook)
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
        holder.bind(book)
    }
}