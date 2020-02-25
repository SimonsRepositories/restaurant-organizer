package com.example.restaurantorganizer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.adapter.MenuAdapter;
import com.example.restaurantorganizer.adapter.MenutypeAdapter;
import com.example.restaurantorganizer.model.Menu;
import com.example.restaurantorganizer.model.Menutype;
import com.example.restaurantorganizer.model.OrderItem;
import com.example.restaurantorganizer.model.Seat;
import com.example.restaurantorganizer.service.TableService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuSelectionActivitiy extends AppCompatActivity {

    TableService tableService;

    Seat selectedSeat;
    long tableId;

    private final List<Menutype> menutypes = Arrays.asList(
            new Menutype(1, "Meat", new ArrayList<>()),
            new Menutype(2, "Fish", new ArrayList<>()),
            new Menutype(3, "Pasta", new ArrayList<>()),
            new Menutype(4, "Pizza", new ArrayList<>()),
            new Menutype(5, "Drinks", new ArrayList<>())
    );

    private final List<Menu> pizzaItems = Arrays.asList(
            new Menu(6, "Napoli", "Sardellen"),
            new Menu(7, "Hawaii", "Ananas"),
            new Menu(8, "New York Style", "Tomaten, Käse"),
            new Menu(9, "Neapolitan", "Basilikum"),
            new Menu(10, "Marinara", "Knoblauch"),
            new Menu(11, "Pugliese", "Olives"),
            new Menu(12, "Verenose", "Shinken"),
            new Menu(13, "Romana", "capperi, anchovies"),
            new Menu(14, "Focaccia", "Zwiebeln")
    );

    private final List<Menu> fishItems = Arrays.asList(
            new Menu(15, "Barramundi", "tasty"),
            new Menu(16, "perch", "yum"),
            new Menu(17, "Salmon", "fish"),
            new Menu(18, "Herring", "healthy"),
            new Menu(19, "Tuna", "fishy")
    );

    private final List<Menu> meatItems = Arrays.asList(
            new Menu(20, "Chicken", "meaty"),
            new Menu(21, "Turkey", "meaty"),
            new Menu(22, "Pork", "meaty"),
            new Menu(23, "Goat", "meaty"),
            new Menu(24, "Bison", "meaty"),
            new Menu(25, "Lamb", "meaty")
    );

    private final List<Menu> drinkItems = Arrays.asList(
            new Menu(26, "Cola", "sugar"),
            new Menu(27, "Sprite", "lemon")
    );

    private final List<Menu> pastaItems = Arrays.asList(
            new Menu(28, "Spaghetti", "Tomatensauce"),
            new Menu(29, "Lasagne", "Käse"),
            new Menu(30, "Macaroni", "Käse"),
            new Menu(31, "Ravioli", "Gemüse"),
            new Menu(32, "Rigatoni", "Gemüse"),
            new Menu(33, "Tagliatelle", "Teig"),
            new Menu(34, "Cannelloni", "Basilikum")
    );

    private RecyclerView allMenuTypesRecyclerView;
    private RecyclerView allItemsRecyclerView;

    private MenutypeAdapter menutypeAdapter;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_view);

        menutypes.get(0).setMenus(pizzaItems);
        menutypes.get(1).setMenus(fishItems);
        menutypes.get(2).setMenus(meatItems);
        menutypes.get(3).setMenus(drinkItems);
        menutypes.get(4).setMenus(pastaItems);

        selectedSeat = (Seat) getIntent().getSerializableExtra("SEAT");
        tableId = getIntent().getLongExtra("TABLE_ID", 0);

        allMenuTypesRecyclerView = findViewById(R.id.allMenutypes);
        setupMenuTypeRecyclerView(allMenuTypesRecyclerView);

        allItemsRecyclerView = findViewById(R.id.allItems);
        setupItemRecyclerView(allItemsRecyclerView, pizzaItems);

        tableService = new TableService(getSharedPreferences("TABLES", Context.MODE_PRIVATE));

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedSeat = (Seat) getIntent().getSerializableExtra("SEAT");
        tableId = getIntent().getLongExtra("TABLE_ID", 0);

        allMenuTypesRecyclerView = findViewById(R.id.allMenutypes);
        setupMenuTypeRecyclerView(allMenuTypesRecyclerView);

        allItemsRecyclerView = findViewById(R.id.allItems);
        setupItemRecyclerView(allItemsRecyclerView, pizzaItems);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tableService.updateSeat(selectedSeat, tableId);
    }

    private void setupMenuTypeRecyclerView(RecyclerView recyclerView) {
        menutypeAdapter = new MenutypeAdapter(this, menutypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(menutypeAdapter);
        menutypeAdapter.setClickListener((v, position) -> onMenuTypeClick(v, menutypeAdapter.getItem(position)));
    }

    private void setupItemRecyclerView(RecyclerView recyclerView, List<Menu> menus) {
        menuAdapter = new MenuAdapter(this, menus, selectedSeat.getOrderItems());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(menuAdapter);
        menuAdapter.setClickListener((v, position) -> onItemClick(v, menuAdapter.getItem(position)));
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

    private void onItemClick(View v, Menu item) {
        selectedSeat.getOrderItems().add(new OrderItem(123, item, 1, false));
        menuAdapter.setOrderItems(selectedSeat.getOrderItems());
        allItemsRecyclerView.setAdapter(menuAdapter);
    }
}
