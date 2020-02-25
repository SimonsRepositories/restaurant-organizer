package com.example.restaurantorganizer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.model.Item;
import com.example.restaurantorganizer.model.Menutype;
import com.example.restaurantorganizer.model.OrderItem;
import com.example.restaurantorganizer.service.OrderService;

import java.util.Arrays;
import java.util.List;

public class MenuSelectionActivitiy extends AppCompatActivity {

    OrderService os = new OrderService(this);

    private final List<Item> pizzaItems = Arrays.asList(
            new Item(1, "Napoli", "Sardellen"),
            new Item(2, "Hawaii", "Ananas"),
            new Item(3, "New York Style", "Tomaten, Käse"),
            new Item(4, "Neapolitan", "Basilikum"),
            new Item(5, "Marinara", "Knoblauch"),
            new Item(6, "Pugliese", "Olives"),
            new Item(7, "Verenose", "Shinken"),
            new Item(8, "Romana", "capperi, anchovies"),
            new Item(9, "Focaccia", "Zwiebeln")
    );

    private final List<Item> fishItems = Arrays.asList(
            new Item(1, "Barramundi", "tasty"),
            new Item(2, "perch", "yum"),
            new Item(3, "Salmon", "fish"),
            new Item(4, "Herring", "healthy"),
            new Item(5, "Tuna", "fishy")
    );

    private final List<Item> meatItems = Arrays.asList(
            new Item(1, "Chicken", "meaty"),
            new Item(2, "Turkey", "meaty"),
            new Item(3, "Pork", "meaty"),
            new Item(4, "Goat", "meaty"),
            new Item(5, "Bison", "meaty"),
            new Item(5, "Lamb", "meaty")
    );

    private final List<Item> drinkItems = Arrays.asList(
            new Item(1, "Cola", "sugar"),
            new Item(2, "Sprite", "lemon")
    );

    private final List<Item> pastaItems = Arrays.asList(
            new Item(1, "Spaghetti", "Tomatensauce"),
            new Item(2, "Lasagne", "Käse"),
            new Item(3, "Macaroni", "Käse"),
            new Item(4, "Ravioli", "Gemüse"),
            new Item(5, "Rigatoni", "Gemüse"),
            new Item(6, "Tagliatelle", "Teig"),
            new Item(7, "Cannelloni", "Basilikum")
    );

    private final List<Menutype> menutypes = Arrays.asList(
            new Menutype(3, "Meat", meatItems),
            new Menutype(4, "Fish", fishItems),
            new Menutype(2, "Pasta", pastaItems),
            new Menutype(1, "Pizza", pizzaItems),
            new Menutype(5, "Drinks", drinkItems)
    );
    private RecyclerView allMenuTypesRecyclerView;
    private RecyclerView allItemsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_view);

        allMenuTypesRecyclerView = findViewById(R.id.allMenutypes);
        setupMenuTypeRecyclerView(allMenuTypesRecyclerView);

        allItemsRecyclerView = findViewById(R.id.allItems);
        setupItemRecyclerView(allItemsRecyclerView, pizzaItems);
    }

    private void setupMenuTypeRecyclerView(RecyclerView recyclerView) {
        MenutypeAdapter menutypeAdapter = new MenutypeAdapter(this, menutypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(menutypeAdapter);
        menutypeAdapter.setClickListener((v, position) -> onMenuTypeClick(v, menutypeAdapter.getItem(position)));
    }

    private void setupItemRecyclerView(RecyclerView recyclerView, List<Item> items) {
        ItemAdapter itemAdapter = new ItemAdapter(this, items);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.setClickListener((v, position) -> onItemClick(v, itemAdapter.getItem(position)));
    }

    private void onMenuTypeClick(View v, Menutype menutype) {

        switch (menutype.getName()) {
            case "Pizza":
                setupItemRecyclerView(allItemsRecyclerView, pizzaItems);
                break;
            case "Pasta":
                setupItemRecyclerView(allItemsRecyclerView, pastaItems);
                break;
            case "Meat":
                setupItemRecyclerView(allItemsRecyclerView, meatItems);
                break;
            case "Fish":
                setupItemRecyclerView(allItemsRecyclerView, fishItems);
                break;
            case "Drinks":
                setupItemRecyclerView(allItemsRecyclerView, drinkItems);
                break;
        }
    }

    private void onItemClick(View v, Item item) {
        int color = ((ColorDrawable)v.getBackground()).getColor();
        v.setBackgroundColor(Color.GREEN);
        OrderItem oi = new OrderItem(1, item, 2, false);
        os.addOrderItem(oi);

    }
}
