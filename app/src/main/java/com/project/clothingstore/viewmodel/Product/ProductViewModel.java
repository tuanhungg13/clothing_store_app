package com.project.clothingstore.viewmodel.Product;

import android.util.Log;

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

    public ProductViewModel() {
        // Default load with initial limit
    }

    public void loadProduct(String categoriId) {
        productService.getProductByCategoriIdList(listProduct, categoriId);
    }
    public void loadFilteredProduct(int categoriType, int minPrice, int maxPrice,
                                    double rating, List<Integer> discounts) {
        productService.Test(listProduct, categoriType, minPrice, maxPrice, rating);
//        productService.getFilteredProducts(listProduct, categoriType, minPrice, maxPrice, rating, discounts);
    }



    public LiveData<List<Product>> getListProduct() {
        return listProduct;
    }
}
