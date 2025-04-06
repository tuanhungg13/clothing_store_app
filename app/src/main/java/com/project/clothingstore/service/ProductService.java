package com.project.clothingstore.service;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.clothingstore.modal.Product;
import com.project.clothingstore.modal.ProductCategories;
import com.project.clothingstore.utils.helper.FirebaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService {
    private final CollectionReference productRef;

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


    // Fake data (sau xóa)
    public List<Product> getSanPhamList() {
        List<Product> list = new ArrayList<>();
        List<String> images1 = new ArrayList<>();
        images1.add("https://imgs.search.brave.com/WH0EAoxRlNgLWFEGOgz-IgnpZf9JYyrBGYs0xeHVUys/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wcm9k/dWN0LmhzdGF0aWMu/bmV0LzIwMDAwMDI3/MjA4MS9wcm9kdWN0/LzQ0MDlfNC40MDFf/NTk5MDAwX3dlYl9h/ZGQyZWZlNzljMzA0/YTc2OGQwZTgyMTI3/YTExNGJkNF9ncmFu/ZGUuanBn");
        list.add(new Product("Áo len", 39000, Arrays.asList("https://imgs.search.brave.com/D2e42mhBDr38wRWcxwtQLjkjoJskWXMIXRyoOYSLUMI/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxNzg3Nzkv/ZmlsZS9hb19sZW5f/Y29fbG9fbnUuanBn")));
        list.add(new Product("Áo dài", 45000, Arrays.asList("https://imgs.search.brave.com/NALIw-mSjKwuO0a0zIUbPIoxBcyF_cqYvoYRqlckWEI/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90YWls/b2N3ZWRkaW5nLnZu/L3dwLWNvbnRlbnQv/dXBsb2Fkcy8yMDIz/LzA0L0FuaC1tYXUt/YW8tZGFpLXR1eWV0/LWRlcC01MzN4ODAw/LmpwZw")));
        list.add(new Product("Đồ thể thao", 80000, Arrays.asList("https://imgs.search.brave.com/1Yylxm02DhkadoPI486UVUP3SqUPlgiPCoQMiJXVk5M/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wcm9k/dWN0LmhzdGF0aWMu/bmV0LzIwMDAwMDI0/Nzk2OS9wcm9kdWN0/LzJfMGU0YzBjNTY4/YzY1NGIzNjliMDEx/ZDY1OGNkMWQ2Y2Vf/bGFyZ2UuanBn")));
        list.add(new Product("Đồ mùa hè", 89000, images1));
        return list;
    }

    public List<Product> getListRecommentProduct() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Áo hoodie trắng", 20, Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc")));
        list.add(new Product("Áo phông", 30, Arrays.asList("https://imgs.search.brave.com/nVsiA9Sv8-fDMI2zukVEPuVshyxKwxmSQYijsAbcdLg/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tY2Ru/LmNvb2xtYXRlLm1l/L2ltYWdlL0F1Z3Vz/dDIwMjIvYW8taW4t/aGluaF83NjkuanBn")));
        list.add(new Product("Áo len", 39, Arrays.asList("https://imgs.search.brave.com/D2e42mhBDr38wRWcxwtQLjkjoJskWXMIXRyoOYSLUMI/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxNzg3Nzkv/ZmlsZS9hb19sZW5f/Y29fbG9fbnUuanBn")));
        list.add(new Product("Áo dài", 45, Arrays.asList("https://imgs.search.brave.com/NALIw-mSjKwuO0a0zIUbPIoxBcyF_cqYvoYRqlckWEI/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90YWls/b2N3ZWRkaW5nLnZu/L3dwLWNvbnRlbnQv/dXBsb2Fkcy8yMDIz/LzA0L0FuaC1tYXUt/YW8tZGFpLXR1eWV0/LWRlcC01MzN4ODAw/LmpwZw")));
        list.add(new Product("Đồ thể thao", 80, Arrays.asList("https://imgs.search.brave.com/1Yylxm02DhkadoPI486UVUP3SqUPlgiPCoQMiJXVk5M/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wcm9k/dWN0LmhzdGF0aWMu/bmV0LzIwMDAwMDI0/Nzk2OS9wcm9kdWN0/LzJfMGU0YzBjNTY4/YzY1NGIzNjliMDEx/ZDY1OGNkMWQ2Y2Vf/bGFyZ2UuanBn")));
        return list;
    }

    // String productName, int price, String categoryId, List<String> images, int priceBeforeDiscount, String ratings

    public List<Product> getListProduct() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Áo hoodie trắng", 200000, "0", Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc"), 250000, "4.5"));
        list.add(new Product("Quần jean rách gối", 350000, "0", Arrays.asList("https://imgs.search.brave.com/2fuynJDBZBqOkm3aQWzZOyhWdtUXuo5pXimqLkA5X34/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdC5h/cHAxaC5jb20vdXBs/b2Fkcy9pbWFnZXMv/Y29tcGFueTcyL2lt/YWdlcy9xdWElQ0Ml/ODIlQ0MlODBuJTIw/amVhbiUyMG5hbSUy/MHhhbmglMjByYSVD/QyU4MWNoJTIwZ28l/Q0MlODIlQ0MlODFp/LmpwZw"), 400000, "4"));
        list.add(new Product("Áo thun basic", 180000, "0", Arrays.asList("https://imgs.search.brave.com/HN2TcsoJTo0O3rs_RWIGtESaqfuZ2OEpZ7v1PfMZ7Qo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L2NhY2hlL3cxMjAw/L3RzL3Byb2R1Y3Qv/ZTIvZDMvOTQvMzNi/ODQ4NTA0MTg3NmFh/NjMyZmE1NThmNGYz/MWVhZTUuanBn"), 220000, "3.7"));
        list.add(new Product("Quần tây công sở", 300000, "0", Arrays.asList("https://imgs.search.brave.com/BJdyrFwzMw7JaJiz9lgai0WBuBEBxwmSaa79Qwkscac/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aG9p/dHJhbmd0aGFpaG9h/LmNvbS9zdG9yYWdl/L2NhY2hlL2NhdGFs/b2cvcHJvZHVjdC9x/dWFuX3N1b25nX251/X3RoYWlfaG9hXzI0/NS0zXzQyNXgud2Vi/cA"), 350000, "5"));
        list.add(new Product("Áo sơ mi trắng", 250000, "0", Arrays.asList("https://imgs.search.brave.com/VL0DICqtCFgLeoSO1WMyqhLRYR5PmW51Aq1rleFc32g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9i/ZTMxNTktNjYyL3Bz/LzIwMjQwOTE5X0ZP/Z1hEMHJuUFUuanBl/Zw"), 290000, "4"));
        list.add(new Product("Quần short kaki", 220000, "0", Arrays.asList("https://imgs.search.brave.com/xQrKzjDW-jBmVpAGkTaNNo_LKrfzUDs9zovR-AEb808/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9k/MGYzY2EtNzEzNi9w/cy8yMDI0MTAyOF9T/N1NUelJDYVU5Lmpw/ZWc"), 260000, "4"));
        list.add(new Product("Áo polo nam", 270000, "0", Arrays.asList("https://imgs.search.brave.com/L0feDn2RlyfUQanIqQ88rmco-BW_3ugxtXWjO0TeBQo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb2xv/bWFub3Iudm4vY2Ru/L3Nob3AvZmlsZXMv/YW8tcG9sby1uYW0t/YWRlbi0wLmpwZz92/PTE3MzkyNTk1OTgm/d2lkdGg9MTIwMA"), 310000, "5"));
        list.add(new Product("Quần jogger thể thao", 280000, "0", Arrays.asList("https://imgs.search.brave.com/D7_y8EybRuubjgIEAEBwu7557oNAwc3s7fu14ZJTqI0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YTMuY29vbG1hdGUu/bWUvY2RuLWNnaS9p/bWFnZS93aWR0aD00/MjAscXVhbGl0eT05/MCxmb3JtYXQ9YXV0/by91cGxvYWRzL0Rl/Y2VtYmVyMjAyNC9x/dWFuLWpvZ2dlcnMt/dGhlLXRoYW8tZGFp/bHktd2Vhci0xLmpw/Zw"), 320000, "4"));
        list.add(new Product("Áo khoác gió", 400000, "0", Arrays.asList("https://imgs.search.brave.com/wG9GXHLlFRy6IWS4lmh267jf1gaM_T5G7EUbNh3ixkU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L3RzL2JyaWNrdjJv/Zy8yMC83NC83ZS8x/ZWMzMDUwZDUxOTc2/Y2IyY2VjMDAyODRj/YjQwNzhiMy53ZWJw"), 450000, "5"));
        list.add(new Product("Quần jean skinny", 330000, "0", Arrays.asList("https://imgs.search.brave.com/UsDlb9Q54Hog4ocQIFMFR-xQZcqZwwyg7rEJC3P-wyc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxODQ2MDEv/ZmlsZS90b3AtNS1j/YWNoLW1peC1xdWFu/LXNraW5ueS1qZWFu/cy1uYW0tZGVwLWNo/dWFuLXNpZXUtbWF1/LTNfZGEzY2MzMWU0/MDQ4NDk1NzkyODMy/OWFkODhlZTQ3NmVf/Z3JhbmRlLmpwZw"), 380000, "5"));

        list.add(new Product("Giày Nam", 200000, "1", Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc"), 250000, "5"));
        list.add(new Product("Giày Nữ", 350000, "1", Arrays.asList("https://imgs.search.brave.com/2fuynJDBZBqOkm3aQWzZOyhWdtUXuo5pXimqLkA5X34/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdC5h/cHAxaC5jb20vdXBs/b2Fkcy9pbWFnZXMv/Y29tcGFueTcyL2lt/YWdlcy9xdWElQ0Ml/ODIlQ0MlODBuJTIw/amVhbiUyMG5hbSUy/MHhhbmglMjByYSVD/QyU4MWNoJTIwZ28l/Q0MlODIlQ0MlODFp/LmpwZw"), 400000, "4"));
        list.add(new Product("Giày tre em", 180000, "1", Arrays.asList("https://imgs.search.brave.com/HN2TcsoJTo0O3rs_RWIGtESaqfuZ2OEpZ7v1PfMZ7Qo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L2NhY2hlL3cxMjAw/L3RzL3Byb2R1Y3Qv/ZTIvZDMvOTQvMzNi/ODQ4NTA0MTg3NmFh/NjMyZmE1NThmNGYz/MWVhZTUuanBn"), 220000, "4"));
        list.add(new Product("Giay thể thao", 300000, "1", Arrays.asList("https://imgs.search.brave.com/BJdyrFwzMw7JaJiz9lgai0WBuBEBxwmSaa79Qwkscac/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aG9p/dHJhbmd0aGFpaG9h/LmNvbS9zdG9yYWdl/L2NhY2hlL2NhdGFs/b2cvcHJvZHVjdC9x/dWFuX3N1b25nX251/X3RoYWlfaG9hXzI0/NS0zXzQyNXgud2Vi/cA"), 350000, "5"));
        list.add(new Product("Giay thể thao", 250000, "1", Arrays.asList("https://imgs.search.brave.com/VL0DICqtCFgLeoSO1WMyqhLRYR5PmW51Aq1rleFc32g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9i/ZTMxNTktNjYyL3Bz/LzIwMjQwOTE5X0ZP/Z1hEMHJuUFUuanBl/Zw"), 290000, "4"));
        list.add(new Product("Giay thể thao", 220000, "1", Arrays.asList("https://imgs.search.brave.com/xQrKzjDW-jBmVpAGkTaNNo_LKrfzUDs9zovR-AEb808/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9k/MGYzY2EtNzEzNi9w/cy8yMDI0MTAyOF9T/N1NUelJDYVU5Lmpw/ZWc"), 260000, "4"));
        list.add(new Product("Giay thể thao", 270000, "1", Arrays.asList("https://imgs.search.brave.com/L0feDn2RlyfUQanIqQ88rmco-BW_3ugxtXWjO0TeBQo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb2xv/bWFub3Iudm4vY2Ru/L3Nob3AvZmlsZXMv/YW8tcG9sby1uYW0t/YWRlbi0wLmpwZz92/PTE3MzkyNTk1OTgm/d2lkdGg9MTIwMA"), 310000, "5"));
        list.add(new Product("Giay thể thao", 280000, "1", Arrays.asList("https://imgs.search.brave.com/D7_y8EybRuubjgIEAEBwu7557oNAwc3s7fu14ZJTqI0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YTMuY29vbG1hdGUu/bWUvY2RuLWNnaS9p/bWFnZS93aWR0aD00/MjAscXVhbGl0eT05/MCxmb3JtYXQ9YXV0/by91cGxvYWRzL0Rl/Y2VtYmVyMjAyNC9x/dWFuLWpvZ2dlcnMt/dGhlLXRoYW8tZGFp/bHktd2Vhci0xLmpw/Zw"), 320000, "4"));
        list.add(new Product("Giày Nữ", 400000, "1", Arrays.asList("https://imgs.search.brave.com/wG9GXHLlFRy6IWS4lmh267jf1gaM_T5G7EUbNh3ixkU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L3RzL2JyaWNrdjJv/Zy8yMC83NC83ZS8x/ZWMzMDUwZDUxOTc2/Y2IyY2VjMDAyODRj/YjQwNzhiMy53ZWJw"), 450000, "5"));
        list.add(new Product("Giày Nữ", 330000, "1", Arrays.asList("https://imgs.search.brave.com/UsDlb9Q54Hog4ocQIFMFR-xQZcqZwwyg7rEJC3P-wyc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxODQ2MDEv/ZmlsZS90b3AtNS1j/YWNoLW1peC1xdWFu/LXNraW5ueS1qZWFu/cy1uYW0tZGVwLWNo/dWFuLXNpZXUtbWF1/LTNfZGEzY2MzMWU0/MDQ4NDk1NzkyODMy/OWFkODhlZTQ3NmVf/Z3JhbmRlLmpwZw"), 380000, "5"));

        list.add(new Product("Kính", 200000, "2", Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc"), 250000, "5"));
        list.add(new Product("Đồng Ho", 350000, "2", Arrays.asList("https://imgs.search.brave.com/2fuynJDBZBqOkm3aQWzZOyhWdtUXuo5pXimqLkA5X34/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdC5h/cHAxaC5jb20vdXBs/b2Fkcy9pbWFnZXMv/Y29tcGFueTcyL2lt/YWdlcy9xdWElQ0Ml/ODIlQ0MlODBuJTIw/amVhbiUyMG5hbSUy/MHhhbmglMjByYSVD/QyU4MWNoJTIwZ28l/Q0MlODIlQ0MlODFp/LmpwZw"), 400000, "4"));
        list.add(new Product("Kính", 180000, "2", Arrays.asList("https://imgs.search.brave.com/HN2TcsoJTo0O3rs_RWIGtESaqfuZ2OEpZ7v1PfMZ7Qo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L2NhY2hlL3cxMjAw/L3RzL3Byb2R1Y3Qv/ZTIvZDMvOTQvMzNi/ODQ4NTA0MTg3NmFh/NjMyZmE1NThmNGYz/MWVhZTUuanBn"), 220000, "4"));
        list.add(new Product("Kính", 300000, "2", Arrays.asList("https://imgs.search.brave.com/BJdyrFwzMw7JaJiz9lgai0WBuBEBxwmSaa79Qwkscac/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aG9p/dHJhbmd0aGFpaG9h/LmNvbS9zdG9yYWdl/L2NhY2hlL2NhdGFs/b2cvcHJvZHVjdC9x/dWFuX3N1b25nX251/X3RoYWlfaG9hXzI0/NS0zXzQyNXgud2Vi/cA"), 350000, "5"));
        list.add(new Product("Kính", 250000, "2", Arrays.asList("https://imgs.search.brave.com/VL0DICqtCFgLeoSO1WMyqhLRYR5PmW51Aq1rleFc32g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9i/ZTMxNTktNjYyL3Bz/LzIwMjQwOTE5X0ZP/Z1hEMHJuUFUuanBl/Zw"), 290000, "4"));
        list.add(new Product("Kính", 220000, "2", Arrays.asList("https://imgs.search.brave.com/xQrKzjDW-jBmVpAGkTaNNo_LKrfzUDs9zovR-AEb808/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9k/MGYzY2EtNzEzNi9w/cy8yMDI0MTAyOF9T/N1NUelJDYVU5Lmpw/ZWc"), 260000, "4"));
        list.add(new Product("Kính", 270000, "2", Arrays.asList("https://imgs.search.brave.com/L0feDn2RlyfUQanIqQ88rmco-BW_3ugxtXWjO0TeBQo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb2xv/bWFub3Iudm4vY2Ru/L3Nob3AvZmlsZXMv/YW8tcG9sby1uYW0t/YWRlbi0wLmpwZz92/PTE3MzkyNTk1OTgm/d2lkdGg9MTIwMA"), 310000, "5"));
        list.add(new Product("Kính", 280000, "2", Arrays.asList("https://imgs.search.brave.com/D7_y8EybRuubjgIEAEBwu7557oNAwc3s7fu14ZJTqI0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YTMuY29vbG1hdGUu/bWUvY2RuLWNnaS9p/bWFnZS93aWR0aD00/MjAscXVhbGl0eT05/MCxmb3JtYXQ9YXV0/by91cGxvYWRzL0Rl/Y2VtYmVyMjAyNC9x/dWFuLWpvZ2dlcnMt/dGhlLXRoYW8tZGFp/bHktd2Vhci0xLmpw/Zw"), 320000, "4"));
        list.add(new Product("Kính", 400000, "2", Arrays.asList("https://imgs.search.brave.com/wG9GXHLlFRy6IWS4lmh267jf1gaM_T5G7EUbNh3ixkU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L3RzL2JyaWNrdjJv/Zy8yMC83NC83ZS8x/ZWMzMDUwZDUxOTc2/Y2IyY2VjMDAyODRj/YjQwNzhiMy53ZWJw"), 450000, "5"));
        list.add(new Product("Kính", 330000, "2", Arrays.asList("https://imgs.search.brave.com/UsDlb9Q54Hog4ocQIFMFR-xQZcqZwwyg7rEJC3P-wyc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxODQ2MDEv/ZmlsZS90b3AtNS1j/YWNoLW1peC1xdWFu/LXNraW5ueS1qZWFu/cy1uYW0tZGVwLWNo/dWFuLXNpZXUtbWF1/LTNfZGEzY2MzMWU0/MDQ4NDk1NzkyODMy/OWFkODhlZTQ3NmVf/Z3JhbmRlLmpwZw"), 380000, "5"));

        list.add(new Product("Son", 200000, "3", Arrays.asList("https://imgs.search.brave.com/MDZsD7FkUd4PtBeWW6KDOrz6LU9-QjPoA5SHxdhexN0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9idW1z/aG9wLmNvbS52bi93/cC1jb250ZW50L3Vw/bG9hZHMvMjAxOC8w/OS9tYXUtYW8taG9v/ZGllLWRlcC0yLmpw/ZWc"), 250000, "5"));
        list.add(new Product("Son", 350000, "3", Arrays.asList("https://imgs.search.brave.com/2fuynJDBZBqOkm3aQWzZOyhWdtUXuo5pXimqLkA5X34/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zdC5h/cHAxaC5jb20vdXBs/b2Fkcy9pbWFnZXMv/Y29tcGFueTcyL2lt/YWdlcy9xdWElQ0Ml/ODIlQ0MlODBuJTIw/amVhbiUyMG5hbSUy/MHhhbmglMjByYSVD/QyU4MWNoJTIwZ28l/Q0MlODIlQ0MlODFp/LmpwZw"), 400000, "4"));
        list.add(new Product("Son", 180000, "3", Arrays.asList("https://imgs.search.brave.com/HN2TcsoJTo0O3rs_RWIGtESaqfuZ2OEpZ7v1PfMZ7Qo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L2NhY2hlL3cxMjAw/L3RzL3Byb2R1Y3Qv/ZTIvZDMvOTQvMzNi/ODQ4NTA0MTg3NmFh/NjMyZmE1NThmNGYz/MWVhZTUuanBn"), 220000, "4"));
        list.add(new Product("Son", 300000, "3", Arrays.asList("https://imgs.search.brave.com/BJdyrFwzMw7JaJiz9lgai0WBuBEBxwmSaa79Qwkscac/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aG9p/dHJhbmd0aGFpaG9h/LmNvbS9zdG9yYWdl/L2NhY2hlL2NhdGFs/b2cvcHJvZHVjdC9x/dWFuX3N1b25nX251/X3RoYWlfaG9hXzI0/NS0zXzQyNXgud2Vi/cA"), 350000, "5"));
        list.add(new Product("Son", 250000, "3", Arrays.asList("https://imgs.search.brave.com/VL0DICqtCFgLeoSO1WMyqhLRYR5PmW51Aq1rleFc32g/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9i/ZTMxNTktNjYyL3Bz/LzIwMjQwOTE5X0ZP/Z1hEMHJuUFUuanBl/Zw"), 290000, "4"));
        list.add(new Product("Son", 220000, "3", Arrays.asList("https://imgs.search.brave.com/xQrKzjDW-jBmVpAGkTaNNo_LKrfzUDs9zovR-AEb808/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb3Mu/bnZuY2RuLmNvbS9k/MGYzY2EtNzEzNi9w/cy8yMDI0MTAyOF9T/N1NUelJDYVU5Lmpw/ZWc"), 260000, "4"));
        list.add(new Product("Son", 270000, "3", Arrays.asList("https://imgs.search.brave.com/L0feDn2RlyfUQanIqQ88rmco-BW_3ugxtXWjO0TeBQo/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9wb2xv/bWFub3Iudm4vY2Ru/L3Nob3AvZmlsZXMv/YW8tcG9sby1uYW0t/YWRlbi0wLmpwZz92/PTE3MzkyNTk1OTgm/d2lkdGg9MTIwMA"), 310000, "5"));
        list.add(new Product("Son", 280000, "3", Arrays.asList("https://imgs.search.brave.com/D7_y8EybRuubjgIEAEBwu7557oNAwc3s7fu14ZJTqI0/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YTMuY29vbG1hdGUu/bWUvY2RuLWNnaS9p/bWFnZS93aWR0aD00/MjAscXVhbGl0eT05/MCxmb3JtYXQ9YXV0/by91cGxvYWRzL0Rl/Y2VtYmVyMjAyNC9x/dWFuLWpvZ2dlcnMt/dGhlLXRoYW8tZGFp/bHktd2Vhci0xLmpw/Zw"), 320000, "4"));
        list.add(new Product("Son", 400000, "3", Arrays.asList("https://imgs.search.brave.com/wG9GXHLlFRy6IWS4lmh267jf1gaM_T5G7EUbNh3ixkU/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9zYWx0/LnRpa2ljZG4uY29t/L3RzL2JyaWNrdjJv/Zy8yMC83NC83ZS8x/ZWMzMDUwZDUxOTc2/Y2IyY2VjMDAyODRj/YjQwNzhiMy53ZWJw"), 450000, "5"));
        list.add(new Product("Son", 330000, "3", Arrays.asList("https://imgs.search.brave.com/UsDlb9Q54Hog4ocQIFMFR-xQZcqZwwyg7rEJC3P-wyc/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9maWxl/LmhzdGF0aWMubmV0/LzEwMDAxODQ2MDEv/ZmlsZS90b3AtNS1j/YWNoLW1peC1xdWFu/LXNraW5ueS1qZWFu/cy1uYW0tZGVwLWNo/dWFuLXNpZXUtbWF1/LTNfZGEzY2MzMWU0/MDQ4NDk1NzkyODMy/OWFkODhlZTQ3NmVf/Z3JhbmRlLmpwZw"), 380000, "5"));
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
