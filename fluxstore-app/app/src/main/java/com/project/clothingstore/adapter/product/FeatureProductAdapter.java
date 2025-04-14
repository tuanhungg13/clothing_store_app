package com.project.clothingstore.adapter.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.view.activity.ProductDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

public class FeatureProductAdapter extends RecyclerView.Adapter<FeatureProductAdapter.FreaturedProductViewHolder> {
    private List<Product> listSP;


    public void setData(List<Product> list) {
        this.listSP = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FreaturedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_freatured, parent, false);
        return new FreaturedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreaturedProductViewHolder holder, int position) {
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
        // Cắt ngắn tên sản phẩm nếu quá dài
        String productName = sp.getProductName();
        final int MAX_PRODUCT_NAME_LENGTH = 14; // Độ dài tối đa của tên sản phẩm
        if (productName.length() > MAX_PRODUCT_NAME_LENGTH) {
            productName = productName.substring(0, 14) + "...";
        }
        holder.txtName.setText(productName);

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext(); // Lấy context từ View
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", sp.getProductId());  // Truyền ID của sản phẩm
            context.startActivity(intent);
        });

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(sp.getPrice()); // "7,000,000,000"
        formatted = formatted.replace(",", ".");     // "7.000.000.000"
        holder.txtPrice.setText(String.valueOf("đ "+formatted));
        holder.ratingBar.setRating(sp.getTotalRating());
//        holder.txtOldPrice.setText("180.000");
//        holder.txtOldPrice.setPaintFlags(holder.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public int getItemCount() {
        if (listSP != null) {
            return listSP.size();
        }
        return 0;
    }

    public class FreaturedProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imv;
        TextView txtName, txtPrice, txtOldPrice;
        RatingBar ratingBar;
        public FreaturedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.imv_itemNB);
            txtName = itemView.findViewById(R.id.txt_ItemNameNB);
            txtPrice = itemView.findViewById(R.id.txt_ItemPriceNB);
            ratingBar = itemView.findViewById(R.id.ratingBar_freature_product);
//            txtOldPrice = itemView.findViewById(R.id.txt_ItemOldPriceNB);
        }
    }
}
