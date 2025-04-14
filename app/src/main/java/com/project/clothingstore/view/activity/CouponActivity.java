package com.project.clothingstore.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.clothingstore.R;
import com.project.clothingstore.adapter.CouponAdapter;
import com.project.clothingstore.viewmodel.CouponViewModel;
import com.project.clothingstore.viewmodel.UserViewModel;

import java.util.ArrayList;

public class CouponActivity extends AppCompatActivity {
    private RecyclerView rcvCoupon;
    private CouponAdapter adapter;
    private CouponViewModel couponViewModel;
    private UserViewModel userViewModel;
    private ImageView btnback;
    private TextView tvEmptyCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_coupon);

        rcvCoupon = findViewById(R.id.rcv_coupon);
        rcvCoupon.setLayoutManager(new LinearLayoutManager(this));
        btnback = findViewById(R.id.btn_back);
        tvEmptyCoupon = findViewById(R.id.tv_empty_coupon);

        adapter = new CouponAdapter(this, new ArrayList<>());
        rcvCoupon.setAdapter(adapter);

        couponViewModel = new ViewModelProvider(this).get(CouponViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Lấy thông tin user hiện tại
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userViewModel.fetchUserInfo(firebaseUser.getUid());
        }

        // Quan sát dữ liệu người dùng để lấy coupon
        userViewModel.getCurrentUser().observe(this, user -> {
            if (user != null && user.getCoupons() != null && !user.getCoupons().isEmpty()) {
                couponViewModel.fetchCouponsByIds(user.getUid(),user.getCoupons());
            } else {
                tvEmptyCoupon.setVisibility(View.VISIBLE);
                rcvCoupon.setVisibility(View.GONE);
            }
        });

        // Quan sát danh sách coupon
        couponViewModel.getCouponListLiveData().observe(this, coupons -> {
            if (coupons != null && !coupons.isEmpty()) {
                adapter.setData(coupons);
                tvEmptyCoupon.setVisibility(View.GONE);
                rcvCoupon.setVisibility(View.VISIBLE);
            } else {
                tvEmptyCoupon.setVisibility(View.VISIBLE);
                rcvCoupon.setVisibility(View.GONE);
            }
        });

        btnback.setOnClickListener(v -> finish());
    }
}
