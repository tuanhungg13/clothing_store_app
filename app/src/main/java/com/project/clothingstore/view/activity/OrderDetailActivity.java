package com.project.clothingstore.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.clothingstore.MainActivity;
import com.project.clothingstore.R;
import com.project.clothingstore.adapter.Order.OrderItemAdapter;
import com.project.clothingstore.modal.OrderItems;
import com.project.clothingstore.modal.Orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderAdress, tvTotalPrice, tvShippingFee, tvSubTotalPrice;
    private ImageView btnBack;
    private TextView tvOrderStatus, tvOrderSubStatus;
    private ImageView imvOrderStatus;
    private Button btnShopping, btnRating;
    private RecyclerView rvOrderItems;
    private OrderItemAdapter itemAdapter;
    private Orders order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail); // Đảm bảo file layout có đủ các view bên dưới

        // Ánh xạ view
        tvOrderId = findViewById(R.id.tvOrderId_Detail);
        tvOrderAdress = findViewById(R.id.tvOrderAdress_Detail);
        tvSubTotalPrice = findViewById(R.id.tvSubTotalPrice_Detail);
        tvShippingFee = findViewById(R.id.tvShippingFee_Detail);
        tvTotalPrice = findViewById(R.id.tvTotalPrice_Detail);
        rvOrderItems = findViewById(R.id.rvOrderProducts);

        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        imvOrderStatus = findViewById(R.id.imvOrderStatus);
        tvOrderSubStatus = findViewById(R.id.tvOrderSubStatus);

        // Nhận dữ liệu
        order = getIntent().getParcelableExtra("order");

        // Ánh xạ nút back
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại activity trước đó
            }
        });

        btnShopping = findViewById(R.id.btnHomePage);
//        btnShopping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OrderDetailActivity.this, RatingActivity.class);
//                intent.putExtra("orderList", order);
//
//                startActivity(intent);
//            }
//        });

        // Check xem đã cmt vào sản phẩm chưa
        btnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thay thế nội dung hiện tại bằng đoạn code sau
                String uid = FirebaseAuth.getInstance().getUid();

                getUnratedProducts(order.getOrderItems(), uid, unratedItems -> {
                    if (unratedItems.isEmpty()) {
                        Toast.makeText(OrderDetailActivity.this,
                                "Bạn đã đánh giá tất cả sản phẩm trong đơn hàng này", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tạo một bản sao của order chỉ với các sản phẩm chưa đánh giá
                        Orders unratedOrder = new Orders(order);
                        unratedOrder.setOrderItems(unratedItems);

                        Intent intent = new Intent(OrderDetailActivity.this, RatingActivity.class);
                        intent.putExtra("orderList", unratedOrder);
                        startActivity(intent);
                    }
                });
            }
        });
        // - end

        btnRating = findViewById(R.id.btnRating);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        if (order != null) {

            if(order.getStatus().equals("PENDING")){
                tvOrderStatus.setText("Đơn hàng của bạn đang \nđược giao");
                imvOrderStatus.setImageResource(R.drawable.dmq_ic_delivery_truck);
                tvOrderSubStatus.setVisibility(View.GONE);
                btnShopping.setVisibility(View.GONE);
                btnRating.setText("Tiếp tục mua sắm");
                btnRating.setVisibility(View.VISIBLE);
            } else if (order.getStatus().equals("CANCEL")) {
                tvOrderStatus.setText("Đơn hàng của bạn \nđã bị hủy");
                imvOrderStatus.setImageResource(R.drawable.ic_order_cancelled);
                tvOrderSubStatus.setVisibility(View.GONE);
                btnShopping.setVisibility(View.GONE);
                btnRating.setText("Tiếp tục mua sắm");
                btnRating.setVisibility(View.VISIBLE);

            }
            else if (order.getStatus().equals("SUCCESS")) {
                tvOrderStatus.setText("Đơn hàng của bạn đã \nđược giao thành công");
                imvOrderStatus.setImageResource(R.drawable.ic_hand_box);
                tvOrderSubStatus.setVisibility(View.VISIBLE);
                btnShopping.setVisibility(View.VISIBLE);
                btnShopping.setText("Đánh giá");
                btnRating.setText("Tiếp tục mua sắm");
                btnRating.setVisibility(View.VISIBLE);

            }
            tvOrderId.setText("#" + order.getOrderId());
            tvOrderAdress.setText("" + order.getAddress());
            tvSubTotalPrice.setText("" + order.calculateSubPrice());
            tvShippingFee.setText("" + order.getShippingPrice());
            tvTotalPrice.setText("" + order.calculateTotalPrice());


            // Setup adapter hiển thị danh sách sản phẩm
            itemAdapter = new OrderItemAdapter(this, order.getOrderItems());
            rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
            rvOrderItems.setAdapter(itemAdapter);
        }
    }

    // Check xem đã cmt vào sản phẩm chưa
    private void getUnratedProducts(List<OrderItems> orderItems, String uid, OnUnratedProductsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final int[] checkedProducts = {0};
        List<OrderItems> unratedItems = new ArrayList<>();

        for (OrderItems item : orderItems) {
            String productId = item.getProductId();

            db.collection("ratings")
                    .whereEqualTo("uid", uid)
                    .whereEqualTo("productId", productId)
                    .get()
                    .addOnCompleteListener(task -> {
                        checkedProducts[0]++;

                        if (task.isSuccessful() && task.getResult().isEmpty()) {
                            // Sản phẩm chưa được đánh giá
                            unratedItems.add(item);
                        }

                        if (checkedProducts[0] == orderItems.size()) {
                            listener.onUnratedProductsFound(unratedItems);
                        }
                    });
        }
    }

    interface OnUnratedProductsListener {
        void onUnratedProductsFound(List<OrderItems> unratedItems);
    }

    // - end
}

