package com.project.clothingstore.viewmodel.discover;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.clothingstore.modal.ProductCategories;
import com.project.clothingstore.service.ProductCategoriesService;

import java.util.List;

public class ProductCategoriesViewModal extends ViewModel
{
    private MutableLiveData<List<ProductCategories>> listProductCategory = new MutableLiveData<>();
    private ProductCategoriesService productCategoriesService = new ProductCategoriesService();

    public ProductCategoriesViewModal() {
//        LoadProductCategory();
    }

//    private void LoadProductCategory() {
//        listProductCategory.setValue(productCategoriesService.getListProductCategory());
//    }
    public LiveData<List<ProductCategories>> getListProductCategory(String type) {
        List<ProductCategories> list = productCategoriesService.getListProductCategoryByType(type);
        listProductCategory.setValue(list);
        return listProductCategory;
    }

}
