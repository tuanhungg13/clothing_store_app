package com.project.clothingstore.adapter.productcollections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.ProductCollections;

import java.util.List;

public class ProductCollectionAdapter3 extends RecyclerView.Adapter<ProductCollectionAdapter3.ProductViewHolder3> {
    private List<ProductCollections> listBST;
    public void setData(List<ProductCollections> list) {
        this.listBST = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suu_tap_all, parent, false);
        return new ProductViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder3 holder, int position) {
        ProductCollections bst = listBST.get(position);
        if (bst == null) {
            return;
        }
        holder.imv.setImageResource(bst.getImv());
        holder.txtTitle.setText(bst.getTxtTitle());
        holder.txtMessage.setText(bst.getMessenge());

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = 600;
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        if (listBST != null){
            return listBST.size();
        }
        return 0;
    }

    public class ProductViewHolder3 extends RecyclerView.ViewHolder {
        ImageView imv;
        TextView txtTitle, txtMessage;
        public ProductViewHolder3(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.img_BST_All);
            txtTitle = itemView.findViewById(R.id.txt_title_BSTAll);
            txtMessage = itemView.findViewById(R.id.txt_title_BSTAll);
        }
    }
}
