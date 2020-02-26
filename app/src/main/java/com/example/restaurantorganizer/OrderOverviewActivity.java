package com.example.restaurantorganizer;

import android.content.Context;
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

import com.example.restaurantorganizer.adapter.OrderOverviewAdapter;
import com.example.restaurantorganizer.model.OrderItem;
import com.example.restaurantorganizer.service.OrderService;
import com.example.restaurantorganizer.service.TableService;

public class OrderOverviewActivity extends AppCompatActivity {

    Button deleteBtn;
    private RecyclerView allOrderedItems;
    TableService tableService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_overview_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteBtn = findViewById(R.id.deleteItem);
        allOrderedItems = findViewById(R.id.allOrderedItems);
        tableService = new TableService(getSharedPreferences("TABLES", Context.MODE_PRIVATE));
        setupAllOrderedItems(allOrderedItems);
        System.out.println(tableService.getAllOrderItems());
    }

    //Kitchen Output
    private void setupAllOrderedItems(RecyclerView recyclerView) {
        OrderOverviewAdapter orderOverviewAdapter = new OrderOverviewAdapter(this, tableService.getAllOrderItems());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderOverviewAdapter);

        orderOverviewAdapter.setClickListener((v, position) -> onOrderedItemClick(v, orderOverviewAdapter.getItem(position)));
    }

    private void onOrderedItemClick(View v, OrderItem orderItem) {
        tableService.getAllOrderItems().clear();
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
