package com.PROJECT.kitchenkart.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.PROJECT.kitchenkart.Adapters.OrderAdapter; // Reusing OrderAdapter
import com.PROJECT.kitchenkart.Models.Order; // Reusing Order model
import com.PROJECT.kitchenkart.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** @noinspection ALL*/
public class BuyerOrdersFragment extends Fragment implements OrderAdapter.OnOrderActionListener {

    private RecyclerView recyclerViewBuyerOrders;
    private OrderAdapter orderAdapter;
    private List<Order> buyerOrderList;

    public BuyerOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buyer_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.fragment_title);
        if (textView != null) {
            textView.setText(R.string.bottom_nav_orders);
        }

        recyclerViewBuyerOrders = view.findViewById(R.id.recyclerViewBuyerOrders);
        recyclerViewBuyerOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        buyerOrderList = new ArrayList<>();
        populateSampleBuyerOrders(); // Populate with sample data
        orderAdapter = new OrderAdapter(getContext(), buyerOrderList, this);
        recyclerViewBuyerOrders.setAdapter(orderAdapter);
    }

    private void populateSampleBuyerOrders() {
        // Sample orders for a buyer
        buyerOrderList.add(new Order("B001", "You (Buyer)", 850.00, "2025-07-23 15:00", "Pending", Arrays.asList("Kottu Roti", "Watalappan"), "Your Address, Colombo"));
        buyerOrderList.add(new Order("B002", "You (Buyer)", 300.00, "2025-07-22 10:45", "Accepted", Arrays.asList("Hoppers x3"), "Your Address, Colombo"));
        buyerOrderList.add(new Order("B003", "You (Buyer)", 600.00, "2025-07-21 09:30", "Delivered", Arrays.asList("Lamprais"), "Your Address, Colombo"));
        buyerOrderList.add(new Order("B004", "You (Buyer)", 180.00, "2025-07-20 18:00", "Cancelled", Arrays.asList("Pol Sambol"), "Your Address, Colombo"));
    }

    @Override
    public void onAcceptClick(Order order) {
        Toast.makeText(getContext(), "Buyer cannot accept/reject orders.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRejectClick(Order order) {
        Toast.makeText(getContext(), "Buyer cannot accept/reject orders.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkDeliveredClick(Order order) {
        Toast.makeText(getContext(), "Buyer cannot mark orders delivered.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetailsClick(Order order) {
        Toast.makeText(getContext(), "Buyer viewing details for Order " + order.getOrderId(), Toast.LENGTH_SHORT).show();
        // TODO: Start OrderDetailsActivity for buyer's view
    }
}