// Your provided FoodProductAdapter.java
package com.PROJECT.kitchenkart.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // Not used in this snippet, but fine to keep

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.PROJECT.kitchenkart.Models.FoodProduct;
import com.PROJECT.kitchenkart.R;
// import com.bumptech.glide.Glide; // Uncomment if using Glide
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FoodProductAdapter extends RecyclerView.Adapter<FoodProductAdapter.ProductViewHolder> {

    private final Context context;
    private final List<FoodProduct> productList;
    private final OnProductActionListener listener;

    public interface OnProductActionListener {
        void onEditClick(FoodProduct product);
        void onDeleteClick(FoodProduct product);
    }

    public FoodProductAdapter(Context context, List<FoodProduct> productList, OnProductActionListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_product, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        FoodProduct product = productList.get(position);

        // These are the lines where the NullPointerException would occur if the TextViews are null
        holder.textViewFoodName.setText(product.getName());
        holder.textViewFoodDescription.setText(product.getDescription());
        holder.textViewFoodPrice.setText(String.format("LKR %.2f", product.getPrice()));

        // Load image (You'll need an image loading library like Glide or Picasso)
        // For now, use a placeholder drawable
        // Glide.with(context).load(product.getImageUrl()).placeholder(R.drawable.food_placeholder).into(holder.imageViewFood);
        holder.imageViewFood.setImageResource(R.drawable.food_placeholder); // Using a placeholder drawable

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(product);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewFood;
        final TextView textViewFoodName;
        final TextView textViewFoodDescription;
        final TextView textViewFoodPrice;
        final MaterialButton buttonEdit;
        final MaterialButton buttonDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // These findViewById calls rely on the IDs in item_food_product.xml
            imageViewFood = itemView.findViewById(R.id.imageViewFood);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewFoodDescription = itemView.findViewById(R.id.textViewFoodDescription);
            textViewFoodPrice = itemView.findViewById(R.id.textViewFoodPrice);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}