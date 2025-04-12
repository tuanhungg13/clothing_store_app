package com.project.clothingstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.ProfileOption;

import java.util.List;

public class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionAdapter.OptionViewHolder> {

    private final List<ProfileOption> optionList;
    private final OnOptionClickListener listener;


    public interface OnOptionClickListener {
        void onClick(ProfileOption option);
    }
    public ProfileOptionAdapter(List<ProfileOption> optionList, OnOptionClickListener listener) {
        this.optionList = optionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_option, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        ProfileOption option = optionList.get(position);
        holder.tvOption.setText(option.getTitle());
        holder.imgIcon.setImageResource(option.getIconResId());

        // Ẩn mũi tên nếu là "Đăng xuất"
        if ("Đăng xuất".equals(option.getTitle()) || option.getTitle().isEmpty()) {
            holder.imgArrow.setVisibility(View.GONE);
        } else {
            holder.imgArrow.setVisibility(View.VISIBLE);
            holder.tvOption.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            holder.imgIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(option));
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView tvOption;
        ImageView imgIcon, imgArrow;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOption = itemView.findViewById(R.id.tv_option);
            imgIcon = itemView.findViewById(R.id.img_icon);
            imgArrow = itemView.findViewById(R.id.img_arrow);
        }
    }
}


