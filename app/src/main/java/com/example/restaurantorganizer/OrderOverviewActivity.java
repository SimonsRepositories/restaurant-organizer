package com.example.restaurantorganizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.adapter.OrderOverviewAdapter;
import com.example.restaurantorganizer.model.OrderItem;
import com.example.restaurantorganizer.service.TableService;

public class OrderOverviewActivity extends AppCompatActivity {

    private Button deleteBtn;
    private RecyclerView allOrderedItemsRecyclerView;
    private TableService tableService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_overview_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteBtn = findViewById(R.id.deleteItem);
        allOrderedItemsRecyclerView = findViewById(R.id.allOrderedItems);
        tableService = new TableService(getSharedPreferences("TABLES", Context.MODE_PRIVATE));
        setupAllOrderedItems(allOrderedItemsRecyclerView);
    }

    //Kitchen Output
    private void setupAllOrderedItems(RecyclerView recyclerView) {
        OrderOverviewAdapter orderOverviewAdapter = new OrderOverviewAdapter(this, tableService.getAllOrderItems());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderOverviewAdapter);

        orderOverviewAdapter.setClickListener((v, position) -> onOrderedItemClick(orderOverviewAdapter.getItem(position)));
    }

    private void onOrderedItemClick(OrderItem orderItem) {
        tableService.setChecked(orderItem.getId());
        setupAllOrderedItems(allOrderedItemsRecyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
