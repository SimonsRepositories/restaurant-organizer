package com.example.restaurantorganizer.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.restaurantorganizer.model.OrderItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    public OrderService(Context context) {
        this.mPrefs = context.getSharedPreferences("orderItems", Context.MODE_PRIVATE);
    }

    private SharedPreferences  mPrefs;

    private List<OrderItem> orderItems = new ArrayList<>();

    private void writeOrderItems() {

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(orderItems);
        prefsEditor.putString("orderItems", json);
        prefsEditor.apply();
    }

    private void readOrderItems() {
        String json = mPrefs.getString("orderItems", "[]");
        orderItems = new Gson().fromJson(json, List.class);
        System.out.println();
    }


    public void addOrderItem(OrderItem orderItem) {
        readOrderItems();
        orderItems.add(orderItem);
        writeOrderItems();
    }

    public List<OrderItem> getOrderItems() {
        readOrderItems();
        return orderItems;
    }
}
