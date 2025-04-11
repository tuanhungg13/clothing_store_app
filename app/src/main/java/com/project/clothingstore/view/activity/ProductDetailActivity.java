package com.project.clothingstore.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.project.clothingstore.R;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.view.fragment.productdetail.ProductDescriptionFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductImagesFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductInfoFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductRatingsFragment;
import com.project.clothingstore.view.fragment.productdetail.SimilarProductsFragment;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private ProductDetailViewModel viewModel;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_detail);
//
//        // Lấy productId từ intent
//        productId = getIntent().getStringExtra("productId");
//
//        Log.d("FeatureProductAdapter", "Clicked on product: " + productId); // Log kiểm tra
//
//
//        if (productId == null || productId.isEmpty()) {
//            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Thiết lập toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }
//
//        // Nút quay lại
//        ImageButton btnBack = findViewById(R.id.btnBack);
//        btnBack.setOnClickListener(v -> onBackPressed());
//
//        // Khởi tạo ViewModel
//        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
//
//        // Tải dữ liệu sản phẩm
//        viewModel.loadProductDetails(productId);
//
//        // Nút thêm vào giỏ hàng
//        Button btnAddToCart = findViewById(R.id.btnAddToCart);
//        btnAddToCart.setOnClickListener(v -> addToCart());
//
//        // Load các Fragment
//        loadFragments();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        // Nhận productId từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");
            if (productId != null) {
                viewModel.loadProductDetails(productId);
            }
        }

        Log.d("FeatureProductAdapter", "Clicked on product: " + productId); // Log kiểm tra



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
//        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        // Tải dữ liệu sản phẩm
//        viewModel.loadProductDetails(productId);

        // Nút thêm vào giỏ hàng
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(v -> addToCart());

        // Load các Fragment
        loadFragments();


        // Quan sát dữ liệu từ ViewModel để cập nhật giao diện
        viewModel.getProduct().observe(this, product -> {
            if (product != null) {
                loadFragments(product);
            }
        });


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

    private void loadFragments(Product product) {
        // Load từng Fragment với dữ liệu tương ứng
        loadFragment(R.id.productImagesContainer, new ProductImagesFragment(), product.getImages().get(0));
        loadFragment(R.id.productInfoContainer, new ProductInfoFragment(), product.getProductName());
        loadFragment(R.id.productDescriptionContainer, new ProductDescriptionFragment(), product.getDescription());
        loadFragment(R.id.productRatingsContainer, new ProductRatingsFragment(), productId);
        loadFragment(R.id.similarProductsContainer, new SimilarProductsFragment(), product.getCategoryId());
    }

    private void loadFragment(int containerId, Fragment fragment, String data) {
        // Truyền dữ liệu từ activity sang fragment
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        fragment.setArguments(bundle);

        // Thực hiện giao dịch fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }



    private void addToCart() {
        Product product = viewModel.getProduct().getValue();
        String selectedColor = viewModel.getSelectedColor().getValue();
        String selectedSize = viewModel.getSelectedSize().getValue();

        if (product == null) {
            Toast.makeText(this, "Sản phẩm không tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedColor == null || selectedSize == null) {
            Toast.makeText(this, "Vui lòng chọn màu sắc và kích cỡ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem sản phẩm có sẵn với màu và kích cỡ đã chọn hay không
        boolean productAvailable = false;
        int availableQuantity = 0;

        List<Product.Variant> variants = product.getVariants();
        if (variants != null) {
            for (Product.Variant variant : variants) {
                if (variant.getColor().equals(selectedColor)) {
                    List<Product.Variant.SizeQuantity> sizes = variant.getSizes();
                    if (sizes != null) {
                        for (Product.Variant.SizeQuantity sizeQty : sizes) {
                            if (sizeQty.getSize().equals(selectedSize) && sizeQty.getQuantity() > 0) {
                                productAvailable = true;
                                availableQuantity = sizeQty.getQuantity();
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (!productAvailable) {
            Toast.makeText(this, "Sản phẩm đã hết hàng với màu sắc và kích cỡ đã chọn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm vào giỏ hàng (giả định)
        String message = "Đã thêm vào giỏ hàng: " +
                product.getProductName() +
                " - Màu: " + selectedColor +
                ", Size: " + selectedSize;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Ở đây bạn sẽ thêm logic để thêm sản phẩm vào giỏ hàng thực tế
        // cartRepository.addToCart(productId, selectedColor, selectedSize, 1);
    }
}
