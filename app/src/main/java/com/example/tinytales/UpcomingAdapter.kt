package com.example.tinytales

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tinytales.databinding.UpcomingBookRowBinding

class UpcomingAdapter(private val books: List<Book>) : RecyclerView.Adapter<UpcomingAdapter.BookViewHolder>() {

    class BookViewHolder(private val binding: UpcomingBookRowBinding) : RecyclerView.ViewHolder(binding.root) {
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
        val binding = UpcomingBookRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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