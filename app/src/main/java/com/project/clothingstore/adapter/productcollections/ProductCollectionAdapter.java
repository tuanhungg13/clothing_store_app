package com.project.clothingstore.adapter.productcollections;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.ProductCollections;
import com.project.clothingstore.view.activity.collection.CollectionActivity;
import com.project.clothingstore.view.activity.product.ProductsActivity;

import java.util.List;
public class ProductCollectionAdapter extends RecyclerView.Adapter<ProductCollectionAdapter.ProductViewHolder> {
    private List<ProductCollections> listBST;
    public void setData(List<ProductCollections> list) {
        this.listBST = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bosuutap, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductCollections bst = listBST.get(position);
        if (bst == null) {
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(bst.getCollectionImg()) // Lấy ảnh đầu tiên trong danh sách
                .placeholder(R.drawable.spnb) // Ảnh tạm khi load
                .error(R.drawable.item) // Ảnh hiển thị nếu load lỗi
                .into(holder.imv);
        holder.txtTitle.setText(bst.getCollectionName());


        holder.itemView.setOnClickListener(v -> {
            // Chuyển đến CollectionActivity và truyền dữ liệu
            Intent intent = new Intent(holder.itemView.getContext(), CollectionActivity.class);
            intent.putExtra("collectionImg", bst.getCollectionImg());
            intent.putExtra("collectionId", bst.getCollectionId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (listBST != null){
            return listBST.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imv;
        TextView txtTitle;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.img_BST);
            txtTitle = itemView.findViewById(R.id.txt_img_BST);
        }
    }
}
