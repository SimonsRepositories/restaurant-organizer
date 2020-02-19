package com.example.restaurantorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.model.Seat;
import com.example.restaurantorganizer.model.Table;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tableHeader;

    private RecyclerView seatsLeftRecyclerView;
    private RecyclerView seatsRightRecyclerView;

    private final List<Seat> seats = Arrays.asList(new Seat(1L), new Seat(2L), new Seat(3L), new Seat(4L), new Seat(5L));

    private final Table scannedTable = new Table(1L, "Tisch 1", seats);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tableHeader = findViewById(R.id.tableHeader);
        tableHeader.setText(scannedTable.getName());


        seatsLeftRecyclerView = findViewById(R.id.seatsLeft);
        seatsRightRecyclerView = findViewById(R.id.seatsRight);
        List<Seat> seats = scannedTable.getSeats();
        setupSeatRecyclerView(seatsLeftRecyclerView, seats.subList(0, seats.size() /2 + (seats.size()%2))); // first half of seat list
        setupSeatRecyclerView(seatsRightRecyclerView, scannedTable.getSeats().subList(seats.size() /2 + (seats.size()%2), seats.size())); // second half of seat list

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ordersNav:
                Intent intent = new Intent(this, OrderOverviewActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSeatRecyclerView(RecyclerView recyclerView, List<Seat> seats) {
        SeatAdapter seatAdapter = new SeatAdapter(this, seats);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(seatAdapter);
        seatAdapter.setClickListener((v, position) -> onSeatClick(v, seatAdapter.getItem(position)));
    }

    private void onSeatClick(View v, Seat seat) {
        seat.getId();
    }

}
