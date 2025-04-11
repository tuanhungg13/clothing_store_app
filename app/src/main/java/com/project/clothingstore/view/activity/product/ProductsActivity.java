package com.project.clothingstore.view.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.Filter.FilterFragment;
import com.project.clothingstore.view.fragment.Filter.SearchBarFragment;
import com.project.clothingstore.view.fragment.product.ProductFragment;

public class ProductsActivity extends AppCompatActivity implements SearchBarFragment.OnFilterButtonClickListener{
    ImageButton imgbtn_back;
    private String categoryId;

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
//        txt_title = findViewById(R.id.txt_title_products);
        imgbtn_back = findViewById(R.id.btn_back_products);
        drawerLayout = findViewById(R.id.drawer_layout_activity);

        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");

        if (savedInstanceState == null) {
            loadFragment(new ProductFragment(), categoryId);
            loadFragmentSeach(new SearchBarFragment());
            loadFragmentFilter(new FilterFragment());
        }



        // Xử lý sự kiện nhấn nút quay lại
        imgbtn_back.setOnClickListener(v -> {
            finish();
        });

    }
    @Override
    public void onFilterButtonClick() {
        toggleDrawer(); // Gọi mở/đóng drawer từ fragment con
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

    private void loadFragmentSeach(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_search_fliter_activity_products, fragment);
        transaction.commit();
    }

    private void loadFragmentFilter(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_filter_Activity, fragment);
        transaction.commit();
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

}