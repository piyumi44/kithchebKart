package com.PROJECT.kitchenkart.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.PROJECT.kitchenkart.Activities.AddProductActivity; // For adding/editing products
import com.PROJECT.kitchenkart.Adapters.FoodProductAdapter;
import com.PROJECT.kitchenkart.Models.FoodProduct;
import com.PROJECT.kitchenkart.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/** @noinspection ALL*/
public class ManageProductsFragment extends Fragment implements FoodProductAdapter.OnProductActionListener {

    private RecyclerView recyclerViewProducts;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddProduct;
    private TextView tvNoProducts; // Added for when there are no products

    private FoodProductAdapter foodProductAdapter;
    private List<FoodProduct> productList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private CollectionReference productsRef;

    public ManageProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_products, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Ensure user is logged in
        if (currentUser == null) {
            // Handle case where user is not logged in, perhaps redirect to Login
            Toast.makeText(getContext(), "Please log in to manage products.", Toast.LENGTH_SHORT).show();
            // Optional: Redirect to LoginActivity
            // startActivity(new Intent(getActivity(), LoginActivity.class));
            // getActivity().finish();
            return view; // Return early
        }

        // Reference to the products collection for the current seller
        // Assuming products are stored under a 'sellers' collection, then sellerId, then 'products'
        // OR a flat 'products' collection with a 'sellerId' field
        productsRef = db.collection("products"); // Assuming 'products' is the collection

        // Initialize UI components
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        progressBar = view.findViewById(R.id.progressBar);
        fabAddProduct = view.findViewById(R.id.fabAddProduct);
        tvNoProducts = view.findViewById(R.id.tvNoProducts); // Initialize no products TextView


        // Setup RecyclerView
        productList = new ArrayList<>();
        foodProductAdapter = new FoodProductAdapter(getContext(), productList, this); // 'this' for listener
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProducts.setAdapter(foodProductAdapter);

        // Set up FAB click listener for adding new products
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddProductActivity.class);
            startActivity(intent);
        });

        // Load products when the fragment is created
        loadProducts();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload products every time the fragment becomes visible (e.g., after returning from AddProductActivity)
        loadProducts();
    }

    private void loadProducts() {
        if (currentUser == null) return; // Should already be handled in onCreateView

        progressBar.setVisibility(View.VISIBLE);
        tvNoProducts.setVisibility(View.GONE); // Hide no products message initially
        productList.clear(); // Clear existing list to avoid duplicates

        productsRef
                .whereEqualTo("sellerId", currentUser.getUid()) // Filter by current seller's UID
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FoodProduct product = document.toObject(FoodProduct.class);
                            // Set the document ID as the product ID for easy referencing during edit/delete
                            product.setProductId(document.getId());
                            productList.add(product);
                        }
                        foodProductAdapter.notifyDataSetChanged();

                        if (productList.isEmpty()) {
                            tvNoProducts.setVisibility(View.VISIBLE); // Show message if no products
                        } else {
                            tvNoProducts.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(getContext(), "Error loading products: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        tvNoProducts.setVisibility(View.VISIBLE); // Show message on error too
                    }
                });
    }

    // --- FoodProductAdapter.OnProductActionListener implementation ---

    @Override
    public void onEditClick(FoodProduct product) {
        // Navigate to AddProductActivity (or a dedicated EditProductActivity) for editing
        Intent intent = new Intent(getActivity(), AddProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getProductId()); // Pass product ID
        // You might also pass other product data if AddProductActivity is complex
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(FoodProduct product) {
        // Implement delete confirmation dialog for better UX
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete '" + product.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> deleteProduct(product))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteProduct(FoodProduct product) {
        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            // Your logic here
        }

        progressBar.setVisibility(View.VISIBLE);
        productsRef.document(product.getProductId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), product.getName() + " deleted successfully!", Toast.LENGTH_SHORT).show();
                    loadProducts(); // Reload the list after deletion
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error deleting product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}