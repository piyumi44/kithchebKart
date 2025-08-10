package com.PROJECT.kitchenkart.Activities;

public class products {
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

    public class CheckoutActivity extends AppCompatActivity {

        private LinearLayout itemsContainer;
        private Button addProductButton;
        private List<Product> productList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_checkout);

            itemsContainer = findViewById(R.id.itemsContainer);
            addProductButton = findViewById(R.id.addProductButton);

            // Initialize product list with sample data
            productList = new ArrayList<>();
            productList.add(new Product("Pol Sambal & String Helpers", 150.00));
            productList.add(new Product("Pittu", 100.00));
            productList.add(new Product("Pol Rotti", 50.00));
            productList.add(new Product("Tose", 80.00));
            productList.add(new Product("Milk Rice", 80.00));

            // Display products
            displayProducts();

            addProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle add new product button click
                    // You can implement this functionality as needed
                }
            });
        }

        private void displayProducts() {
            itemsContainer.removeAllViews();

            for (Product product : productList) {
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_product, itemsContainer, false);

                TextView description = itemView.findViewById(R.id.productDescription);
                TextView price = itemView.findViewById(R.id.productPrice);

                description.setText(product.getName());
                price.setText("Rs." + String.format("%.2f", product.getPrice()));

                itemsContainer.addView(itemView);
            }
        }

        // Product model class
        private static class Product {
            private String name;
            private double price;

            public Product(String name, double price) {
                this.name = name;
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public double getPrice() {
                return price;
            }
        }
    }

}
