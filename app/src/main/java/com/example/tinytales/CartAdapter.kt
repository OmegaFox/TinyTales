package com.example.tinytales

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tinytales.databinding.ItemCartBinding
import com.google.firebase.firestore.FirebaseFirestore

class CartAdapter(private var cartItems: List<CartItem>,
                  private val onRemoveItem: (String) -> Unit,
                  private val onQuantityChange: () -> Unit) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem,
                 onRemoveItem: (String,) -> Unit,
                 onQuantityChange: () -> Unit)
        {
            binding.titleBook.text = cartItem.title
            binding.authorBook.text = cartItem.author
            binding.priceBook.text = cartItem.price
            binding.textQuantity.text = cartItem.quantity.toString()

            // Sử dụng Glide để tải hình ảnh sách
            Glide.with(binding.imageBook.context)
                .load(cartItem.imageUrl)
                .into(binding.imageBook)

            // Lắng nghe sự kiện khi người dùng nhấn nút "Close"
            binding.btnClose.setOnClickListener {
                onRemoveItem(cartItem.documentId) // Truyền document ID của sách cần xóa
            }

            // Xử lý sự kiện tăng số lượng sách
            binding.btnIncrease.setOnClickListener {
                cartItem.quantity += 1
                binding.textQuantity.text = cartItem.quantity.toString()
                onQuantityChange() // Gọi callback để cập nhật tổng giá
            }

            // Xử lý sự kiện giảm số lượng sách
            binding.btnDecrease.setOnClickListener {
                if (cartItem.quantity > 1) {
                    cartItem.quantity -= 1
                    binding.textQuantity.text = cartItem.quantity.toString()
                    onQuantityChange() // Gọi callback để cập nhật tổng giá
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position], onRemoveItem, onQuantityChange)
    }

    override fun getItemCount(): Int = cartItems.size

    // Cập nhật dữ liệu khi có thay đổi
    fun updateCartItems(newItems: List<CartItem>) {
        cartItems = newItems
        notifyDataSetChanged()
    }
}
