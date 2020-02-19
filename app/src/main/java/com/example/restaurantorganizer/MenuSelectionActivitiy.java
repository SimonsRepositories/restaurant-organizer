package com.example.restaurantorganizer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.model.Menutype;

import java.util.Arrays;
import java.util.List;

public class MenuSelectionActivitiy extends AppCompatActivity {

    private final List<Menutype> menutypes = Arrays.asList(new Menutype(1, "pizza"), new Menutype(2, "pasta"));
    private RecyclerView allMenuTypesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_view);

        allMenuTypesRecyclerView = findViewById(R.id.allMenutypes);
        setupSeatRecyclerView(allMenuTypesRecyclerView);
    }

    private void setupSeatRecyclerView(RecyclerView recyclerView) {
        MenutypeAdapter menutypeAdapter = new MenutypeAdapter(this, menutypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(menutypeAdapter);
    }
}
