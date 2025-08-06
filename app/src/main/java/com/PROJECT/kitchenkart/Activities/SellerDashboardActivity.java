package com.PROJECT.kitchenkart.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Import MenuItem

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Import Toolbar
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment; // Import Fragment
import androidx.fragment.app.FragmentTransaction; // Import FragmentTransaction

// Assuming you have these fragments for SellerDashboard
import com.PROJECT.kitchenkart.Fragments.ManageProductsFragment; // You might need to create these
import com.PROJECT.kitchenkart.Fragments.CurrentOrdersFragment;
import com.PROJECT.kitchenkart.Fragments.SalesAnalyticsFragment;
// import com.PROJECT.kitchenkart.Fragments.ProfileFragment; // Only if you want to embed ProfileFragment
import com.PROJECT.kitchenkart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView; // Import BottomNavigationView

public class SellerDashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_dashboard); // You'll need this layout

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.seller_dashboard_title); // Set seller dashboard title
        }

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation); // Assuming same ID as buyer dashboard

        // Set listener for bottom navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Load the default fragment (e.g., ManageProductsFragment)
        if (savedInstanceState == null) {
            loadFragment(new ManageProductsFragment()); // You'll need this fragment
            bottomNavigationView.setSelectedItemId(R.id.nav_manage_products); // Set selected item on start
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_manage_products) { // Example for seller-specific nav
            loadFragment(new ManageProductsFragment());
            toolbar.setTitle(R.string.bottom_nav_manage_products);
            return true;
        } else if (itemId == R.id.nav_seller_orders) { // Example
            loadFragment(new CurrentOrdersFragment());
            toolbar.setTitle(R.string.bottom_nav_seller_orders);
            return true;
        } else if (itemId == R.id.nav_sales_analytics) { // Example
            loadFragment(new SalesAnalyticsFragment());
            toolbar.setTitle(R.string.bottom_nav_sales_analytics);
            return true;
        } else if (itemId == R.id.nav_seller_profile) { // This will be your profile link for seller
            // LAUNCH PROFILEACTIVITY AS A SEPARATE ACTIVITY FOR SELLER
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true; // Mark as handled
        } else {
            return false; // Item not recognized
        }
    }

    private void loadFragment(Fragment fragment) {
        // Begin the transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.fragment_container, fragment);
        // Commit the transaction
        transaction.commit();
    }
}