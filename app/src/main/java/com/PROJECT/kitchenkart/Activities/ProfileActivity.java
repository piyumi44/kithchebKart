package com.PROJECT.kitchenkart.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap; // Primary import for Bitmap
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import com.PROJECT.kitchenkart.Models.User;
import com.PROJECT.kitchenkart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
// import com.google.firebase.storage.UploadTask; // Often unused if listeners are chained directly. Keep if explicitly needed.

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Removed duplicate import: import android.graphics.Bitmap;
import com.bumptech.glide.Glide;

/** @noinspection ALL*/
public class ProfileActivity extends AppCompatActivity {

    private TextView tvEmail;
    private TextInputEditText etName, etPhone, etAddress;
    private Button btnUpdateProfile;
    private ProgressBar progressBar;
    private ShapeableImageView ivProfilePicture;
    private TextView tvChangeProfilePicture;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private DocumentReference userDocRef;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ActivityResultLauncher<String[]> requestPermissionsLauncher;
    private ActivityResultLauncher<String> pickImageFromGalleryLauncher;

    // FIX: Changed from <Void> to <Bitmap>
    private ActivityResultLauncher<Void> takePictureLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Check if user is logged in
        if (currentUser == null) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Get user document reference
        userDocRef = db.collection("users").document(currentUser.getUid());

        // Initialize UI components
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvEmail = findViewById(R.id.tvEmail);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        progressBar = findViewById(R.id.progressBar);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvChangeProfilePicture = findViewById(R.id.tvChangeProfilePicture);

        // --- Initialize ActivityResultLaunchers ---

        // 1. For requesting runtime permissions
        requestPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean allGranted = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        allGranted = permissions.getOrDefault(Manifest.permission.READ_MEDIA_IMAGES, false) &&
                                permissions.getOrDefault(Manifest.permission.CAMERA, false);
                    } else {
                        allGranted = permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false) &&
                                permissions.getOrDefault(Manifest.permission.CAMERA, false);
                    }

                    if (allGranted) {
                        showImageSourceDialog();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Permissions denied. Cannot access gallery or camera.", Toast.LENGTH_LONG).show();
                    }
                }
        );

        // 2. For picking an image from the gallery
        pickImageFromGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                imageUri -> {
                    if (imageUri != null) {
                        ivProfilePicture.setImageURI(imageUri);
                        uploadImageToFirebaseStorage(imageUri);
                    } else {
                        Toast.makeText(ProfileActivity.this, "No image selected.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // 3. For taking a photo with the camera (returns Bitmap thumbnail)
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(), // Returns Bitmap
                bitmap -> {
                    if (bitmap != null) {
                        ivProfilePicture.setImageBitmap(bitmap);
                        uploadImageToFirebaseStorage(bitmap);
                    } else {
                        Toast.makeText(ProfileActivity.this, "No photo taken.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // --- Set Listeners ---
        loadUserProfile();
        btnUpdateProfile.setOnClickListener(v -> updateUserProfile());
        ivProfilePicture.setOnClickListener(v -> checkAndRequestImagePermissions());
        tvChangeProfilePicture.setOnClickListener(v -> checkAndRequestImagePermissions());
    }

    // Handle back button press in Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmail.setText(currentUser.getEmail());

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            etName.setText(user.getName());
                            etPhone.setText(user.getPhone());
                            etAddress.setText(user.getAddress());

                            // Load profile picture using Glide if URL exists
                            if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
                                Glide.with(ProfileActivity.this)
                                        .load(user.getProfilePictureUrl())
                                        .placeholder(R.drawable.ic_person_24)
                                        .error(R.drawable.ic_person_24)
                                        .into(ivProfilePicture);
                            } else {
                                ivProfilePicture.setImageResource(R.drawable.ic_person_24);
                            }
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "User profile data not found. Please complete your details.", Toast.LENGTH_LONG).show();
                        ivProfilePicture.setImageResource(R.drawable.ic_person_24);
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Error loading profile: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                    ivProfilePicture.setImageResource(R.drawable.ic_person_24);
                }
            }
        });
    }

    private void updateUserProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Full Name is required");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Delivery Address is required");
            etAddress.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnUpdateProfile.setEnabled(false);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);
        updates.put("address", address);

        userDocRef.update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        btnUpdateProfile.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Error updating profile: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // --- Permission and Image Picking Logic ---

    private void checkAndRequestImagePermissions() {
        List<String> permissionsToRequest = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toArray(new String[0]));
        } else {
            showImageSourceDialog();
        }
    }

    private void showImageSourceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Select Image Source")
                .setItems(new CharSequence[]{"Gallery", "Camera"}, (dialog, which) -> {
                    if (which == 0) {
                        openGallery();
                    } else {
                        openCamera();
                    }
                })
                .show();
    }

    private void openGallery() {
        pickImageFromGalleryLauncher.launch("image/*");
    }

    private void openCamera() {
        takePictureLauncher.launch(null);
    }

    // --- Firebase Storage Upload and Firestore Update Methods ---

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(ProfileActivity.this, "No image URI to upload.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        StorageReference ref = storageReference.child("profile_pictures/" + currentUser.getUid() + ".jpg");

        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        updateProfilePictureUrlInFirestore(downloadUrl);
                    }).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void uploadImageToFirebaseStorage(Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(ProfileActivity.this, "No bitmap to upload.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] data = baos.toByteArray();

        StorageReference ref = storageReference.child("profile_pictures/" + currentUser.getUid() + ".jpg");

        ref.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        updateProfilePictureUrlInFirestore(downloadUrl);
                    }).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void updateProfilePictureUrlInFirestore(String url) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("profilePictureUrl", url);

        userDocRef.update(updates)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Profile picture URL saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to save picture URL to profile: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                    }
                });
    }
}