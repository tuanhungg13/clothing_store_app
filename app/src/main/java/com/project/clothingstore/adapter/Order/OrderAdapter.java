package com.project.clothingstore.adapter.Order;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.Orders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Orders> orderList;
    private OnOrderClickListener onOrderClickListener;

    public OrderAdapter(List<Orders> orderList, OnOrderClickListener onOrderClickListener) {
        this.orderList = orderList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n") // ???
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Orders order = orderList.get(position);
        holder.tvOrderId.setText("Đơn hàng #" + order.getOrderId());
        Date date = order.getOrderDate().toDate(); // Chuyển Timestamp thành Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(date);
        holder.tvOrderDate.setText(formattedDate);
//        holder.tvOrderDate.setText(order.getOrderDate().toDate().format);
        holder.tvProductName.setText(order.getOrderItems().get(0).getProductName());
        holder.tvProductDetails.setText("Size: " + order.getOrderItems().get(0).getSize() + " | Color" + order.getOrderItems().get(0).getColor());
        Log.d("OrderAdapter", "Product Price: " + order.getOrderItems().get(0).getPrice());
        holder.tvProductPrice.setText("" + order.getOrderItems().get(0).getPrice() + "đ");
        holder.tvQuantity.setText("x" + order.getOrderItems().get(0).getQuantity());
        holder.tvSubtotal.setText(order.calculateTotalPrice() + "đ");
        holder.tvStatus.setText(order.getStatus());
        if(order.getStatus().equals("PENDING")) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.pending));
        } else if(order.getStatus().equals("SUCCESS")) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.success));
        } else if(order.getStatus().equals("CANCEL")) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.cancel));
        }
//        holder.tvStatus.setTextColor(order.getStatusColor());

        // Handle details button click
        holder.btnDetails.setOnClickListener(v -> onOrderClickListener.onOrderClicked(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId, tvOrderDate, tvProductName, tvProductDetails, tvProductPrice, tvQuantity, tvSubtotal, tvStatus;
        MaterialButton btnDetails;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId_O);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate_O);
            tvProductName = itemView.findViewById(R.id.tvProductName_O);
            tvProductDetails = itemView.findViewById(R.id.tvProductDetails_O);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice_O);
            tvQuantity = itemView.findViewById(R.id.tvQuantity_O);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal_O);
            tvStatus = itemView.findViewById(R.id.tvStatus_O);
            btnDetails = itemView.findViewById(R.id.btnDetails_O);
        }
    }

    public interface OnOrderClickListener {
        void onOrderClicked(Orders order);
    }

    public void setOrders(List<Orders> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }
}