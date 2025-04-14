package com.project.clothingstore.adapter.Order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.Orders;
import com.project.clothingstore.view.activity.OrderDetailActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Orders> orderList;
    private Context context;

    public OrderAdapter(Context context, List<Orders> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Orders order = orderList.get(position);

        holder.tvOrderId.setText("Đơn hàng #\n" + order.getOrderId());
        Date date = order.getOrderDate().toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvOrderDate.setText(sdf.format(date));

        holder.tvProductName.setText(order.getOrderItems().get(0).getProductName());
        holder.tvProductDetails.setText("Kích cỡ: " + order.getOrderItems().get(0).getVariant().getSize() + " | Màu: " + order.getOrderItems().get(0).getVariant().getColor());

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvProductPrice.setText(formatter.format(order.getOrderItems().get(0).getPrice()) + " đ");
        holder.tvQuantity.setText("x" + order.getOrderItems().get(0).getQuantity());
        holder.tvQuantityLabel.setText("Số lượng: " + order.getOrderItems().size());
        holder.tvSubtotal.setText(formatter.format(order.calculateTotalPrice()) + " đ");

        String statusText = "";
        if(order.getStatus().equals("PENDING")) {
            statusText = "Chưa xử lí";
        } else if(order.getStatus().equals("SUCCESS")) {
            statusText = "Hoàn tất";
        } else if(order.getStatus().equals("CANCEL")) {
            statusText = "Hủy";
        }

        holder.tvStatus.setText(statusText);
        int colorRes = R.color.pending;
        if(order.getStatus().equals("SUCCESS")) colorRes = R.color.success;
        else if(order.getStatus().equals("CANCEL")) colorRes = R.color.cancel;


        holder.tvStatus.setTextColor(context.getResources().getColor(colorRes));

        Glide.with(context)
                .load(order.getOrderItems().get(0).getImage())
                .placeholder(R.drawable.error_image)
                .error(R.drawable.error_image)
                .into(holder.imvProductImage);

        // Handle btnDetails click here
        holder.btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order", order); // order must implement Serializable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setOrders(List<Orders> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvProductName, tvProductDetails, tvProductPrice, tvQuantity, tvSubtotal, tvStatus, tvQuantityLabel;
        MaterialButton btnDetails;
        ImageView imvProductImage;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId_O);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate_O);
            tvProductName = itemView.findViewById(R.id.tvProductName_O);
            tvProductDetails = itemView.findViewById(R.id.tvProductDetails_O);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice_O);
            tvQuantity = itemView.findViewById(R.id.tvQuantity_O);
            tvQuantityLabel = itemView.findViewById(R.id.tvQuantityLabel_O);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal_O);
            tvStatus = itemView.findViewById(R.id.tvStatus_O);
            btnDetails = itemView.findViewById(R.id.btnDetails_O);
            imvProductImage = itemView.findViewById(R.id.ivProductImage_O);
        }
    }
}