package com.project.clothingstore.viewmodel.productcollections;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.Product;
import com.project.clothingstore.service.ProductService;

import java.util.List;

public class ProductCollectionItemsViewModel extends ViewModel {
    private MutableLiveData<List<Product>> listProduct = new MutableLiveData<>();
    private ProductService productService = new ProductService();


    public void loadProduct(String collectionId) {
        productService.getProductByCollectioIDList(listProduct, collectionId);
    }


    public LiveData<List<Product>> getListProduct() {
        return listProduct;
    }
}
