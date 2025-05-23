
package com.project.clothingstore.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.clothingstore.R;
import com.project.clothingstore.adapter.Order.OrderAdapter;
import com.project.clothingstore.modal.Orders;
import com.project.clothingstore.viewmodel.OrderViewModel;

import java.util.ArrayList;

//public class OrderFragment extends Fragment {
//
//    private OrderViewModel viewModel;
//    private RecyclerView recyclerView;
//    private OrderAdapter adapter;
//    private Button btnPending, btnCompleted, btnCancelled;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_order, container, false);
//
//        recyclerView = view.findViewById(R.id.rvOrders);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        adapter = new OrderAdapter(new ArrayList<>(), order -> {
//            showOrderDetails(order);
//        });
//        recyclerView.setAdapter(adapter);
//
//        // Khởi tạo ViewModel
//        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
//
//        // Lấy UID của người dùng đăng nhập
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Mặc định tải danh sách đơn hàng có trạng thái "PENDING"
//        viewModel.loadOrdersByUserIdAndStatus(uid, "PENDING");
//
//        // Quan sát LiveData và cập nhật RecyclerView khi có dữ liệu mới
//        viewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orders -> {
//            if (orders != null) {
//                adapter.setOrders(orders);
//            }
//        });
//
//        // Ánh xạ các button trong layout
//        btnPending = view.findViewById(R.id.btnPending);
//        btnCompleted = view.findViewById(R.id.btnCompleted);
//        btnCancelled = view.findViewById(R.id.btnCancelled);
//
//        // Sự kiện cho các button để lọc đơn hàng theo trạng thái
//        btnPending.setOnClickListener(v -> filterOrders("PENDING"));
//        btnCompleted.setOnClickListener(v -> filterOrders("SUCCESS"));
//        btnCancelled.setOnClickListener(v -> filterOrders("CANCEL"));
//
//        btnPending.setSelected(true); // Mặc định chọn PENDING khi khởi tạo
//
//        btnPending.setOnClickListener(v -> {
//            filterOrders("PENDING");
//            btnPending.setSelected(true); // Đánh dấu nút PENDING là đã chọn
//            btnCompleted.setSelected(false); // Bỏ chọn các nút khác
//            btnCancelled.setSelected(false);
//        });
//
//        btnCompleted.setOnClickListener(v -> {
//            filterOrders("SUCCESS");
//            btnPending.setSelected(false); // Bỏ chọn PENDING
//            btnCompleted.setSelected(true); // Đánh dấu nút COMPLETED là đã chọn
//            btnCancelled.setSelected(false);
//        });
//
//        btnCancelled.setOnClickListener(v -> {
//            filterOrders("CANCEL");
//            btnPending.setSelected(false); // Bỏ chọn PENDING
//            btnCompleted.setSelected(false); // Bỏ chọn COMPLETED
//            btnCancelled.setSelected(true); // Đánh dấu nút CANCEL là đã chọn
//        });
//
//        return view;
//    }
//
//    private void filterOrders(String status) {
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        viewModel.loadOrdersByUserIdAndStatus(uid, status);
//    }
//
//    private void showOrderDetails(Orders order) {
//        // TODO: Hiển thị chi tiết đơn hàng (có thể mở một fragment hoặc activity mới)
//    }
//}

//public class OrderFragment extends Fragment {
//
//    private OrderViewModel viewModel;
//    private OrderAdapter adapter;
//    private Button btnPending, btnCompleted, btnCancelled;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_order, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
//        adapter = new OrderAdapter();
//
//        RecyclerView recyclerView = view.findViewById(R.id.rvOrders);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        recyclerView.setAdapter(adapter);
//
//        btnPending = view.findViewById(R.id.btnPending);
//        btnCompleted = view.findViewById(R.id.btnCompleted);
//        btnCancelled = view.findViewById(R.id.btnCancelled);
//
//        // Quan sát dữ liệu
//        viewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orders -> {
//            if (orders != null) adapter.setOrders(orders);
//        });
//
//        // Mặc định load đơn hàng đang chờ
//        filterOrders("PENDING", btnPending);
//
//        // Gán sự kiện click
//        btnPending.setOnClickListener(v -> filterOrders("PENDING", btnPending));
//        btnCompleted.setOnClickListener(v -> filterOrders("SUCCESS", btnCompleted));
//        btnCancelled.setOnClickListener(v -> filterOrders("CANCEL", btnCancelled));
//    }
//
//    private void filterOrders(String status, Button selectedButton) {
//        viewModel.loadOrdersByStatus(status);
//        setSelectedButton(selectedButton);
//    }
//
//    private void setSelectedButton(Button selectedBtn) {
//        btnPending.setSelected(false);
//        btnCompleted.setSelected(false);
//        btnCancelled.setSelected(false);
//        selectedBtn.setSelected(true);
//    }
//}

public class OrderFragment extends Fragment {

    private OrderViewModel viewModel;
    private OrderAdapter adapter;
    private Button btnPending, btnCompleted, btnCancelled;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        adapter = new OrderAdapter(requireContext(), new ArrayList<>());

        RecyclerView recyclerView = view.findViewById(R.id.rvOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        btnPending = view.findViewById(R.id.btnPending);
        btnCompleted = view.findViewById(R.id.btnCompleted);
        btnCancelled = view.findViewById(R.id.btnCancelled);

        // Quan sát dữ liệu
        viewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) adapter.setOrders(orders);
        });

        // Mặc định load đơn hàng đang chờ
        filterOrders("PENDING", btnPending);

        // Gán sự kiện click
        btnPending.setOnClickListener(v -> filterOrders("PENDING", btnPending));
        btnCompleted.setOnClickListener(v -> filterOrders("SUCCESS", btnCompleted));
        btnCancelled.setOnClickListener(v -> filterOrders("CANCEL", btnCancelled));
    }

    private void filterOrders(String status, Button selectedButton) {
        viewModel.loadOrdersByStatus(status);
        setSelectedButton(selectedButton);
    }

    private void setSelectedButton(Button selectedBtn) {
        btnPending.setSelected(false);
        btnCompleted.setSelected(false);
        btnCancelled.setSelected(false);
        selectedBtn.setSelected(true);
    }
}