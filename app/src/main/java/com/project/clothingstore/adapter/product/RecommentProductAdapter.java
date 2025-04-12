package com.project.clothingstore.adapter.product;

import android.content.Context;
import android.content.Intent;
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

public class RecommentProductAdapter extends RecyclerView.Adapter<RecommentProductAdapter.FreaturedProductViewHolder> {
    private List<Product> listSP;

    public void setData(List<Product> list) {
        this.listSP = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FreaturedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recomment_product, parent, false);
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

        holder.txtName.setText(sp.getProductName());

        holder.txtPrice.setText(String.valueOf("đ "+ formatted(sp.getPrice())));

        holder.txtOldPrice.setText(formatted(sp.getPriceBeforeDiscount()));
        holder.txtOldPrice.setPaintFlags(holder.txtOldPrice.getPaintFlags() | holder.txtOldPrice.getPaintFlags() | 16);
        holder.ratingBar.setRating(sp.getTotalRating());

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext(); // Lấy context từ View
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", sp.getProductId());  // Truyền ID của sản phẩm
            context.startActivity(intent);
        });

    }


    // Format số tiền
    public String formatted(double number){
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedd = formatter.format(number); // "7,000,000,000"
        formattedd = formattedd.replace(",", ".");     // "7.000.000.000"
        return formattedd;
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
            imv = itemView.findViewById(R.id.imv_item_dexuat);
            txtName = itemView.findViewById(R.id.txt_ItemName_dexuat);
            txtPrice = itemView.findViewById(R.id.txt_ItemPrice_dexuat);
            txtOldPrice = itemView.findViewById(R.id.txt_ItemOldPrice_dexuat);
            ratingBar = itemView.findViewById(R.id.rating_product_recomment);
        }
    }
}