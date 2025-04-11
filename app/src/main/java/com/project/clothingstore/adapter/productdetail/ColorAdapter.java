// ColorAdapter.java
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
import com.project.clothingstore.modal.ColorItem;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private List<ColorItem> colors;
    private int selectedPosition = 0;
    private OnColorSelectedListener listener;

    public interface OnColorSelectedListener {
        void onColorSelected(ColorItem color);
    }

    public ColorAdapter(List<ColorItem> colors, OnColorSelectedListener listener) {
        this.colors = colors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        ColorItem color = colors.get(position);
        holder.colorNameTv.setText(color.getName());

        // Set selected state
        if (position == selectedPosition) {
            holder.colorNameTv.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.dmq_color_text_selector));
        } else {
            holder.colorNameTv.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.dmq_color_text_unselected));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onColorSelected(color);
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public void setSelectedPosition(int position) {
        if (position >= 0 && position < colors.size()) {
            int previousSelected = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
        }
    }

    public void selectColorByName(String colorName) {
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).getName().equals(colorName)) {
                setSelectedPosition(i);
                break;
            }
        }
    }

    static class ColorViewHolder extends RecyclerView.ViewHolder {
        TextView colorNameTv;

        ColorViewHolder(View itemView) {
            super(itemView);
            colorNameTv = itemView.findViewById(R.id.tvColorName);
        }
    }
}
