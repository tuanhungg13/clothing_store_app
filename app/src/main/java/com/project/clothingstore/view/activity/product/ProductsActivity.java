package com.project.clothingstore.view.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.product.ProductFragment;

public class ProductsActivity extends AppCompatActivity {
    ImageButton imgbtn_back;
    TextView txt_title;
    private String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        txt_title = findViewById(R.id.txt_title_products);
        imgbtn_back = findViewById(R.id.btn_back_products);

        categoryId = getIntent().getStringExtra("categoryId");
        Intent intent = getIntent();
        String title = intent.getStringExtra("categoryName");
        txt_title.setText(title);


        if (savedInstanceState == null) {
            loadFragment(new ProductFragment(), categoryId);
        }



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



}