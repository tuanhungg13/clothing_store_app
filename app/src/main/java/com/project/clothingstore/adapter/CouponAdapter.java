package com.project.clothingstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.Coupon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {

    private List<Coupon> couponList;
    private final Context context;

    public CouponAdapter(Context context, List<Coupon> couponList) {
        this.context = context;
        this.couponList = couponList;
    }
    public void setData(List<Coupon> coupons) {
        this.couponList = coupons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);
        return new CouponViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {
        Coupon coupon = couponList.get(position);
        holder.txtTitle.setText(coupon.getTitle());
        holder.txtCode.setText("Code: " + coupon.getCode());
        String discountStr = coupon.getDiscount() >= 1000
                ? (coupon.getDiscount() / 1000) + "K"
                : String.valueOf(coupon.getDiscount());
        holder.txtDiscount.setText(discountStr);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        String dateStr = sdf.format(coupon.getExpirationDate().toDate());
        holder.txtExpiry.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public static class CouponViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtCode, txtDiscount, txtExpiry;

        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tv_title);
            txtCode = itemView.findViewById(R.id.tv_code);
            txtDiscount = itemView.findViewById(R.id.tv_discount);
            txtExpiry = itemView.findViewById(R.id.tv_expired);
        }
    }
}

