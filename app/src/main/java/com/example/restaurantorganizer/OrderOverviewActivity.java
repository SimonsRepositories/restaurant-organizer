package com.example.restaurantorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.model.OrderItem;
import com.example.restaurantorganizer.service.OrderService;

public class OrderOverviewActivity extends AppCompatActivity {

    Button backBtn;
    Button deleteBtn;
    private RecyclerView allOrderedItems;
    OrderService os;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_overview_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(OrderOverviewActivity.this, MainActivity.class));
            }
        });

        deleteBtn = findViewById(R.id.deleteItem);
        allOrderedItems = findViewById(R.id.allOrderedItems);
        os = new OrderService(this);

    }

    //Kitchen Output
    private void setupAllOrderedItems(RecyclerView recyclerView) {
        OrderOverviewAdapter orderOverviewAdapter = new OrderOverviewAdapter(this, os.getOrderItems());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderOverviewAdapter);

        orderOverviewAdapter.setClickListener((v, position) -> onOrderedItemClick(v, orderOverviewAdapter.getItem(position)));
    }

    private void onOrderedItemClick(View v, OrderItem orderItem) {
        OrderService os = new OrderService(this);
        //delete item from list
        os.getOrderItems().remove(orderItem);
        //update view
        setupAllOrderedItems(allOrderedItems);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
