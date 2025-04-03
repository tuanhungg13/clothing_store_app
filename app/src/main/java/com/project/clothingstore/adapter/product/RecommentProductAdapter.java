package com.project.clothingstore.adapter.product;

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
        holder.txtPrice.setText(String.valueOf("$ "+sp.getPrice()));
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
        TextView txtName, txtPrice;
        public FreaturedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.imv_item_dexuat);
            txtName = itemView.findViewById(R.id.txt_ItemName_dexuat);
            txtPrice = itemView.findViewById(R.id.txt_ItemPrice_dexuat);
        }
    }
}