package com.PROJECT.kitchenkart.Models;

/** @noinspection ALL*/
public class FoodProduct {
    private String id; // This will store the Firestore document ID
    private String sellerId; // Added to link product to a specific seller
    private String name;
    private String description;
    private double price;
    private String imageUrl; // For displaying product image

    public FoodProduct(String number, String polSambol, String s, double v, String url) {
        // Default constructor required for Firebase or other deserialization
    }

    // Constructor for creating a new FoodProduct object
    public FoodProduct(String id, String sellerId, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.sellerId = sellerId; // Initialize sellerId
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters

    // Use 'id' to represent the Firestore document ID.
    // When fetching from Firestore, you would typically set this 'id'
    // after getting the document snapshot ID.
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // This method is correctly named and should be used to set the document ID.
    // It's equivalent to setId(String id) but named 'setProductId' for clarity
    // in the context of our product management.
    public void setProductId(String productId) {
        this.id = productId; // Assign to the 'id' field
    }

    // This method should return the String product ID.
    // It's equivalent to getId() but named 'getProductId' for clarity.
    public String getProductId() {
        return this.id; // Return the 'id' field
    }

    // New: Getter and Setter for sellerId
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}