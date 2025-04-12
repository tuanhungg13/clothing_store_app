package com.project.clothingstore.adapter.productdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.Product;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private OnProductClickListener listener;
    private static final int MAX_PRODUCT_NAME_LENGTH = 15; // Giới hạn độ dài tên sản phẩm
    private final DecimalFormat priceFormatter;

    public interface OnProductClickListener {
        void onProductClick(String productId);
    }

    public SimilarProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;

        // Khởi tạo formatter để định dạng giá tiền
        this.priceFormatter = new DecimalFormat("#,###");
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

        // Cắt ngắn tên sản phẩm nếu quá dài
        String productName = product.getProductName();
        if (productName.length() > MAX_PRODUCT_NAME_LENGTH) {
            productName = productName.substring(0, MAX_PRODUCT_NAME_LENGTH - 3) + "...";
        }
        holder.tvName.setText(productName);

        // Format giá tiền theo định dạng Việt Nam
        String formattedPrice = "đ " + priceFormatter.format(product.getPrice());
        holder.tvPrice.setText(formattedPrice);

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
