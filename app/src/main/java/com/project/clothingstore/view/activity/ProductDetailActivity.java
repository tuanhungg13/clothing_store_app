package com.project.clothingstore.view.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.productdetail.ProductDescriptionFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductImagesFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductInfoFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductRatingsFragment;
import com.project.clothingstore.view.fragment.productdetail.SimilarProductsFragment;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

public class ProductDetailActivity extends AppCompatActivity {
    private ProductDetailViewModel viewModel;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Lấy productId từ intent
        productId = getIntent().getStringExtra("PRODUCT_ID");

//        if (productId == null || productId.isEmpty()) {
//            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }

        // Thiết lập toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Nút quay lại
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        // Tải dữ liệu sản phẩm
        viewModel.loadProductDetails(productId);

        // Nút thêm vào giỏ hàng
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(v -> addToCart());

        // Load các Fragment
        loadFragments();
    }

    private void loadFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.productImagesContainer, new ProductImagesFragment());
        transaction.replace(R.id.productInfoContainer, new ProductInfoFragment());
        transaction.replace(R.id.productDescriptionContainer, new ProductDescriptionFragment());
        transaction.replace(R.id.productRatingsContainer, new ProductRatingsFragment());
        transaction.replace(R.id.similarProductsContainer, new SimilarProductsFragment());

        transaction.commit();
    }

    private void addToCart() {
        if (viewModel.getProduct().getValue() == null) {
            Toast.makeText(this, "Sản phẩm không tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedColor = viewModel.getSelectedColor().getValue();
        String selectedSize = viewModel.getSelectedSize().getValue();

        String message = "Added to cart: " +
                viewModel.getProduct().getValue().getProductName() +
                " - Color: " + (selectedColor != null ? selectedColor : "N/A") +
                ", Size: " + (selectedSize != null ? selectedSize : "N/A");

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
