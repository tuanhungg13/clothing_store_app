package com.project.clothingstore.viewmodel.Product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.Product;
import com.project.clothingstore.modal.ProductCategories;
import com.project.clothingstore.service.ProductService;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private MutableLiveData<List<Product>> listProduct = new MutableLiveData<>();
    private ProductService productService = new ProductService();


    public LiveData<List<Product>> getListProduct(String categoryId) {
        List<Product> list = productService.getListProductByType(categoryId);
        listProduct.setValue(list);
        return listProduct;
    }
}
