package com.project.clothingstore.adapter.productcategory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.project.clothingstore.R;
import com.project.clothingstore.modal.ProductCategory;

import java.util.List;
public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductViewHolder> {
    private List<ProductCategory> listBST;
    public void setData(List<ProductCategory> list) {
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
        ProductCategory bst = listBST.get(position);
        if (bst == null) {
            return;
        }
        holder.imv.setImageResource(bst.getImv());
        holder.txtTitle.setText(bst.getTxtTitle());
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
