package com.project.clothingstore.adapter.productdetail;

import android.text.format.DateUtils;
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

        // Set rating and comment
        holder.ratingBar.setRating(rating.getRate());
        holder.tvComment.setText(rating.getComment());

        // Set time ago
        long timeInMillis = rating.getCreatedAt();
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                timeInMillis,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS);
        holder.tvTime.setText(timeAgo);

        // Set user data if available
        if (userMap != null && userMap.containsKey(rating.getUidId())) {
            User user = userMap.get(rating.getUidId());
            holder.tvUsername.setText(user.getFullName());

            // Here you could also load a user avatar if you had that field
            // For now we use the default avatar
        } else {
            holder.tvUsername.setText("User");
        }
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
