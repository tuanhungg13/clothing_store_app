package com.project.clothingstore.view.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.project.clothingstore.MainActivity;
import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.Filter.FilterFragment;
import com.project.clothingstore.view.fragment.Filter.SearchBarFragment;
import com.project.clothingstore.view.fragment.product.ProductFragment;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity implements SearchBarFragment.OnFilterButtonClickListener{
    ImageButton imgbtn_back;
    private String categoryId, productName;
    int categoriType;
    int minPrice, maxPrice;
    double rating;
    ArrayList<Integer> discountList;

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

        productName = intent.getStringExtra("productName");

        categoriType = intent.getIntExtra("categoriType", -1);
        String minPriceStr = intent.getStringExtra("minPrice");
        String maxPriceStr = intent.getStringExtra("maxPrice");
        if (minPriceStr != null && !minPriceStr.isEmpty()) {
            try {
                minPrice = Integer.parseInt(minPriceStr);
            } catch (NumberFormatException e) {
                minPrice = -1; // Giá trị mặc định nếu không thể chuyển đổi
            }
        } else {
            minPrice = -1; // Giá trị mặc định nếu không có dữ liệu
        }
        if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
            try {
                maxPrice = Integer.parseInt(maxPriceStr);
            } catch (NumberFormatException e) {
                maxPrice = -1; // Giá trị mặc định nếu không thể chuyển đổi
            }
        } else {
            maxPrice = -1; // Giá trị mặc định nếu không có dữ liệu
        }
        rating = intent.getIntExtra("rating", -1); // Nếu không có thì là -1
        discountList = intent.getIntegerArrayListExtra("discountList");



        if (savedInstanceState == null) {
            loadFragmentProduct(new ProductFragment(), categoryId);
            loadFragmentSeach(new SearchBarFragment());
            loadFragmentFilter(new FilterFragment());
        }



        // Xử lý sự kiện nhấn nút quay lại
        // Xử lý sự kiện nhấn nút quay lại
        imgbtn_back.setOnClickListener(v -> {
            Intent intentT = new Intent(ProductsActivity.this, MainActivity.class);
            intentT.putExtra("openSearchFragment", true); // Thêm thông báo mở SearchFragment
            startActivity(intentT);
            finish(); // Đóng ProductsActivity
        });


    }
    @Override
    public void onFilterButtonClick() {
        toggleDrawer(); // Gọi mở/đóng drawer từ fragment con
    }

    private void loadFragmentProduct(Fragment fragment, String cateId) {

        // Truyền dữ liệu từ activity sang fragment
        Bundle bundle  = new Bundle();
        bundle.putString("categoryId", cateId);
        bundle.putString("productName", productName);
        bundle.putInt("categoriType", categoriType);
        bundle.putInt("minPrice", minPrice);
        bundle.putInt("maxPrice", maxPrice);
        bundle.putDouble("rating", rating);
        bundle.putIntegerArrayList("discountList", discountList);
        // Gán bundle cho fragment
        fragment.setArguments(bundle);
        // Thực hiện giao dịch fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_product_items, fragment);
        transaction.commit();
    }

    private void loadFragmentSeach(Fragment fragment) {
        Bundle bundle  = new Bundle();
        bundle.putBoolean("isSearch", true);
        bundle.putString("productName", productName);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_search_fliter_activity_products, fragment);
        transaction.commit();
    }

    private void loadFragmentFilter(Fragment fragment) {
        // Truyền dữ liệu từ activity sang fragment
        Bundle bundle  = new Bundle();
        bundle.putBoolean("isFromApply", true);
        bundle.putString("categoryId", categoryId);
        bundle.putString("productName", productName);
        bundle.putInt("categoriType", categoriType);
        bundle.putInt("minPrice", minPrice);
        bundle.putInt("maxPrice", maxPrice);
        bundle.putDouble("rating", rating);
        bundle.putIntegerArrayList("discountList", discountList);
        // Gán bundle cho fragment
        fragment.setArguments(bundle);
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