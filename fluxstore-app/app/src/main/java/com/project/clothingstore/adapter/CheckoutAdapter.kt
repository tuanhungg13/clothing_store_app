package com.project.clothingstore.adapter

import CartItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.clothingstore.R
import com.project.clothingstore.utils.helper.formatPrice

class CheckoutAdapter(
    private val context: Context,
    private val orderItems: List<CartItem>
) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        private val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        private val tvVariant: TextView = view.findViewById(R.id.tvVariant)
        private val imgProduct: ImageView = view.findViewById(R.id.imgProduct)

        fun bind(item: CartItem) {
            tvProductName.text = item.productName
            tvProductPrice.text = "₫${formatPrice(item.price)} x ${item.quantity}"
            tvVariant.text = "Size: ${item.variant.size} | Color: ${item.variant.color}"
            Glide.with(itemView.context)
                .load(item.image) // lấy URL từ cartItem
                .placeholder(R.drawable.resource_default)
                .error(R.drawable.resource_default)
                .into(imgProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = orderItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderItems[position])
    }
}
