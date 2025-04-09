package com.project.clothingstore.viewmodel.productcategori;

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
        LoadProductCategory(0);
    }

    public void LoadProductCategory(int categoriType) {
        productCategoriesService.getListCategoryWithProductCount(listProductCategory, categoriType);
    }

    public LiveData<List<ProductCategories>> getListProductCategory() {

        return listProductCategory;
    }

}
