package com.project.clothingstore.service;


import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService {
    private final CollectionReference productRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProductService() {
        productRef = FirebaseHelper.getProductCollection();
    }

    public ProductService(CollectionReference productRef) {
        this.productRef = productRef;
    }

    // ✅ Lấy tất cả sản phẩm
    public void getAllProducts(OnCompleteListener<QuerySnapshot> listener) {
        productRef.get().addOnCompleteListener(listener);
    }

    // ✅ Lấy sản phẩm theo ID
    public void getProductById(String productId, OnCompleteListener<DocumentSnapshot> listener) {
        productRef.document(productId).get().addOnCompleteListener(listener);
    }

    // ✅ Lọc sản phẩm theo category
    public void getProductsByCategory(String categoryId, OnCompleteListener<QuerySnapshot> listener) {
        productRef.whereEqualTo("categoryId", categoryId).get().addOnCompleteListener(listener);
    }

    // ✅ Tìm kiếm sản phẩm theo tên
    public void searchProductsByName(String keyword, OnCompleteListener<QuerySnapshot> listener) {
        productRef.orderBy("productName")
                .startAt(keyword)
                .endAt(keyword + "\uf8ff")
                .get().addOnCompleteListener(listener);
    }

    // ✅ Phân trang sản phẩm
    public void getProductsPaged(DocumentSnapshot lastDoc, int limit, OnCompleteListener<QuerySnapshot> listener) {
        Query query = productRef.orderBy("productName").limit(limit);
        if (lastDoc != null) {
            query = query.startAfter(lastDoc);
        }
        query.get().addOnCompleteListener(listener);
    }

//     Lay ra 5 san pham co luot ban cao nhat
    public void getSanPhamList(MutableLiveData<List<Product>> liveData, String field, int limitt) {
        CollectionReference productsRef = db.collection("products");

        // Lấy 5 sản phẩm có lượt bán cao nhất
        productsRef.orderBy(field, Query.Direction.DESCENDING)
                .limit(limitt)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> productList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Product product = doc.toObject(Product.class);
                            product.setProductId(doc.getId());
                            productList.add(product);
                        }
                        liveData.setValue(productList);
                    } else {
                        liveData.setValue(new ArrayList<>()); // Tránh null
                    }
                });
    }



    // Lấy tất cả sản phẩm
    public void getAllSanPhamList(MutableLiveData<List<Product>> liveData) {
        CollectionReference productsRef = db.collection("products");
        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Product> productList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Product product = doc.toObject(Product.class);
                    product.setProductId(doc.getId());
                    productList.add(product);
                }
                liveData.setValue(productList);
            } else {
                liveData.setValue(new ArrayList<>()); // Tránh null
            }
        });
    }


    public List<Product> getListProduct() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Áo hoodie trắng", 200000, "0", Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc"), 250000, 4.5F));
        list.add(new Product("Quần jean rách gối", 350000, "0", Arrays.asList("https://imgs.search.brave.com/2fuynJDBZBqOkm3aQWzZOyhWdtUXuo5pXimqLkA5X34/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdC5h/cHAxaC5jb20vdXBs/b2Fkcy9pbWFnZXMv/Y29tcGFueTcyL2lt/YWdlcy9xdWElQ0Ml/ODIlQ0MlODBuJTIw/amVhbiUyMG5hbSUy/MHhhbmglMjByYSVD/QyU4MWNoJTIwZ28l/Q0MlODIlQ0MlODFp/LmpwZw"), 400000, 4));
        list.add(new Product("Áo thun basic", 180000, "0", Arrays.asList("https://imgs.search.brave.com/HN2TcsoJTo0O3rs_RWIGtESaqfuZ2OEpZ7v1PfMZ7Qo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L2NhY2hlL3cxMjAw/L3RzL3Byb2R1Y3Qv/ZTIvZDMvOTQvMzNi/ODQ4NTA0MTg3NmFh/NjMyZmE1NThmNGYz/MWVhZTUuanBn"), 220000, 3.7F));
        list.add(new Product("Quần tây công sở", 300000, "0", Arrays.asList("https://imgs.search.brave.com/BJdyrFwzMw7JaJiz9lgai0WBuBEBxwmSaa79Qwkscac/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aG9p/dHJhbmd0aGFpaG9h/LmNvbS9zdG9yYWdl/L2NhY2hlL2NhdGFs/b2cvcHJvZHVjdC9x/dWFuX3N1b25nX251/X3RoYWlfaG9hXzI0/NS0zXzQyNXgud2Vi/cA"), 350000, 5));
        list.add(new Product("Áo sơ mi trắng", 250000, "0", Arrays.asList("https://imgs.search.brave.com/VL0DICqtCFgLeoSO1WMyqhLRYR5PmW51Aq1rleFc32g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9i/ZTMxNTktNjYyL3Bz/LzIwMjQwOTE5X0ZP/Z1hEMHJuUFUuanBl/Zw"), 290000, 4));
        list.add(new Product("Quần short kaki", 220000, "0", Arrays.asList("https://imgs.search.brave.com/xQrKzjDW-jBmVpAGkTaNNo_LKrfzUDs9zovR-AEb808/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9k/MGYzY2EtNzEzNi9w/cy8yMDI0MTAyOF9T/N1NUelJDYVU5Lmpw/ZWc"), 260000, 4));
        list.add(new Product("Áo polo nam", 270000, "0", Arrays.asList("https://imgs.search.brave.com/L0feDn2RlyfUQanIqQ88rmco-BW_3ugxtXWjO0TeBQo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb2xv/bWFub3Iudm4vY2Ru/L3Nob3AvZmlsZXMv/YW8tcG9sby1uYW0t/YWRlbi0wLmpwZz92/PTE3MzkyNTk1OTgm/d2lkdGg9MTIwMA"), 310000, 5));
        list.add(new Product("Quần jogger thể thao", 280000, "0", Arrays.asList("https://imgs.search.brave.com/D7_y8EybRuubjgIEAEBwu7557oNAwc3s7fu14ZJTqI0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YTMuY29vbG1hdGUu/bWUvY2RuLWNnaS9p/bWFnZS93aWR0aD00/MjAscXVhbGl0eT05/MCxmb3JtYXQ9YXV0/by91cGxvYWRzL0Rl/Y2VtYmVyMjAyNC9x/dWFuLWpvZ2dlcnMt/dGhlLXRoYW8tZGFp/bHktd2Vhci0xLmpw/Zw"), 320000, 4));
        list.add(new Product("Áo khoác gió", 400000, "0", Arrays.asList("https://imgs.search.brave.com/wG9GXHLlFRy6IWS4lmh267jf1gaM_T5G7EUbNh3ixkU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L3RzL2JyaWNrdjJv/Zy8yMC83NC83ZS8x/ZWMzMDUwZDUxOTc2/Y2IyY2VjMDAyODRj/YjQwNzhiMy53ZWJw"), 450000, 5));
        list.add(new Product("Quần jean skinny", 330000, "0", Arrays.asList("https://imgs.search.brave.com/UsDlb9Q54Hog4ocQIFMFR-xQZcqZwwyg7rEJC3P-wyc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxODQ2MDEv/ZmlsZS90b3AtNS1j/YWNoLW1peC1xdWFu/LXNraW5ueS1qZWFu/cy1uYW0tZGVwLWNo/dWFuLXNpZXUtbWF1/LTNfZGEzY2MzMWU0/MDQ4NDk1NzkyODMy/OWFkODhlZTQ3NmVf/Z3JhbmRlLmpwZw"), 380000, 4));

        list.add(new Product("Giày Nam", 200000, "1", Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc"), 250000, 5));
        list.add(new Product("Giày Nữ", 350000, "1", Arrays.asList("https://imgs.search.brave.com/2fuynJDBZBqOkm3aQWzZOyhWdtUXuo5pXimqLkA5X34/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdC5h/cHAxaC5jb20vdXBs/b2Fkcy9pbWFnZXMv/Y29tcGFueTcyL2lt/YWdlcy9xdWElQ0Ml/ODIlQ0MlODBuJTIw/amVhbiUyMG5hbSUy/MHhhbmglMjByYSVD/QyU4MWNoJTIwZ28l/Q0MlODIlQ0MlODFp/LmpwZw"), 400000, 4));
        list.add(new Product("Giày tre em", 180000, "1", Arrays.asList("https://imgs.search.brave.com/HN2TcsoJTo0O3rs_RWIGtESaqfuZ2OEpZ7v1PfMZ7Qo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L2NhY2hlL3cxMjAw/L3RzL3Byb2R1Y3Qv/ZTIvZDMvOTQvMzNi/ODQ4NTA0MTg3NmFh/NjMyZmE1NThmNGYz/MWVhZTUuanBn"), 220000, 4));
        list.add(new Product("Giay thể thao", 300000, "1", Arrays.asList("https://imgs.search.brave.com/BJdyrFwzMw7JaJiz9lgai0WBuBEBxwmSaa79Qwkscac/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aG9p/dHJhbmd0aGFpaG9h/LmNvbS9zdG9yYWdl/L2NhY2hlL2NhdGFs/b2cvcHJvZHVjdC9x/dWFuX3N1b25nX251/X3RoYWlfaG9hXzI0/NS0zXzQyNXgud2Vi/cA"), 350000, 5));
        list.add(new Product("Giay thể thao", 250000, "1", Arrays.asList("https://imgs.search.brave.com/VL0DICqtCFgLeoSO1WMyqhLRYR5PmW51Aq1rleFc32g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9i/ZTMxNTktNjYyL3Bz/LzIwMjQwOTE5X0ZP/Z1hEMHJuUFUuanBl/Zw"), 290000, 4));
        list.add(new Product("Giay thể thao", 220000, "1", Arrays.asList("https://imgs.search.brave.com/xQrKzjDW-jBmVpAGkTaNNo_LKrfzUDs9zovR-AEb808/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9k/MGYzY2EtNzEzNi9w/cy8yMDI0MTAyOF9T/N1NUelJDYVU5Lmpw/ZWc"), 260000, 4));
        list.add(new Product("Giay thể thao", 270000, "1", Arrays.asList("https://imgs.search.brave.com/L0feDn2RlyfUQanIqQ88rmco-BW_3ugxtXWjO0TeBQo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb2xv/bWFub3Iudm4vY2Ru/L3Nob3AvZmlsZXMv/YW8tcG9sby1uYW0t/YWRlbi0wLmpwZz92/PTE3MzkyNTk1OTgm/d2lkdGg9MTIwMA"), 310000, 5));
        list.add(new Product("Giay thể thao", 280000, "1", Arrays.asList("https://imgs.search.brave.com/D7_y8EybRuubjgIEAEBwu7557oNAwc3s7fu14ZJTqI0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YTMuY29vbG1hdGUu/bWUvY2RuLWNnaS9p/bWFnZS93aWR0aD00/MjAscXVhbGl0eT05/MCxmb3JtYXQ9YXV0/by91cGxvYWRzL0Rl/Y2VtYmVyMjAyNC9x/dWFuLWpvZ2dlcnMt/dGhlLXRoYW8tZGFp/bHktd2Vhci0xLmpw/Zw"), 320000, 5));
        list.add(new Product("Giày Nữ", 400000, "1", Arrays.asList("https://imgs.search.brave.com/wG9GXHLlFRy6IWS4lmh267jf1gaM_T5G7EUbNh3ixkU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L3RzL2JyaWNrdjJv/Zy8yMC83NC83ZS8x/ZWMzMDUwZDUxOTc2/Y2IyY2VjMDAyODRj/YjQwNzhiMy53ZWJw"), 450000, 5));
        list.add(new Product("Giày Nữ", 330000, "1", Arrays.asList("https://imgs.search.brave.com/UsDlb9Q54Hog4ocQIFMFR-xQZcqZwwyg7rEJC3P-wyc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxODQ2MDEv/ZmlsZS90b3AtNS1j/YWNoLW1peC1xdWFu/LXNraW5ueS1qZWFu/cy1uYW0tZGVwLWNo/dWFuLXNpZXUtbWF1/LTNfZGEzY2MzMWU0/MDQ4NDk1NzkyODMy/OWFkODhlZTQ3NmVf/Z3JhbmRlLmpwZw"), 380000, 5));

        list.add(new Product("Kính", 200000, "2", Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc"), 250000, 5));
        list.add(new Product("Đồng Ho", 350000, "2", Arrays.asList("https://imgs.search.brave.com/2fuynJDBZBqOkm3aQWzZOyhWdtUXuo5pXimqLkA5X34/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdC5h/cHAxaC5jb20vdXBs/b2Fkcy9pbWFnZXMv/Y29tcGFueTcyL2lt/YWdlcy9xdWElQ0Ml/ODIlQ0MlODBuJTIw/amVhbiUyMG5hbSUy/MHhhbmglMjByYSVD/QyU4MWNoJTIwZ28l/Q0MlODIlQ0MlODFp/LmpwZw"), 400000, 5));
        list.add(new Product("Kính", 180000, "2", Arrays.asList("https://imgs.search.brave.com/HN2TcsoJTo0O3rs_RWIGtESaqfuZ2OEpZ7v1PfMZ7Qo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L2NhY2hlL3cxMjAw/L3RzL3Byb2R1Y3Qv/ZTIvZDMvOTQvMzNi/ODQ4NTA0MTg3NmFh/NjMyZmE1NThmNGYz/MWVhZTUuanBn"), 220000, 4));
        list.add(new Product("Kính", 300000, "2", Arrays.asList("https://imgs.search.brave.com/BJdyrFwzMw7JaJiz9lgai0WBuBEBxwmSaa79Qwkscac/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aG9p/dHJhbmd0aGFpaG9h/LmNvbS9zdG9yYWdl/L2NhY2hlL2NhdGFs/b2cvcHJvZHVjdC9x/dWFuX3N1b25nX251/X3RoYWlfaG9hXzI0/NS0zXzQyNXgud2Vi/cA"), 350000, 5));
        list.add(new Product("Kính", 250000, "2", Arrays.asList("https://imgs.search.brave.com/VL0DICqtCFgLeoSO1WMyqhLRYR5PmW51Aq1rleFc32g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9i/ZTMxNTktNjYyL3Bz/LzIwMjQwOTE5X0ZP/Z1hEMHJuUFUuanBl/Zw"), 290000, 5));
        list.add(new Product("Kính", 220000, "2", Arrays.asList("https://imgs.search.brave.com/xQrKzjDW-jBmVpAGkTaNNo_LKrfzUDs9zovR-AEb808/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9k/MGYzY2EtNzEzNi9w/cy8yMDI0MTAyOF9T/N1NUelJDYVU5Lmpw/ZWc"), 260000, 5));
        list.add(new Product("Kính", 270000, "2", Arrays.asList("https://imgs.search.brave.com/L0feDn2RlyfUQanIqQ88rmco-BW_3ugxtXWjO0TeBQo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb2xv/bWFub3Iudm4vY2Ru/L3Nob3AvZmlsZXMv/YW8tcG9sby1uYW0t/YWRlbi0wLmpwZz92/PTE3MzkyNTk1OTgm/d2lkdGg9MTIwMA"), 310000, 4));
        list.add(new Product("Kính", 280000, "2", Arrays.asList("https://imgs.search.brave.com/D7_y8EybRuubjgIEAEBwu7557oNAwc3s7fu14ZJTqI0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YTMuY29vbG1hdGUu/bWUvY2RuLWNnaS9p/bWFnZS93aWR0aD00/MjAscXVhbGl0eT05/MCxmb3JtYXQ9YXV0/by91cGxvYWRzL0Rl/Y2VtYmVyMjAyNC9x/dWFuLWpvZ2dlcnMt/dGhlLXRoYW8tZGFp/bHktd2Vhci0xLmpw/Zw"), 320000, 5));
        list.add(new Product("Kính", 400000, "2", Arrays.asList("https://imgs.search.brave.com/wG9GXHLlFRy6IWS4lmh267jf1gaM_T5G7EUbNh3ixkU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L3RzL2JyaWNrdjJv/Zy8yMC83NC83ZS8x/ZWMzMDUwZDUxOTc2/Y2IyY2VjMDAyODRj/YjQwNzhiMy53ZWJw"), 450000, 5));
        list.add(new Product("Kính", 330000, "2", Arrays.asList("https://imgs.search.brave.com/UsDlb9Q54Hog4ocQIFMFR-xQZcqZwwyg7rEJC3P-wyc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxODQ2MDEv/ZmlsZS90b3AtNS1j/YWNoLW1peC1xdWFu/LXNraW5ueS1qZWFu/cy1uYW0tZGVwLWNo/dWFuLXNpZXUtbWF1/LTNfZGEzY2MzMWU0/MDQ4NDk1NzkyODMy/OWFkODhlZTQ3NmVf/Z3JhbmRlLmpwZw"), 380000, 5));

        list.add(new Product("Son", 200000, "3", Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc"), 250000, 5));
        list.add(new Product("Son", 350000, "3", Arrays.asList("https://imgs.search.brave.com/2fuynJDBZBqOkm3aQWzZOyhWdtUXuo5pXimqLkA5X34/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdC5h/cHAxaC5jb20vdXBs/b2Fkcy9pbWFnZXMv/Y29tcGFueTcyL2lt/YWdlcy9xdWElQ0Ml/ODIlQ0MlODBuJTIw/amVhbiUyMG5hbSUy/MHhhbmglMjByYSVD/QyU4MWNoJTIwZ28l/Q0MlODIlQ0MlODFp/LmpwZw"), 400000, 4));
        list.add(new Product("Son", 180000, "3", Arrays.asList("https://imgs.search.brave.com/HN2TcsoJTo0O3rs_RWIGtESaqfuZ2OEpZ7v1PfMZ7Qo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L2NhY2hlL3cxMjAw/L3RzL3Byb2R1Y3Qv/ZTIvZDMvOTQvMzNi/ODQ4NTA0MTg3NmFh/NjMyZmE1NThmNGYz/MWVhZTUuanBn"), 220000, 4));
        list.add(new Product("Son", 300000, "3", Arrays.asList("https://imgs.search.brave.com/BJdyrFwzMw7JaJiz9lgai0WBuBEBxwmSaa79Qwkscac/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aG9p/dHJhbmd0aGFpaG9h/LmNvbS9zdG9yYWdl/L2NhY2hlL2NhdGFs/b2cvcHJvZHVjdC9x/dWFuX3N1b25nX251/X3RoYWlfaG9hXzI0/NS0zXzQyNXgud2Vi/cA"), 350000, 5));
        list.add(new Product("Son", 250000, "3", Arrays.asList("https://imgs.search.brave.com/VL0DICqtCFgLeoSO1WMyqhLRYR5PmW51Aq1rleFc32g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9i/ZTMxNTktNjYyL3Bz/LzIwMjQwOTE5X0ZP/Z1hEMHJuUFUuanBl/Zw"), 290000, 4));
        list.add(new Product("Son", 220000, "3", Arrays.asList("https://imgs.search.brave.com/xQrKzjDW-jBmVpAGkTaNNo_LKrfzUDs9zovR-AEb808/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9k/MGYzY2EtNzEzNi9w/cy8yMDI0MTAyOF9T/N1NUelJDYVU5Lmpw/ZWc"), 260000, 4));
        list.add(new Product("Son", 270000, "3", Arrays.asList("https://imgs.search.brave.com/L0feDn2RlyfUQanIqQ88rmco-BW_3ugxtXWjO0TeBQo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb2xv/bWFub3Iudm4vY2Ru/L3Nob3AvZmlsZXMv/YW8tcG9sby1uYW0t/YWRlbi0wLmpwZz92/PTE3MzkyNTk1OTgm/d2lkdGg9MTIwMA"), 310000, 5));
        list.add(new Product("Son", 280000, "3", Arrays.asList("https://imgs.search.brave.com/D7_y8EybRuubjgIEAEBwu7557oNAwc3s7fu14ZJTqI0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YTMuY29vbG1hdGUu/bWUvY2RuLWNnaS9p/bWFnZS93aWR0aD00/MjAscXVhbGl0eT05/MCxmb3JtYXQ9YXV0/by91cGxvYWRzL0Rl/Y2VtYmVyMjAyNC9x/dWFuLWpvZ2dlcnMt/dGhlLXRoYW8tZGFp/bHktd2Vhci0xLmpw/Zw"), 320000, 4));
        list.add(new Product("Son", 400000, "3", Arrays.asList("https://imgs.search.brave.com/wG9GXHLlFRy6IWS4lmh267jf1gaM_T5G7EUbNh3ixkU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L3RzL2JyaWNrdjJv/Zy8yMC83NC83ZS8x/ZWMzMDUwZDUxOTc2/Y2IyY2VjMDAyODRj/YjQwNzhiMy53ZWJw"), 450000, 5));
        list.add(new Product("Son", 330000, "3", Arrays.asList("https://imgs.search.brave.com/UsDlb9Q54Hog4ocQIFMFR-xQZcqZwwyg7rEJC3P-wyc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxODQ2MDEv/ZmlsZS90b3AtNS1j/YWNoLW1peC1xdWFu/LXNraW5ueS1qZWFu/cy1uYW0tZGVwLWNo/dWFuLXNpZXUtbWF1/LTNfZGEzY2MzMWU0/MDQ4NDk1NzkyODMy/OWFkODhlZTQ3NmVf/Z3JhbmRlLmpwZw"), 380000, 5));
        return list;
    }

    public List<Product> getListProductByType(String categoryId) {
        List<Product> list = new ArrayList<>();
        for (Product product : getListProduct()) {
            if (product.getCategoryId().equals(categoryId)) {
                list.add(product);
            }
        }
        return list;
    }
}
