package com.project.clothingstore.view.fragment.productdetail;

import android.os.Bundle;
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
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;
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
    private boolean isExpanded = true; // Start expanded

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_ratings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
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

        // Get shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

        // Setup RecyclerView
        reviewAdapter = new ReviewAdapter(new ArrayList<>(), null);
        recyclerReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReviews.setAdapter(reviewAdapter);

        // Setup click listeners
        layoutRatings.setOnClickListener(v -> toggleRatingsExpansion());
        imgExpandRatings.setOnClickListener(v -> toggleRatingsExpansion());

        // Observe product data for rating
        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                tvAverageRating.setText(String.format("%.1f", product.getTotalRating()));
                ratingBarSummary.setRating(product.getTotalRating());
            }
        });

        // Observe ratings data
        viewModel.getRatings().observe(getViewLifecycleOwner(), ratings -> {
            if (ratings != null) {
                tvTotalRatings.setText(ratings.size() + " ratings");
                tvReviewCount.setText(ratings.size() + " Reviews");

                // Update adapter with new ratings
                reviewAdapter.updateRatings(ratings);
            }
        });

        // Observe user data for reviews
        viewModel.getUserMap().observe(getViewLifecycleOwner(), userMap -> {
            if (userMap != null) {
                reviewAdapter.updateUserMap(userMap);
            }
        });

        // Observe rating distribution
        viewModel.getRatingDistribution().observe(getViewLifecycleOwner(), this::updateRatingDistribution);

        // Set initial expansion state
        updateExpansionUI();
    }

    private void toggleRatingsExpansion() {
        isExpanded = !isExpanded;
        updateExpansionUI();
    }

    private void updateExpansionUI() {
        recyclerReviews.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        imgExpandRatings.setRotation(isExpanded ? 180 : 0);
    }

    private void updateRatingDistribution(Map<Integer, Integer> distribution) {
        // Update progress bars and percentages
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
