package com.project.clothingstore.viewmodel.productcollections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.ProductCollections;
import com.project.clothingstore.service.ProductCollectionService;

import java.util.List;

public class ProductCollectionViewModel2 extends ViewModel {
    private MutableLiveData<List<ProductCollections>> listProductCategory = new MutableLiveData<>();
    private ProductCollectionService productCollectionService = new ProductCollectionService();
    public ProductCollectionViewModel2() {
        LoadProductCategory();
    }

    public void LoadProductCategory() {
        productCollectionService.getCollectionList(listProductCategory, "bình thường");
    }

    public LiveData<List<ProductCollections>> getListProductCategory() {
        return listProductCategory;
    }
}
