package com.project.clothingstore.adapter.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.project.clothingstore.view.activity.ProductDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    List<Product> listSP;
    public void setData(List<Product> list) {
        this.listSP = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product sp = listSP.get(position);
        if (sp == null || sp.getImages() == null || sp.getImages().isEmpty()) {
            return;
        }

        // Load ảnh từ URL bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(sp.getImages().get(0)) // Lấy ảnh đầu tiên trong danh sách
                .placeholder(R.drawable.item) // Ảnh tạm khi load
                .error(R.drawable.aophong) // Ảnh hiển thị nếu load lỗi
                .into(holder.imv);

        String productName = sp.getProductName();
        final int MAX_PRODUCT_NAME_LENGTH = 18; // Độ dài tối đa của tên sản phẩm
        if (productName.length() > MAX_PRODUCT_NAME_LENGTH) {
            productName = productName.substring(0, 18) + "...";
        }
        holder.txtName.setText(productName);

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(sp.getPrice()); // "7,000,000,000"
        formatted = formatted.replace(",", ".");     // "7.000.000.000"
        holder.txtPrice.setText(String.valueOf("đ " + formatted));
        holder.txtOldPrice.setText(String.valueOf("đ " + formatter.format(sp.getPriceBeforeDiscount())));
        holder.txtOldPrice.setPaintFlags(holder.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.ratingBar.setRating(sp.getTotalRating());
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext(); // Lấy context từ View
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", sp.getProductId());  // Truyền ID của sản phẩm
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (listSP != null) {
            return listSP.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imv;
        TextView txtName, txtPrice, txtOldPrice;

        RatingBar ratingBar;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.imv_item_product);
            txtName = itemView.findViewById(R.id.txt_name_product);
            txtPrice = itemView.findViewById(R.id.txt_price_product);
            txtOldPrice = itemView.findViewById(R.id.txt_oldprice_product);
            ratingBar = itemView.findViewById(R.id.rating_product);
        }
    }
}