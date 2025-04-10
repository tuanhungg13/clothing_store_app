package com.project.clothingstore.adapter.productdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.clothingstore.R;

import java.util.List;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder> {
    private List<String> imageUrls;

    public ProductImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_image, parent, false);
        return new ImageViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
//        String imageUrl = imageUrls.get(position);
//
//        // Load image using Glide
//        Glide.with(holder.itemView.getContext())
//                .load(imageUrl)
//                .placeholder(R.drawable.aophong)
//                .error(R.drawable.aophong)
//                .into(holder.imageView);
//    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = imageUrls.get(position);

        try {
            int imageResId = Integer.parseInt(imagePath); // Chuyển từ chuỗi sang resource ID
            holder.imageView.setImageResource(imageResId);
        } catch (NumberFormatException e) {
            // Nếu không phải resource ID, thì tải ảnh từ URL
            Glide.with(holder.itemView.getContext())
                    .load(imagePath)
                    .placeholder(R.drawable.aophong)  // Ảnh mặc định khi tải
                    .error(R.drawable.error_image)  // Ảnh hiển thị khi lỗi
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public void updateImages(List<String> newImages) {
        this.imageUrls = newImages;
        notifyDataSetChanged();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgProduct);
        }
    }
}
