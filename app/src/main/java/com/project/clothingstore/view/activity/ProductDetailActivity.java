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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.R;
import com.project.clothingstore.modal.CartItem;
import com.project.clothingstore.modal.Carts;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.service.CartRepository;
import com.project.clothingstore.view.fragment.productdetail.ProductDescriptionFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductImagesFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductInfoFragment;
import com.project.clothingstore.view.fragment.productdetail.ProductRatingsFragment;
import com.project.clothingstore.view.fragment.productdetail.SimilarProductsFragment;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;
import com.project.clothingstore.viewmodel.UserViewModel;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private ProductDetailViewModel viewModel;
    private String productId;
    private UserViewModel userViewModel;

    private String cartId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        // Nhận productId từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");
            if (productId != null) {
                // Tải dữ liệu sản phẩm
                viewModel.loadProductDetails(productId);
            }
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid != null) {
            userViewModel.fetchUserInfo(uid);
        }
        else {

        }

        userViewModel.getCurrentUser(). observe(this, user -> {
            if (user != null) {
                cartId = user.getCartId();
                Log.d("ProductDetail", "Cart ID: " + cartId);
            }
        });

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
        Log.d("ProductDetail", "Bắt đầu thêm sản phẩm vào giỏ hàng");

        Product product = viewModel.getProduct().getValue();
        String selectedColor = viewModel.getSelectedColor().getValue();
        String selectedSize = viewModel.getSelectedSize().getValue();
        product.setProductId(productId);
        if (product == null) {
            Log.e("ProductDetail", "Sản phẩm không tồn tại");
            Toast.makeText(this, "Sản phẩm không tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("ProductDetail", "Sản phẩm: " + product.getProductName());
        Log.d("ProductDetail", "Màu đã chọn: " + selectedColor);
        Log.d("ProductDetail", "Size đã chọn: " + selectedSize);

        if (selectedColor == null || selectedSize == null) {
            Log.e("ProductDetail", "Chưa chọn màu hoặc size");
            Toast.makeText(this, "Vui lòng chọn màu sắc và kích cỡ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem sản phẩm có sẵn với màu và kích cỡ đã chọn hay không
        boolean productAvailable = false;
        int availableQuantity = 0;


        List<Product.Variant> variants = product.getVariants();
        if (variants != null) {
            Log.d("ProductDetail", "Số lượng variant: " + variants.size());

            for (Product.Variant variant : variants) {
                if (variant.getColor().equals(selectedColor)) {
                    Log.d("ProductDetail", "Tìm thấy variant với màu: " + selectedColor);

                    List<Product.Variant.SizeQuantity> sizes = variant.getSizes();
                    if (sizes != null) {
                        Log.d("ProductDetail", "Số lượng size: " + sizes.size());

                        for (Product.Variant.SizeQuantity sizeQty : sizes) {
                            if (sizeQty.getSize().equals(selectedSize)) {
                                Log.d("ProductDetail", "Tìm thấy size: " + selectedSize + " với số lượng: " + sizeQty.getQuantity());

                                if (sizeQty.getQuantity() > 0) {
                                    productAvailable = true;
                                    availableQuantity = sizeQty.getQuantity();
                                    Log.d("ProductDetail", "Sản phẩm có sẵn, số lượng: " + availableQuantity);
                                } else {
                                    Log.d("ProductDetail", "Sản phẩm hết hàng với size này");
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (!productAvailable) {
            Log.e("ProductDetail", "Sản phẩm không có sẵn với màu và size đã chọn");
            Toast.makeText(this, "Sản phẩm đã hết hàng với màu sắc và kích cỡ đã chọn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng variant cho cartItem
        Carts.cartItem.variant itemVariant = new Carts.cartItem.variant(selectedColor, selectedSize);
        Log.d("ProductDetail", "Đã tạo variant cho cart item");

        // Tạo đối tượng cartItem
        Carts.cartItem item = new Carts.cartItem(
                product.getProductId(),
                product.getProductName(),
                product.getImages().get(0), // Lấy ảnh đầu tiên làm ảnh đại diện
                1, // Số lượng mặc định là 1
                product.getPrice(),
                itemVariant
        );

        Log.d("ProductDetail asd", "Product ID : " + product.getProductId());
        CartItem cartItem = new CartItem(
                product.getProductId(),
                product.getProductName(),
                product.getImages().get(0), // Lấy ảnh đầu tiên làm ảnh đại diện
                1, // Số lượng mặc định là 1
                product.getPrice(),
                itemVariant
        );

        Log.d("ProductDetail xxx", "Đã tạo cart item: " + item.getProductName() + ", màu: " + item.getVariant().getColor() + ", size: " + item.getVariant().getSize());
        Log.d("ProductDetail", "Đã tạo cart item: " + item.getProductName() + ", giá: " + item.getPrice());

        // Thêm vào giỏ hàng
        Log.d("ProductDetail", "Bắt đầu gọi CartRepository.addToCart()");
        CartRepository cartRepository = new CartRepository();

        // Kiểm tra cartId đã được lấy chưa
        if (cartId == null || cartId.isEmpty()) {
            Log.e("ProductDetail", "cartId chưa được khởi tạo");
            Toast.makeText(this, "Không thể thêm vào giỏ hàng. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
            return;
        }

        cartRepository.addToCart(cartId, cartItem, new CartRepository.CartOperationCallback() {

            @Override
            public void onSuccess() {
                // Hiển thị thông báo thành công
                Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                // Xử lý lỗi
                Toast.makeText(ProductDetailActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CartRepository", "Error adding to cart", e);
            }
        });

    }



}
