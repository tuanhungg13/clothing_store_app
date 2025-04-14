package com.project.clothingstore.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.MainActivity;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.OrderItems;
import com.project.clothingstore.modal.Orders;
import com.project.clothingstore.modal.Rating;

import java.util.Arrays;
import java.util.List;

public class RatingActivity extends AppCompatActivity {

    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private EditText etComment;
    private TextView tvCharCount;
    private Button btnSubmitRating;
    private Orders order;

    private int currentRating = 4; // Mặc định là 4 sao như trong hình

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Ánh xạ view
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        etComment = findViewById(R.id.etComment);
        tvCharCount = findViewById(R.id.tvCharCount);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);

        // Xử lý sự kiện click vào các sao
        setupStarRating();

        // Xử lý đếm ký tự
        setupCharacterCounter();

        // Xử lý nút quay lại
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.d("RatingActivity", "Order received: ");

        order = getIntent().getParcelableExtra("orderList");
        if (order == null) {
            Log.d("RatingActivity", "Không tìm thấy thông tin đơn hàng");
//            finish();
//            return;
        }
        Log.d("RatingActivity", "Order received: " + order.getOrderItems().get(0).getPrice());
        // Xử lý nút đánh giá
        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRating();
            }
        });
    }

    private void setupStarRating() {
        final List<ImageView> stars = Arrays.asList(star1, star2, star3, star4, star5);

        // Cập nhật hiển thị sao ban đầu
        updateStarDisplay(currentRating);

        // Thiết lập sự kiện click cho từng sao
        for (int i = 0; i < stars.size(); i++) {
            final int position = i;
            stars.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentRating = position + 1;
                    updateStarDisplay(currentRating);
                }
            });
        }
    }

    private void updateStarDisplay(int rating) {
        List<ImageView> stars = Arrays.asList(star1, star2, star3, star4, star5);

        for (int i = 0; i < stars.size(); i++) {
            if (i < rating) {
                stars.get(i).setImageResource(R.drawable.ic_star_filled);
                stars.get(i).setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.green)));
            } else {
                stars.get(i).setImageResource(R.drawable.ic_star_empty);
                stars.get(i).setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.gray_light)));
            }
        }
    }

    private void setupCharacterCounter() {
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần xử lý
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                tvCharCount.setText(length + " kí tự");
            }
        });
    }

    private void submitRating() {
        String comment = etComment.getText().toString().trim();

        // Kiểm tra dữ liệu
        if (comment.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập cảm nghĩ của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy danh sách OrderItems từ order
        List<OrderItems> orderItems = order.getOrderItems();

        if (orderItems == null || orderItems.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm để đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị loading indicator (nếu cần)
        // showLoading();

        // Đếm số lượng đánh giá đã xử lý để biết khi nào hoàn thành tất cả
        final int[] completedRatings = {0};
        final boolean[] hasError = {false};

        // Lấy uid (bạn đã có sẵn)
        String uid = FirebaseAuth.getInstance().getUid();

        // Tạo một Rating cho mỗi sản phẩm trong đơn hàng
        for (OrderItems item : orderItems) {
            String productId = item.getProductId();
            Rating rating = new Rating(uid, productId, currentRating, comment);

            Log.d("RatingActivity", "Submitting rating for product: " + productId);
            Log.d("RatingActivity", "Rating details: " + rating.toString());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("ratings")
                    .add(rating)
                    .addOnSuccessListener(documentReference -> {
                        // Bỏ hai dòng sau:
                         String ratingId = documentReference.getId();
                         documentReference.update("ratingId", ratingId);

                        completedRatings[0]++;

                        // Kiểm tra xem đã hoàn thành tất cả đánh giá chưa
                        if (completedRatings[0] == orderItems.size() && !hasError[0]) {
                            Toast.makeText(RatingActivity.this, "Cảm ơn bạn đã đánh giá sản phẩm!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        hasError[0] = true;
                        Toast.makeText(RatingActivity.this, "Lỗi khi gửi đánh giá: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
        Intent intent = new Intent(RatingActivity.this, MainActivity.class);
        startActivity(intent);
    }

}

