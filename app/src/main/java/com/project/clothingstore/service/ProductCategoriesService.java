package com.project.clothingstore.service;

import com.project.clothingstore.modal.ProductCategories;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoriesService {

    public List<ProductCategories> getListProductCategory(){
        List<ProductCategories> list = new ArrayList<>();
        list.add(new ProductCategories("Áo sơ mi", "0", 50));
        list.add(new ProductCategories("Áo thun", "0", 60));
        list.add(new ProductCategories("Áo khoác", "0", 40));
        list.add(new ProductCategories("Quần tây", "0", 30));
        list.add(new ProductCategories("Quần jeans", "0", 35));

        list.add(new ProductCategories("Sneaker", "1", 25));
        list.add(new ProductCategories("Giày thể thao", "1", 40));
        list.add(new ProductCategories("Giày cao gót", "1", 20));
        list.add(new ProductCategories("Giày sandal", "1", 30));
        list.add(new ProductCategories("Giày bata", "1", 15));

        list.add(new ProductCategories("Vòng tay", "2", 50));
        list.add(new ProductCategories("Hoa tai", "2", 60));
        list.add(new ProductCategories("Đồng hồ", "2", 20));
        list.add(new ProductCategories("Dây chuyền", "2", 35));
        list.add(new ProductCategories("Nhẫn", "2", 25));

        list.add(new ProductCategories("Son môi", "3", 80));
        list.add(new ProductCategories("Kem dưỡng da", "3", 45));
        list.add(new ProductCategories("Nước hoa", "3", 30));
        list.add(new ProductCategories("Phấn nền", "3", 20));
        list.add(new ProductCategories("Mặt nạ dưỡng da", "3", 50));

        return list;
    }

    public List<ProductCategories> getListProductCategoryByType(String type) {
        List<ProductCategories> list = new ArrayList<>();
        for (ProductCategories productCategory : getListProductCategory()) {
            if (productCategory.getType().equals(type)) {
                list.add(productCategory);
            }
        }
        return list;
    }
}
