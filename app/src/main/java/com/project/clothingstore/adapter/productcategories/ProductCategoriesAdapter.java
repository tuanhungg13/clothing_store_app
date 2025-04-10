package com.project.clothingstore.adapter.productcategories;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.ProductCategories;
import com.project.clothingstore.view.activity.product.ProductsActivity;

import java.util.List;

public class ProductCategoriesAdapter extends RecyclerView.Adapter<ProductCategoriesAdapter.ProductCategoriesViewHolder> {
    private List<ProductCategories> list;
    private String categoriId;

    public String getCategoriId() {
        return categoriId;
    }

    public void setCategoriId(String categoriId) {
        this.categoriId = categoriId;
    }

    public void setData(List<ProductCategories> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_discover, parent, false);
        return new ProductCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoriesViewHolder holder, int position) {
        ProductCategories productCategories = list.get(position);
        if (productCategories == null) {
            return;
        }
        holder.txtName_discover.setText(productCategories.getCategoryName());
        holder.txt_sl_discover.setText(Integer.toString(productCategories.getQuantity()) + " items");
        this.categoriId = productCategories.getCategoryId();

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductsActivity.class);
            intent.putExtra("categoryId", productCategories.getCategoryId());
            intent.putExtra("categoryName", productCategories.getCategoryName());
            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ProductCategoriesViewHolder extends RecyclerView.ViewHolder {
        TextView txtName_discover, txt_sl_discover;
        public ProductCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName_discover = itemView.findViewById(R.id.txt_Nameitem_discover);
            txt_sl_discover = itemView.findViewById(R.id.txt_sl_item_discover);
        }
        // Define your ViewHolder class here
    }
}
