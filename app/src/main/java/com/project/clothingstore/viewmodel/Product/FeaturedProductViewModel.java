package com.project.clothingstore.viewmodel.Product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.Product;
import com.project.clothingstore.service.ProductService;

import java.util.List;

public class FeaturedProductViewModel extends ViewModel {
    private MutableLiveData<List<Product>> listProduct = new MutableLiveData<>();
    private ProductService productService = new ProductService();
    // Không giữ sẵn LiveData, mỗi lần gọi sẽ tạo mới LiveData với limit tương ứng
    public FeaturedProductViewModel() {
        // Default load with initial limit
        loadProductWithLimit(5);
    }

    public void loadProductWithLimit(int limit) {
        productService.getSanPhamList(listProduct, "sold", limit);
    }

    public LiveData<List<Product>> getListProduct() {
        return listProduct;
    }

}
