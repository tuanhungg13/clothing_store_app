package com.project.clothingstore.adapter.productdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.Product;

import java.util.List;

public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(String productId);
    }

    public SimilarProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_similar_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        // Set product data
        holder.tvName.setText(product.getProductName());
        holder.tvPrice.setText("$" + product.getPrice() + ".00");

        // Load the first image if available
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImages().get(0))
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .centerCrop()
                    .into(holder.imgProduct);
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product.getProductId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}