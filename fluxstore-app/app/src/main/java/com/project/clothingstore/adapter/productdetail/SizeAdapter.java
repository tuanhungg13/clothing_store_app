// SizeAdapter.java
package com.project.clothingstore.adapter.productdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.SizeItem;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {

    private List<SizeItem> sizes;
    private int selectedPosition = 0;
    private OnSizeSelectedListener listener;

    public interface OnSizeSelectedListener {
        void onSizeSelected(SizeItem size);
    }

    public SizeAdapter(List<SizeItem> sizes, OnSizeSelectedListener listener) {
        this.sizes = sizes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.size_item, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        SizeItem size = sizes.get(position);
        holder.sizeTv.setText(size.getName());

        // Set selected state
        if (position == selectedPosition) {
            holder.sizeTv.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.dmq_size_circle_selected));
            holder.sizeTv.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.sizeTv.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.dmq_size_circle_unselected));
            holder.sizeTv.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onSizeSelected(size);
        });
    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

    public void setSelectedPosition(int position) {
        if (position >= 0 && position < sizes.size()) {
            int previousSelected = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
        }
    }

    public void selectSizeByName(String sizeName) {
        for (int i = 0; i < sizes.size(); i++) {
            if (sizes.get(i).getName().equals(sizeName)) {
                setSelectedPosition(i);
                break;
            }
        }
    }

    static class SizeViewHolder extends RecyclerView.ViewHolder {
        TextView sizeTv;

        SizeViewHolder(View itemView) {
            super(itemView);
            sizeTv = itemView.findViewById(R.id.tvSize);
        }
    }
}
