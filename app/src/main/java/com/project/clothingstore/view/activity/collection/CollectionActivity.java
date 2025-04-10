package com.project.clothingstore.view.activity.collection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.project.clothingstore.R;
import com.project.clothingstore.view.fragment.productcollection.ProductCollectionItemFragment;

public class CollectionActivity extends AppCompatActivity {
    ImageButton imgbtn_back;
    ImageView img_collection;
    public String collectionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        imgbtn_back = findViewById(R.id.btn_back_BST_products);
        img_collection = findViewById(R.id.img_BST_products);

        Intent intent = getIntent();
        String collectionImg = intent.getStringExtra("collectionImg");
        collectionId = intent.getStringExtra("collectionId");
        Glide.with(this)
                .load(collectionImg) // Lấy ảnh đầu tiên trong danh sách
                .placeholder(R.drawable.spnb) // Ảnh tạm khi load
                .error(R.drawable.item) // Ảnh hiển thị nếu load lỗi
                .into(img_collection);
        if (savedInstanceState == null) {
            loadFragment(new ProductCollectionItemFragment(), collectionId);
        }

        imgbtn_back.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadFragment(Fragment fragment, String collectionId) {

        // Truyền dữ liệu từ activity sang fragment
        Bundle bundle  = new Bundle();
        bundle.putString("collectionId", collectionId);
        fragment.setArguments(bundle);
        // Thực hiện giao dịch fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_productColletion_items, fragment);
        transaction.commit();
    }
}