package com.project.clothingstore.adapter.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.OrderItems;

import java.text.DecimalFormat;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ItemViewHolder> {

    private Context context;
    private List<OrderItems> itemList;

    public OrderItemAdapter(Context context, List<OrderItems> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        OrderItems item = itemList.get(position);

        holder.tvName.setText(item.getProductName());
        holder.tvSizeColor.setText("Kích cỡ: " + item.getVariant().getSize() + " | Màu: " + item.getVariant().getColor());
//        holder.tvQuantity.setText("x" + item.getQuantity());

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvPrice.setText("đ " + formatter.format(item.getPrice()) + " x" + item.getQuantity());

        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.error_image)
                .error(R.drawable.error_image)
                .into(holder.imvProduct);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSizeColor, tvPrice, tvQuantity;
        ImageView imvProduct;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvSizeColor = itemView.findViewById(R.id.tvVariant);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
//            tvQuantity = itemView.findViewById(R.id.tvQuantity_ODP);
            imvProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
