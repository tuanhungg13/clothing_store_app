package com.project.clothingstore.view.fragment.productdetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.productdetail.ReviewAdapter;
import com.project.clothingstore.modal.Rating;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductRatingsFragment extends Fragment {
    private ProductDetailViewModel viewModel;
    private TextView tvAverageRating, tvTotalRatings, tvReviewCount;
    private TextView tvPercentage1, tvPercentage2, tvPercentage3, tvPercentage4, tvPercentage5;
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4, progressBar5;
    private RatingBar ratingBarSummary;
    private RecyclerView recyclerReviews;
    private ReviewAdapter reviewAdapter;
    private ImageView imgExpandRatings;
    private LinearLayout layoutRatings;
    private boolean isExpanded = true; // Bắt đầu với trạng thái mở rộng

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_ratings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo views
        tvAverageRating = view.findViewById(R.id.tvAverageRating);
        tvTotalRatings = view.findViewById(R.id.tvTotalRatings);
        tvReviewCount = view.findViewById(R.id.tvReviewCount);
        ratingBarSummary = view.findViewById(R.id.ratingBarSummary);

        progressBar1 = view.findViewById(R.id.progressBar1);
        progressBar2 = view.findViewById(R.id.progressBar2);
        progressBar3 = view.findViewById(R.id.progressBar3);
        progressBar4 = view.findViewById(R.id.progressBar4);
        progressBar5 = view.findViewById(R.id.progressBar5);

        tvPercentage1 = view.findViewById(R.id.tvPercentage1);
        tvPercentage2 = view.findViewById(R.id.tvPercentage2);
        tvPercentage3 = view.findViewById(R.id.tvPercentage3);
        tvPercentage4 = view.findViewById(R.id.tvPercentage4);
        tvPercentage5 = view.findViewById(R.id.tvPercentage5);

        recyclerReviews = view.findViewById(R.id.recyclerReviews);
        imgExpandRatings = view.findViewById(R.id.imgExpandRatings);
        layoutRatings = view.findViewById(R.id.layoutRatings);
//        shimmerLayout = view.findViewById(R.id.shimmerLayout);
//        tvNoReviews = view.findViewById(R.id.tvNoReviews);

        // Hiển thị trạng thái loading
//        showLoadingState(true);

        // Lấy ViewModel được chia sẻ từ Activity
        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

        // Thiết lập RecyclerView
        reviewAdapter = new ReviewAdapter(new ArrayList<>(), new HashMap<>());
        recyclerReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReviews.setAdapter(reviewAdapter);

        // Thiết lập click listeners
        layoutRatings.setOnClickListener(v -> toggleRatingsExpansion());
        imgExpandRatings.setOnClickListener(v -> toggleRatingsExpansion());

        // Quan sát dữ liệu sản phẩm để hiển thị rating trung bình
        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                Log.d("ProductRatingsFragment", "Product rating: " + product.getTotalRating());
                tvAverageRating.setText(String.format("%.1f", product.getTotalRating()));
                ratingBarSummary.setRating(product.getTotalRating());
            }
        });

        // Quan sát dữ liệu đánh giá
//        viewModel.getRatings().observe(getViewLifecycleOwner(), ratings -> {
////            showLoadingState(false);
//
//            if (ratings == null || ratings.isEmpty()) {
//                // Hiển thị thông báo "Chưa có đánh giá nào"
//                tvAverageRating.setText("0.0");
//                ratingBarSummary.setRating(0);
//                tvTotalRatings.setText("0 ratings");
//                tvReviewCount.setText("0 Reviews");
////                tvNoReviews.setVisibility(View.VISIBLE);
//                recyclerReviews.setVisibility(View.GONE);
//
//                // Thiết lập tất cả progress bar về 0
//                resetRatingBars();
//            } else {
//                tvTotalRatings.setText(ratings.size() + " ratings");
//                tvReviewCount.setText(ratings.size() + " Reviews");
////                tvNoReviews.setVisibility(View.GONE);
//                recyclerReviews.setVisibility(View.VISIBLE);
//                reviewAdapter.updateRatings(ratings);
//            }
//        });

        viewModel.getRatings().observe(getViewLifecycleOwner(), ratings -> {
            if (ratings == null || ratings.isEmpty()) {
                tvAverageRating.setText("0.0");
                ratingBarSummary.setRating(0);
                tvTotalRatings.setText("0 ratings");
                tvReviewCount.setText("0 Reviews");
                recyclerReviews.setVisibility(View.GONE);
                resetRatingBars();
            } else {
                tvTotalRatings.setText(ratings.size() + " ratings");
                tvReviewCount.setText(ratings.size() + " Reviews");
                recyclerReviews.setVisibility(View.VISIBLE);
                reviewAdapter.updateRatings(ratings);
            }
        });

        // Quan sát dữ liệu người dùng
        viewModel.getUserMap().observe(getViewLifecycleOwner(), userMap -> {
            if (userMap != null) {
                Log.d("ProductRatingsFragment", "Received user map with " + userMap.size() + " users");
                reviewAdapter.updateUserMap(userMap); // Cập nhật userMap trong adapter
            }
        });

        // Quan sát phân phối đánh giá
        viewModel.getRatingDistribution().observe(getViewLifecycleOwner(), this::updateRatingDistribution);

        // Thiết lập trạng thái mở rộng ban đầu
        updateExpansionUI();
    }

    private void resetRatingBars() {
        progressBar1.setProgress(0);
        progressBar2.setProgress(0);
        progressBar3.setProgress(0);
        progressBar4.setProgress(0);
        progressBar5.setProgress(0);

        tvPercentage1.setText("0%");
        tvPercentage2.setText("0%");
        tvPercentage3.setText("0%");
        tvPercentage4.setText("0%");
        tvPercentage5.setText("0%");
    }

    private void toggleRatingsExpansion() {
        isExpanded = !isExpanded;
        updateExpansionUI();
    }

    private void updateExpansionUI() {
        // Sử dụng animation thay vì thay đổi visibility trực tiếp
        if (isExpanded) {
            recyclerReviews.setVisibility(View.VISIBLE);
            imgExpandRatings.animate().rotation(180).setDuration(300).start();
        } else {
            recyclerReviews.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction(() -> {
                        recyclerReviews.setVisibility(View.GONE);
                        recyclerReviews.setAlpha(1f);
                    });
            imgExpandRatings.animate().rotation(0).setDuration(300).start();
        }
    }

//    private void showLoadingState(boolean isLoading) {
//        if (isLoading) {
//            shimmerLayout.setVisibility(View.VISIBLE);
//            shimmerLayout.startShimmer();
//            recyclerReviews.setVisibility(View.GONE);
//            tvNoReviews.setVisibility(View.GONE);
//        } else {
//            shimmerLayout.stopShimmer();
//            shimmerLayout.setVisibility(View.GONE);
//        }
//    }

    private void updateRatingDistribution(Map<Integer, Integer> distribution) {
        Log.d("ProductRatingsFragment", "Rating distribution: " + distribution);
        // Cập nhật progress bars và phần trăm
        progressBar1.setProgress(distribution.get(1));
        progressBar2.setProgress(distribution.get(2));
        progressBar3.setProgress(distribution.get(3));
        progressBar4.setProgress(distribution.get(4));
        progressBar5.setProgress(distribution.get(5));

        tvPercentage1.setText(distribution.get(1) + "%");
        tvPercentage2.setText(distribution.get(2) + "%");
        tvPercentage3.setText(distribution.get(3) + "%");
        tvPercentage4.setText(distribution.get(4) + "%");
        tvPercentage5.setText(distribution.get(5) + "%");
    }
}
