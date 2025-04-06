package com.project.clothingstore.viewmodel.productcollections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.ProductCollections;
import com.project.clothingstore.service.ProductCollectionService;

import java.util.List;

public class ProductCollectionViewModel extends ViewModel {
    private MutableLiveData<List<ProductCollections>> listProductCategory = new MutableLiveData<>();
    private ProductCollectionService productCollectionService = new ProductCollectionService();
    public ProductCollectionViewModel() {
        LoadProductCategory();
    }

    public void LoadProductCategory() {
        listProductCategory.setValue(productCollectionService.getListBST());
    }

    public LiveData<List<ProductCollections>> getListProductCategory() {
        return listProductCategory;
    }
}
