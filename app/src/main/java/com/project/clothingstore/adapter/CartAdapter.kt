package com.project.clothingstore.adapter

import CartItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.clothingstore.R
import com.project.clothingstore.utils.helper.formatPrice

class CartAdapter(
    private var cartItems: MutableList<CartItem>,  // ✅ Dùng MutableList để có thể thay đổi trực tiếp
    private val onQuantityChanged: (CartItem, Int) -> Unit,// Gọi ra ngoài để cập nhật Firestore, nếu cần
    private val onItemRemoved: (CartItem) -> Unit,
    private val onSelectionChanged: (CartItem, Boolean) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ✅ Hàm cập nhật toàn bộ danh sách từ ngoài (nếu cần)
    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems.toMutableList() // Copy sang MutableList để thay đổi được
        notifyDataSetChanged()
    }

    // ✅ Hàm chỉ cập nhật 1 item cụ thể trong danh sách
    fun updateCartItemQuantity(productId: String, newQuantity: Int) {
        val index = cartItems.indexOfFirst { it.productId == productId }
        if (index != -1) {
            cartItems[index].quantity = newQuantity
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val tvProductQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        private val btnIncrease: TextView = itemView.findViewById(R.id.btnIncrease)
        private val btnDecrease: TextView = itemView.findViewById(R.id.btnDecrease)
        private val tvProductOptions: TextView = itemView.findViewById(R.id.tvProductOptions)
        private val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        private val cbSelect: CheckBox = itemView.findViewById(R.id.cbSelect)
        private val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        fun bind(cartItem: CartItem) {
            tvProductName.text = cartItem.productName
            tvProductPrice.text = formatPrice(cartItem.price)
            tvProductQuantity.text = cartItem.quantity.toString()
            tvProductOptions.text =
                "Size: ${cartItem.variant.size} | Màu: ${cartItem.variant.color}"
            // ✅ Xử lý nút tăng số lượng
            Glide.with(itemView.context)
                .load(cartItem.image) // lấy URL từ cartItem
                .placeholder(R.drawable.resource_default)
                .error(R.drawable.resource_default)
                .into(imgProduct)
            btnIncrease.setOnClickListener {
                if (cartItem.stock == null || cartItem.quantity < cartItem.stock!!) {
                    val newQuantity = cartItem.quantity + 1
                    cartItem.quantity = newQuantity
                    notifyItemChanged(adapterPosition)
                    onQuantityChanged(cartItem, newQuantity)
                } else {
                    Toast.makeText(itemView.context, "Vượt quá số lượng tồn kho", Toast.LENGTH_SHORT).show()
                }
            }
            cbSelect.setOnCheckedChangeListener(null)
            cbSelect.isChecked =
                cartItem.isSelected == true // Cần có thuộc tính isSelected trong CartItem

            cbSelect.setOnCheckedChangeListener { _, isChecked ->
                cartItem.isSelected = isChecked // Cập nhật trạng thái chọn
                onSelectionChanged(cartItem, isChecked) // Gửi về ngoài
            }
            // ✅ Xử lý nút giảm số lượng
            btnDecrease.setOnClickListener {
                if (cartItem.quantity > 1) {
                    val newQuantity = cartItem.quantity - 1
                    cartItem.quantity = newQuantity
                    notifyItemChanged(adapterPosition)
                    onQuantityChanged(cartItem, newQuantity)
                }
            }
            btnDelete.setOnClickListener() {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Xóa item khỏi danh sách
                    onItemRemoved(cartItems[position]) // Gọi ra ngoài nếu cần lưu vào Firestore
                    cartItems.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }
}
