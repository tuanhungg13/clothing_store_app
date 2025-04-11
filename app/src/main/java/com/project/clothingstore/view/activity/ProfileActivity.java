package com.project.clothingstore.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.clothingstore.R;
import com.project.clothingstore.utils.helper.FirebaseHelper;
import com.project.clothingstore.viewmodel.UserViewModel;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPhone, edtProvince, edtDistrict, edtWard, edtStreet;
    private Button btnSave;
    private ShapeableImageView imgAvatar;
    private ImageView imgCamera;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private Uri selectedImageUri;

    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;

    private String currentAvatarUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setupViewModel();
        loadUserInfo();

        imgCamera.setOnClickListener(v -> openGallery());
        btnSave.setOnClickListener(v -> saveUserInfo());
    }

    private void initViews() {
        edtFullName = findViewById(R.id.edit_full_name);
        edtEmail = findViewById(R.id.edit_email);
        edtPhone = findViewById(R.id.edit_phone);
        edtProvince = findViewById(R.id.edit_province);
        edtDistrict = findViewById(R.id.edit_district);
        edtWard = findViewById(R.id.edit_ward);
        edtStreet = findViewById(R.id.edit_street);
        imgAvatar = findViewById(R.id.img_avatar);
        imgCamera = findViewById(R.id.img_camera);
        btnSave = findViewById(R.id.btn_save);

        edtEmail.setEnabled(false); // Không cho phép chỉnh sửa email

        mAuth = FirebaseAuth.getInstance();

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUpdateSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi khi lưu thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        userViewModel.getAvatarUploadStatus().observe(this, status -> {
            if ("success".equals(status)) {
                Toast.makeText(this, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
            } else if ("fail".equals(status)) {
                Toast.makeText(this, "Lỗi khi cập nhật ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();

        FirebaseHelper.getUserCollection().document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        edtFullName.setText(valueOrEmpty(documentSnapshot.getString("fullName")));
                        edtEmail.setText(valueOrEmpty(documentSnapshot.getString("email")));
                        edtPhone.setText(valueOrEmpty(documentSnapshot.getString("phoneNumber")));

                        Map<String, Object> address = (Map<String, Object>) documentSnapshot.get("address");
                        if (address != null) {
                            edtProvince.setText(valueOrEmpty(address.get("province")));
                            edtDistrict.setText(valueOrEmpty(address.get("district")));
                            edtWard.setText(valueOrEmpty(address.get("ward")));
                            edtStreet.setText(valueOrEmpty(address.get("street")));
                        }

                        currentAvatarUrl = documentSnapshot.getString("avatar");
                        if (currentAvatarUrl != null && !currentAvatarUrl.isEmpty()) {
                            try {
                                byte[] decodedString = Base64.decode(currentAvatarUrl, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                Glide.with(this)
                                        .load(decodedByte)
                                        .placeholder(R.drawable.avatar)
                                        .error(R.drawable.avatar)
                                        .into(imgAvatar);
                            } catch (Exception e) {
                                imgAvatar.setImageResource(R.drawable.avatar);
                                Log.e("ProfileActivity", "Lỗi khi giải mã ảnh base64", e);
                            }
                        } else {
                            imgAvatar.setImageResource(R.drawable.avatar);
                        }

                    }
                });
    }

    private void saveUserInfo() {
        if (!validateInput()) return;

        String fullName = edtFullName.getText().toString().trim();
        String phoneNumber = edtPhone.getText().toString().trim();

        Map<String, Object> addressData = new HashMap<>();
        addressData.put("province", edtProvince.getText().toString().trim());
        addressData.put("district", edtDistrict.getText().toString().trim());
        addressData.put("ward", edtWard.getText().toString().trim());
        addressData.put("street", edtStreet.getText().toString().trim());

        userViewModel.updateUserProfile(fullName, phoneNumber, addressData);

        // Nếu có ảnh mới thì upload
        if (selectedImageUri != null) {
            Log.d("ProfileActivity", "Uploading new avatar: " + selectedImageUri);
            userViewModel.uploadAvatar(this, selectedImageUri);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    private String valueOrEmpty(Object value) {
        return value != null ? value.toString() : "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imgAvatar.setImageURI(selectedImageUri); // Hiển thị trước
            }
        }
    }

    private boolean validateInput() {
        String fullName = edtFullName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String province = edtProvince.getText().toString().trim();
        String district = edtDistrict.getText().toString().trim();
        String ward = edtWard.getText().toString().trim();
        String street = edtStreet.getText().toString().trim();

        if (fullName.isEmpty()) {
            edtFullName.setError("Vui lòng nhập họ tên");
            edtFullName.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            edtPhone.setError("Vui lòng nhập số điện thoại");
            edtPhone.requestFocus();
            return false;
        }

        if (!phone.matches("^(0|\\+84)[0-9]{9}$")) {
            edtPhone.setError("Số điện thoại không hợp lệ");
            edtPhone.requestFocus();
            return false;
        }

        if (province.isEmpty()) {
            edtProvince.setError("Vui lòng nhập tỉnh/thành");
            edtProvince.requestFocus();
            return false;
        }

        if (district.isEmpty()) {
            edtDistrict.setError("Vui lòng nhập quận/huyện");
            edtDistrict.requestFocus();
            return false;
        }

        if (ward.isEmpty()) {
            edtWard.setError("Vui lòng nhập phường/xã");
            edtWard.requestFocus();
            return false;
        }

        if (street.isEmpty()) {
            edtStreet.setError("Vui lòng nhập tên đường");
            edtStreet.requestFocus();
            return false;
        }

        return true;
    }
}
