package com.project.clothingstore.viewmodel.productcategory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.ProductCategory;
import com.project.clothingstore.service.ProductCategoryService;

import java.util.List;

public class ProductCategoryViewModel extends ViewModel {
    private MutableLiveData<List<ProductCategory>> listProductCategory = new MutableLiveData<>();
    private ProductCategoryService productCategoryService = new ProductCategoryService();
    public ProductCategoryViewModel() {
        LoadProductCategory();
    }

    public void LoadProductCategory() {
        listProductCategory.setValue(productCategoryService.getListBST());
    }

    public LiveData<List<ProductCategory>> getListProductCategory() {
        return listProductCategory;
    }
}
