//package com.project.clothingstore.view.fragment.productdetail;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.project.clothingstore.R;
//import com.project.clothingstore.adapter.productdetail.ColorAdapter;
//import com.project.clothingstore.adapter.productdetail.SizeAdapter;
//import com.project.clothingstore.modal.ColorItem;
//import com.project.clothingstore.modal.SizeItem;
//import com.project.clothingstore.viewmodel.ProductDetailViewModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProductInfoFragment extends Fragment implements ColorAdapter.OnColorSelectedListener, SizeAdapter.OnSizeSelectedListener {
//    private ProductDetailViewModel viewModel;
//    private TextView tvProductName, tvProductPrice, tvRatingCount;
//    private RatingBar ratingBar;
//    private RecyclerView recyclerViewColors, recyclerViewSizes;
//    private ColorAdapter colorAdapter;
//    private SizeAdapter sizeAdapter;
//    private List<ColorItem> colorList;
//    private List<SizeItem> sizeList;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_product_info, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//        // Initialize views
//        tvProductName = view.findViewById(R.id.tvProductName);
//        tvProductPrice = view.findViewById(R.id.tvProductPrice);
//        tvRatingCount = view.findViewById(R.id.tvRatingCount);
//        ratingBar = view.findViewById(R.id.ratingBar);
//        recyclerViewColors = view.findViewById(R.id.recyclerViewColors);
//        recyclerViewSizes = view.findViewById(R.id.recyclerViewSizes);
//
//        // Get shared ViewModel
//        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);
//
//        // Setup RecyclerViews with empty lists first
//        setupColorRecyclerView(new ArrayList<>());
//        setupSizeRecyclerView(new ArrayList<>());
//
//        // Observe product data
//        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
//            if (product != null) {
//                tvProductName.setText(product.getProductName());
//
//                // Format price with discount if applicable
//                if (product.getDiscount() > 0) {
//                    tvProductPrice.setText("đ " + product.getPrice() + " (-" + (int)(product.getDiscount() * 100) + "%)");
//                } else {
//                    tvProductPrice.setText("đ " + product.getPrice());
//                }
//
//                ratingBar.setRating(product.getTotalRating());
//            }
//        });
//
//        // Observe ratings data for count
//        viewModel.getRatings().observe(getViewLifecycleOwner(), ratings -> {
//            if (ratings != null) {
//                tvRatingCount.setText("(" + ratings.size() + ")");
//            }
//        });
//
//        // Observe colors data
//        viewModel.getColors().observe(getViewLifecycleOwner(), colors -> {
//            if (colors != null && !colors.isEmpty()) {
//                setupColorRecyclerView(colors);
//            }
//        });
//
//        // Observe sizes data
//        viewModel.getSizes().observe(getViewLifecycleOwner(), sizes -> {
//            if (sizes != null) {
//                setupSizeRecyclerView(sizes);
//            }
//        });
//
//        // Observe selected color
//        viewModel.getSelectedColor().observe(getViewLifecycleOwner(), color -> {
//            if (colorAdapter != null && color != null) {
//                colorAdapter.selectColorByName(color);
//            }
//        });
//
//        // Observe selected size
//        viewModel.getSelectedSize().observe(getViewLifecycleOwner(), size -> {
//            if (sizeAdapter != null && size != null) {
//                sizeAdapter.selectSizeByName(size);
//            }
//        });
//    }
//
//    private void setupColorRecyclerView() {
//        // Initialize color list
//        colorList = new ArrayList<>();
//        colorList.add(new ColorItem("1", "Beige", "#F5F5DC"));
//        colorList.add(new ColorItem("2", "Đen", "#000000"));
//        colorList.add(new ColorItem("3", "Đỏ", "#FF0000"));
//        colorList.add(new ColorItem("4", "Xanh", "#0000FF"));
//        colorList.add(new ColorItem("5", "Xám", "#808080"));
//        colorList.add(new ColorItem("6", "Trắng", "#FFFFFF"));
//
//        // Setup adapter
//        colorAdapter = new ColorAdapter(colorList, this);
//        recyclerViewColors.setAdapter(colorAdapter);
//        recyclerViewColors.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//
//        // Set initial selection
//        String selectedColor = viewModel.getSelectedColor().getValue();
//        if (selectedColor != null) {
//            colorAdapter.selectColorByName(selectedColor);
//        }
//    }
//
//    private void setupSizeRecyclerView() {
//        // Initialize size list with default clothing sizes
//        sizeList = new ArrayList<>();
//        sizeList.add(new SizeItem("1", "S"));
//        sizeList.add(new SizeItem("2", "M"));
//        sizeList.add(new SizeItem("3", "L"));
//        sizeList.add(new SizeItem("4", "XL"));
//        sizeList.add(new SizeItem("5", "XXL"));
//
//        // Setup adapter
//        sizeAdapter = new SizeAdapter(sizeList, this);
//        recyclerViewSizes.setAdapter(sizeAdapter);
//        recyclerViewSizes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//
//        // Set initial selection
//        String selectedSize = viewModel.getSelectedSize().getValue();
//        if (selectedSize != null) {
//            sizeAdapter.selectSizeByName(selectedSize);
//        }
//    }
//
//    private void updateSizeOptionsForProductType(int productType) {
//        // Clear current sizes
//        sizeList.clear();
//
//        // Add sizes based on product type
//        switch (productType) {
//            case 1: // Clothing
//                sizeList.add(new SizeItem("1", "S"));
//                sizeList.add(new SizeItem("2", "M"));
//                sizeList.add(new SizeItem("3", "L"));
//                sizeList.add(new SizeItem("4", "XL"));
//                sizeList.add(new SizeItem("5", "XXL"));
//                break;
//            case 2: // Shoes
//                sizeList.add(new SizeItem("1", "36"));
//                sizeList.add(new SizeItem("2", "37"));
//                sizeList.add(new SizeItem("3", "38"));
//                sizeList.add(new SizeItem("4", "39"));
//                sizeList.add(new SizeItem("5", "40"));
//                sizeList.add(new SizeItem("6", "41"));
//                sizeList.add(new SizeItem("7", "42"));
//                sizeList.add(new SizeItem("8", "43"));
//                sizeList.add(new SizeItem("9", "44"));
//                break;
//            case 3: // Accessories
//                sizeList.add(new SizeItem("1", "OS")); // One Size
//                break;
//            default:
//                // Default clothing sizes
//                sizeList.add(new SizeItem("1", "S"));
//                sizeList.add(new SizeItem("2", "M"));
//                sizeList.add(new SizeItem("3", "L"));
//                break;
//        }
//
//        // Notify adapter of data change
//        sizeAdapter.notifyDataSetChanged();
//
//        // Reselect size if possible
//        String selectedSize = viewModel.getSelectedSize().getValue();
//        if (selectedSize != null) {
//            sizeAdapter.selectSizeByName(selectedSize);
//        } else if (!sizeList.isEmpty()) {
//            // Select first size if no size was previously selected
//            viewModel.setSelectedSize(sizeList.get(0).getName());
//        }
//    }
//
//
//    private void setupColorRecyclerView(List<ColorItem> colors) {
//        colorAdapter = new ColorAdapter(colors, this);
//        recyclerViewColors.setAdapter(colorAdapter);
//        recyclerViewColors.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//    }
//
//    private void setupSizeRecyclerView(List<SizeItem> sizes) {
//        sizeAdapter = new SizeAdapter(sizes, this);
//        recyclerViewSizes.setAdapter(sizeAdapter);
//        recyclerViewSizes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//    }
//
//    @Override
//    public void onColorSelected(ColorItem color) {
//        viewModel.setSelectedColor(color.getName());
//    }
//
//    @Override
//    public void onSizeSelected(SizeItem size) {
//        viewModel.setSelectedSize(size.getName());
//    }
//}


package com.project.clothingstore.view.fragment.productdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.clothingstore.R;
import com.project.clothingstore.adapter.productdetail.ColorAdapter;
import com.project.clothingstore.adapter.productdetail.SizeAdapter;
import com.project.clothingstore.modal.ColorItem;
import com.project.clothingstore.modal.SizeItem;
import com.project.clothingstore.viewmodel.ProductDetailViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoFragment extends Fragment implements ColorAdapter.OnColorSelectedListener, SizeAdapter.OnSizeSelectedListener {
    private ProductDetailViewModel viewModel;
    private TextView tvProductName, tvProductPrice, tvRatingCount;
    private RatingBar ratingBar;
    private RecyclerView recyclerViewColors, recyclerViewSizes;
    private ColorAdapter colorAdapter;
    private SizeAdapter sizeAdapter;
    private List<ColorItem> colorList;
    private List<SizeItem> sizeList;
    private final DecimalFormat priceFormatter = new DecimalFormat("#,###");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initialize views
        tvProductName = view.findViewById(R.id.tvProductName);
        tvProductPrice = view.findViewById(R.id.tvProductPrice);
        tvRatingCount = view.findViewById(R.id.tvRatingCount);
        ratingBar = view.findViewById(R.id.ratingBar);
        recyclerViewColors = view.findViewById(R.id.recyclerViewColors);
        recyclerViewSizes = view.findViewById(R.id.recyclerViewSizes);

        // Get shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

        // Setup RecyclerViews with empty lists first
        setupColorRecyclerView(new ArrayList<>());
        setupSizeRecyclerView(new ArrayList<>());

        // Observe product data
        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                tvProductName.setText(product.getProductName());

                // Format price with discount if applicable
                if (product.getDiscount() > 0) {
                    String formattedPrice = "đ " + priceFormatter.format(product.getPrice());
                    int discountPercent = (int)(product.getDiscount() * 100);
                    tvProductPrice.setText(formattedPrice + " (-" + discountPercent + "%)");
                } else {
                    tvProductPrice.setText("đ " + priceFormatter.format(product.getPrice()));
                }

                ratingBar.setRating(product.getTotalRating());
            }
        });

        // Observe ratings data for count
        viewModel.getRatings().observe(getViewLifecycleOwner(), ratings -> {
            if (ratings != null) {
                tvRatingCount.setText("(" + ratings.size() + ")");
            }
        });

        // Observe colors data
        viewModel.getColors().observe(getViewLifecycleOwner(), colors -> {
            if (colors != null && !colors.isEmpty()) {
                setupColorRecyclerView(colors);
            }
        });

        // Observe sizes data
        viewModel.getSizes().observe(getViewLifecycleOwner(), sizes -> {
            if (sizes != null) {
                setupSizeRecyclerView(sizes);
            }
        });

        // Observe selected color
        viewModel.getSelectedColor().observe(getViewLifecycleOwner(), color -> {
            if (colorAdapter != null && color != null) {
                colorAdapter.selectColorByName(color);
            }
        });

        // Observe selected size
        viewModel.getSelectedSize().observe(getViewLifecycleOwner(), size -> {
            if (sizeAdapter != null && size != null) {
                sizeAdapter.selectSizeByName(size);
            }
        });
    }

    // Các phương thức khác giữ nguyên
    private void setupColorRecyclerView(List<ColorItem> colors) {
        colorAdapter = new ColorAdapter(colors, this);
        recyclerViewColors.setAdapter(colorAdapter);
        recyclerViewColors.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupSizeRecyclerView(List<SizeItem> sizes) {
        sizeAdapter = new SizeAdapter(sizes, this);
        recyclerViewSizes.setAdapter(sizeAdapter);
        recyclerViewSizes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onColorSelected(ColorItem color) {
        viewModel.setSelectedColor(color.getName());
    }

    @Override
    public void onSizeSelected(SizeItem size) {
        viewModel.setSelectedSize(size.getName());
    }
}
