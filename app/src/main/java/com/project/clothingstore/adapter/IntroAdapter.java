package com.project.clothingstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.model.IntroItem;

import java.util.List;

public class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.IntroViewHolder> {

    private final List<IntroItem> introItemList;

    public IntroAdapter(List<IntroItem> introItemList) {
        this.introItemList = introItemList;
    }

    @NonNull
    @Override
    public IntroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.intro_item, parent, false);
        return new IntroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntroViewHolder holder, int position) {
        holder.bind(introItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return introItemList.size();
    }

    public static class IntroViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView title, description;

        public IntroViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgModel);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(IntroItem item) {
            imageView.setImageResource(item.getImageResId());
            title.setText(item.getTitle());
            description.setText(item.getDescription());
        }
    }
}
