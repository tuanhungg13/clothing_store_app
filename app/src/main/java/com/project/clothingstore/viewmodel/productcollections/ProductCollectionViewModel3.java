package com.project.clothingstore.viewmodel.productcollections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.ProductCollections;
import com.project.clothingstore.service.ProductCategoryService;

import java.util.List;


public class ProductCollectionViewModel3 extends ViewModel {
    private MutableLiveData<List<ProductCollections>> listProductCategory = new MutableLiveData<>();
    private ProductCategoryService productCategoryService = new ProductCategoryService();
    public ProductCollectionViewModel3() {
        LoadProductCategory();
    }

    public void LoadProductCategory() {
        listProductCategory.setValue(productCategoryService.getListProductCategory3());
    }

    public LiveData<List<ProductCollections>> getListProductCategory() {
        return listProductCategory;
    }
}
