package com.project.clothingstore.view.activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.product.ProductFragment;

public class ProductsActivity extends AppCompatActivity {
    ImageButton imgbtn_ao, imgbtn_phukien, imgbtn_giay, imgbtn_lamdep, imgbtn_back;
    private String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        imgbtn_ao = findViewById(R.id.imgbtn_ao);
        imgbtn_phukien = findViewById(R.id.img_btn_phukien);
        imgbtn_giay = findViewById(R.id.img_btn_giay);
        imgbtn_lamdep = findViewById(R.id.img_btn_lamdep);
        imgbtn_back = findViewById(R.id.btn_back_products);

        categoryId = getIntent().getStringExtra("categoryId");
        // Thiết lập giao diện cho BottomNavigationView
        if (savedInstanceState == null) {
            loadFragment(new ProductFragment(), categoryId);
            updateImageButtonBackground(Integer.parseInt(categoryId));
        }


        imgbtn_ao.setOnClickListener(v -> {
            loadFragment(new ProductFragment(), "0");
            updateImageButtonBackground(0);
        });
        imgbtn_giay.setOnClickListener(v -> {
            loadFragment(new ProductFragment(), "1");
            updateImageButtonBackground(1);
        });
        imgbtn_phukien.setOnClickListener(v -> {
            loadFragment(new ProductFragment(), "2");
            updateImageButtonBackground(2);
        });
        imgbtn_lamdep.setOnClickListener(v -> {
            loadFragment(new ProductFragment(), "3");
            updateImageButtonBackground(3);
        });

        // Xử lý sự kiện nhấn nút quay lại
        imgbtn_back.setOnClickListener(v -> {
            finish();
        });

    }

    private void loadFragment(Fragment fragment, String cateId) {

        // Truyền dữ liệu từ activity sang fragment
        Bundle bundle  = new Bundle();
        bundle.putString("categoryId", cateId);
        fragment.setArguments(bundle);
        // Thực hiện giao dịch fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_product_items, fragment);
        transaction.commit();
    }


    private void updateImageButtonBackground(int index) {
        // Reset tất cả màu nền của các ImageButton
        imgbtn_ao.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        imgbtn_phukien.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        imgbtn_giay.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        imgbtn_lamdep.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        // Cập nhật màu nền cho ImageButton tương ứng với index
        switch (index) {
            case 0:
                imgbtn_ao.setBackgroundColor(getResources().getColor(R.color.img));  // Màu bạn muốn
                break;
            case 1:
                imgbtn_giay.setBackgroundColor(getResources().getColor(R.color.img));
                break;
            case 2:
                imgbtn_phukien.setBackgroundColor(getResources().getColor(R.color.img));
                break;
            case 3:
                imgbtn_lamdep.setBackgroundColor(getResources().getColor(R.color.img));
                break;
        }
    }
}