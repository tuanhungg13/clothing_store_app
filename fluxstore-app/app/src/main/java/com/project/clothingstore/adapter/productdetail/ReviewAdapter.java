
package com.project.clothingstore.adapter.productdetail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.Rating;
import com.project.clothingstore.modal.User;

import java.util.List;
import java.util.Map;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Rating> ratings;
    private Map<String, User> userMap;

    public ReviewAdapter(List<Rating> ratings, Map<String, User> userMap) {
        this.ratings = ratings;
        this.userMap = userMap;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Rating rating = ratings.get(position);
        holder.ratingBar.setRating(rating.getRate());
        holder.tvComment.setText(rating.getComment());

        // Cập nhật thông tin người dùng
        User user = userMap.get(rating.getUid());
        if (user != null) {
            holder.tvUsername.setText(user.getFullName());
            Glide.with(holder.imgAvatar.getContext())
                    .load(user.getAvatar())
                    .placeholder(R.drawable.user_default_avt)
                    .error(R.drawable.user_default_avt)
                    .circleCrop()
                    .into(holder.imgAvatar);
//            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
//                byte[] decodedString = Base64.decode(user.getAvatar(), Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                holder.imgAvatar.setImageBitmap(decodedByte);
//            }
        } else {
            holder.tvUsername.setText("User");
            holder.imgAvatar.setImageResource(R.drawable.user_default_avt);
        }

        // Cập nhật thời gian
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                rating.getCreatedAtMillis(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS);
        holder.tvTime.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public void updateRatings(List<Rating> newRatings) {
        this.ratings = newRatings;
        notifyDataSetChanged();
    }

    public void updateUserMap(Map<String, User> newUserMap) {
        this.userMap = newUserMap;
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvUsername, tvTime, tvComment;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgUserAvatar);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvTime = itemView.findViewById(R.id.tvReviewTime);
            tvComment = itemView.findViewById(R.id.tvReviewComment);
            ratingBar = itemView.findViewById(R.id.ratingBarReview);
        }
    }
}
