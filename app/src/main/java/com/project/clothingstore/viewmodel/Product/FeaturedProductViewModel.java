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
    public FeaturedProductViewModel() {
        LoadProduct();
    }

    public void LoadProduct() {
//        listProduct.setValue(productService.getSanPhamList());
    }

    public LiveData<List<Product>> getListProduct() {
        return listProduct;
    }
}
