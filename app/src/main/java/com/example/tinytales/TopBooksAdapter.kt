package com.example.tinytales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TopBooksAdapter(private val books: List<Book>) : RecyclerView.Adapter<TopBooksAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleBook: TextView = itemView.findViewById(R.id.titleBook)
        val authorBook: TextView = itemView.findViewById(R.id.authorBook)
        val categoryBook: TextView = itemView.findViewById(R.id.categoryBook)
        val price: TextView = itemView.findViewById(R.id.price)
        val imageBook: ImageView = itemView.findViewById(R.id.imageBook)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopBooksAdapter.BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.top_book_row, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopBooksAdapter.BookViewHolder, position: Int) {
        val book = books[position]
        holder.titleBook.text = book.title
        holder.authorBook.text = book.author
        holder.categoryBook.text = book.category
        holder.price.text = book.price

        // load image using Glide
        Glide.with(holder.itemView.context)
            .load(book.imageUrl)
            .into(holder.imageBook)

    }

    override fun getItemCount(): Int {
        return books.size
    }


}