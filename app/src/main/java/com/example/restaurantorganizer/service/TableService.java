package com.example.restaurantorganizer.service;

import android.content.SharedPreferences;

import com.example.restaurantorganizer.dummy.DummySeats;
import com.example.restaurantorganizer.model.OrderItem;
import com.example.restaurantorganizer.model.Seat;
import com.example.restaurantorganizer.model.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableService {

    public TableService(SharedPreferences sharedPreferences) {
        mPrefs = sharedPreferences;
        if (getTables().size() == 0) {
            tables = Arrays.asList(
                    new Table(1L, "Table 1", DummySeats.SEATS1),
                    new Table(2L, "Table 2", DummySeats.SEATS2),
                    new Table(3L, "Table 3", DummySeats.SEATS3),
                    new Table(4L, "Table 4", DummySeats.SEATS4)
            );
            writeTables();
        }
    }

    private SharedPreferences mPrefs;

    private List<Table> tables = new ArrayList<>();

    private void writeTables() {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tables);
        prefsEditor.putString("TABLES", json);
        prefsEditor.apply();
    }

    private void readTables() {
        String json = mPrefs.getString("TABLES", "[]");
        Type listType = new TypeToken<ArrayList<Table>>(){}.getType();
        tables = new Gson().fromJson(json, listType);
    }

    public Table getTableById(long id) {
        return getTables().stream().filter(table -> table.getId() == id).findFirst().orElse(null);
    }

    public void updateTable(Table updateTable) {
        Table table = getTableById(updateTable.getId());
        table.setName(updateTable.getName());
        table.setSeats(updateTable.getSeats());
        writeTables();
    }

    public void updateSeat(Seat updateSeat, long tableId) {
        Table table = getTableById(tableId);
        Seat seat = table.getSeats().stream().filter(s -> s.getId() == updateSeat.getId()).findFirst().orElse(null);
        seat.getOrderItems().clear();
        seat.getOrderItems().addAll(updateSeat.getOrderItems());
        writeTables();
    }

    public List<Table> getTables() {
        readTables();
        return tables;
    }

    public List<OrderItem> getAllOrderItems() {
        return getTables().stream()
                .flatMap(table -> table.getSeats().stream())
                .flatMap(seat -> seat.getOrderItems().stream()).collect(toList());
    }

}
