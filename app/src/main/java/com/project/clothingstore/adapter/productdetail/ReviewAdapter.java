////package com.project.clothingstore.adapter.productdetail;
////
////import android.text.format.DateUtils;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ImageView;
////import android.widget.RatingBar;
////import android.widget.TextView;
////
////import androidx.annotation.NonNull;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.bumptech.glide.Glide;
////import com.project.clothingstore.R;
////import com.project.clothingstore.modal.Rating;
////import com.project.clothingstore.modal.User;
////
////import java.util.List;
////import java.util.Map;
////
////
////public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
////    private List<Rating> ratings;
////    private Map<String, User> userMap;
////
////    public ReviewAdapter(List<Rating> ratings, Map<String, User> userMap) {
////        this.ratings = ratings;
////        this.userMap = userMap;
////    }
////
////    @NonNull
////    @Override
////    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext())
////                .inflate(R.layout.item_review, parent, false);
////        return new ReviewViewHolder(view);
////    }
////
//////    @Override
//////    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
//////        Rating rating = ratings.get(position);
//////
//////        // Set rating and comment
//////        holder.ratingBar.setRating(rating.getRate());
//////        holder.tvComment.setText(rating.getComment());
//////
//////        // Set time ago
//////        long timeInMillis = rating.getCreatedAt();
//////        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//////                timeInMillis,
//////                System.currentTimeMillis(),
//////                DateUtils.MINUTE_IN_MILLIS);
//////        holder.tvTime.setText(timeAgo);
//////
//////        // Set user data if available
//////        if (userMap != null && userMap.containsKey(rating.getUidId())) {
//////            User user = userMap.get(rating.getUidId());
//////            holder.tvUsername.setText(user.getFullName());
//////
//////            // Here you could also load a user avatar if you had that field
//////            // For now we use the default avatar
//////        } else {
//////            holder.tvUsername.setText("User");
//////        }
//////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
////        Rating rating = ratings.get(position);
////
////        // Set rating và comment
////        holder.ratingBar.setRating(rating.getRate());
////        holder.tvComment.setText(rating.getComment());
////
////        // Set thời gian tương đối - sử dụng phương thức mới
////        long timeInMillis = rating.getCreatedAtMillis();
////        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
////                timeInMillis,
////                System.currentTimeMillis(),
////                DateUtils.MINUTE_IN_MILLIS);
////        holder.tvTime.setText(timeAgo);
////
////        // Set thông tin người dùng nếu có
////        if (userMap != null && userMap.containsKey(rating.getUidId())) {
////            User user = userMap.get(rating.getUidId());
////            holder.tvUsername.setText(user.getFullName());
////
////            // Tải avatar người dùng nếu có
//////            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
//////                Glide.with(holder.imgAvatar.getContext())
//////                        .load(user.getAvatar())
//////                        .placeholder(R.drawable.error_image)
//////                        .error(R.drawable.error_image)
//////                        .into(holder.imgAvatar);
//////            }
////
////            if (user != null && user.getAvatar() != null && !user.getAvatar().isEmpty()) {
////                Glide.with(holder.imgAvatar.getContext())
////                        .load(user.getAvatar())
////                        .placeholder(R.drawable.error_image)
////                        .error(R.drawable.error_image)
////                        .circleCrop() // Để hiển thị avatar tròn
////                        .into(holder.imgAvatar);
////            } else {
////                // Sử dụng avatar mặc định
////                Glide.with(holder.imgAvatar.getContext())
////                        .load(R.drawable.error_image)
////                        .circleCrop()
////                        .into(holder.imgAvatar);
////            }
////        } else {
////            holder.tvUsername.setText("User");
////            // Sử dụng avatar mặc định
////            holder.imgAvatar.setImageResource(R.drawable.error_image);
////        }
////    }
////
////
////    @Override
////    public int getItemCount() {
////        return ratings.size();
////    }
////
////    public void updateRatings(List<Rating> newRatings) {
////        this.ratings = newRatings;
////        notifyDataSetChanged();
////    }
////
////    public void updateUserMap(Map<String, User> newUserMap) {
////        this.userMap = newUserMap;
////        notifyDataSetChanged();
////    }
////
////    static class ReviewViewHolder extends RecyclerView.ViewHolder {
////        ImageView imgAvatar;
////        TextView tvUsername, tvTime, tvComment;
////        RatingBar ratingBar;
////
////        public ReviewViewHolder(@NonNull View itemView) {
////            super(itemView);
////            imgAvatar = itemView.findViewById(R.id.imgUserAvatar);
////            tvUsername = itemView.findViewById(R.id.tvUserName);
////            tvTime = itemView.findViewById(R.id.tvReviewTime);
////            tvComment = itemView.findViewById(R.id.tvReviewComment);
////            ratingBar = itemView.findViewById(R.id.ratingBarReview);
////        }
////    }
////}
//
//
//package com.project.clothingstore.adapter.productdetail;
//
//import android.text.format.DateUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.project.clothingstore.R;
//import com.project.clothingstore.modal.Rating;
//import com.project.clothingstore.modal.User;
////import com.project.clothingstore.utils.TextDrawable;
////import com.project.clothingstore.utils.ColorGenerator;
//
//import java.util.List;
//import java.util.Map;
//
//
//public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
//    private List<Rating> ratings;
//    private Map<String, User> userMap;
//
//    public ReviewAdapter(List<Rating> ratings, Map<String, User> userMap) {
//        this.ratings = ratings;
//        this.userMap = userMap;
//    }
//
//    @NonNull
//    @Override
//    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_review, parent, false);
//        return new ReviewViewHolder(view);
//    }
//
//
////    @Override
////    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
////        Rating rating = ratings.get(position);
////
////        // Set rating và comment
////        holder.ratingBar.setRating(rating.getRate());
////        holder.tvComment.setText(rating.getComment());
////
////        // Set thời gian tương đối - sử dụng phương thức mới
////        long timeInMillis = rating.getCreatedAtMillis();
////        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
////                timeInMillis,
////                System.currentTimeMillis(),
////                DateUtils.MINUTE_IN_MILLIS);
////        holder.tvTime.setText(timeAgo);
////
////        // Set thông tin người dùng nếu có
////        User user = userMap != null ? userMap.get(rating.getUidId()) : null;
////
////        if (user != null) {
////            holder.tvUsername.setText(user.getFullName());
////
////            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
////                Glide.with(holder.imgAvatar.getContext())
////                        .load(user.getAvatar())
////                        .placeholder(R.drawable.error_image)
////                        .error(R.drawable.error_image)
////                        .circleCrop()
////                        .into(holder.imgAvatar);
////            } else {
////                // Tạo avatar với chữ cái đầu của tên người dùng
////                String userName = user.getFullName();
////                String initial = !userName.isEmpty() ? userName.substring(0, 1).toUpperCase() : "U";
////
////            }
////        } else {
////            holder.tvUsername.setText("User");
////            // Sử dụng avatar mặc định
////            holder.imgAvatar.setImageResource(R.drawable.error_image);
////        }
////    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
//        Rating rating = ratings.get(position);
//        holder.ratingBar.setRating(rating.getRate());
//        holder.tvComment.setText(rating.getComment());
//
//        // Cập nhật thông tin người dùng và thời gian
//        User user = userMap.get(rating.getUidId());
//        if (user != null) {
//            holder.tvUsername.setText(user.getFullName());
//            Glide.with(holder.imgAvatar.getContext())
//                    .load(user.getAvatar())
//                    .placeholder(R.drawable.error_image)
//                    .circleCrop()
//                    .into(holder.imgAvatar);
//        } else {
//            holder.tvUsername.setText("User");
//            holder.imgAvatar.setImageResource(R.drawable.error_image);
//        }
//
//        // Cập nhật thời gian
//        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//                rating.getCreatedAtMillis(),
//                System.currentTimeMillis(),
//                DateUtils.MINUTE_IN_MILLIS);
//        holder.tvTime.setText(timeAgo);
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return ratings.size();
//    }
//
//    public void updateRatings(List<Rating> newRatings) {
//        this.ratings = newRatings;
//        notifyDataSetChanged();
//    }
//
//    public void updateUserMap(Map<String, User> newUserMap) {
//        this.userMap = newUserMap;
//        notifyDataSetChanged();
//    }
//
//    static class ReviewViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgAvatar;
//        TextView tvUsername, tvTime, tvComment;
//        RatingBar ratingBar;
//
//        public ReviewViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imgAvatar = itemView.findViewById(R.id.imgUserAvatar);
//            tvUsername = itemView.findViewById(R.id.tvUserName);
//            tvTime = itemView.findViewById(R.id.tvReviewTime);
//            tvComment = itemView.findViewById(R.id.tvReviewComment);
//            ratingBar = itemView.findViewById(R.id.ratingBarReview);
//        }
//    }
//}

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
        holder.ratingBar.setRating(rating.getRate());
        holder.tvComment.setText(rating.getComment());

        // Cập nhật thông tin người dùng
        User user = userMap.get(rating.getUid());
        if (user != null) {
            holder.tvUsername.setText(user.getFullName());
            Glide.with(holder.imgAvatar.getContext())
                    .load(user.getAvatar())
                    .placeholder(R.drawable.error_image)
                    .circleCrop()
                    .into(holder.imgAvatar);
        } else {
            holder.tvUsername.setText("User");
            holder.imgAvatar.setImageResource(R.drawable.error_image);
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
