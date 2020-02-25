package com.example.restaurantorganizer;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.adapter.MenuSelectionActivitiy;
import com.example.restaurantorganizer.adapter.SeatAdapter;
import com.example.restaurantorganizer.model.Seat;
import com.example.restaurantorganizer.model.Table;
import com.example.restaurantorganizer.service.TableService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tableHeader;

    private RecyclerView seatsLeftRecyclerView;
    private RecyclerView seatsRightRecyclerView;

    private final List<Seat> seats = Arrays.asList(new Seat(1L, new ArrayList<>()), new Seat(2L,  new ArrayList<>()), new Seat(3L,  new ArrayList<>()), new Seat(4L,  new ArrayList<>()), new Seat(5L,  new ArrayList<>()));

    private Table scannedTable;
    private TableService tableService;

    private NfcAdapter nfcAdapter;
    private Button scanButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("on create");

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tableService = new TableService(getSharedPreferences("TABLES", MODE_PRIVATE));
        scannedTable = tableService.getTableById(1L);

        System.out.println(scannedTable);

        tableHeader = findViewById(R.id.tableHeader);
        tableHeader.setText(scannedTable.getName());


        seatsLeftRecyclerView = findViewById(R.id.seatsLeft);
        seatsRightRecyclerView = findViewById(R.id.seatsRight);
        List<Seat> seats = scannedTable.getSeats();
        setupSeatRecyclerView(seatsLeftRecyclerView, seats.subList(0, seats.size() /2 + (seats.size()%2))); // first half of seat list
        setupSeatRecyclerView(seatsRightRecyclerView, scannedTable.getSeats().subList(seats.size() /2 + (seats.size()%2), seats.size())); // second half of seat list


        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v -> {
            System.out.println("click");
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        scannedTable = tableService.getTableById(1L);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("new Intent");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                }
                // Process the messages array.
                System.out.println(Arrays.toString(messages));
            }
        }


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
        Intent intent = new Intent(this, MenuSelectionActivitiy.class);
        intent.putExtra("SEAT", scannedTable.getSeats().stream().filter(s -> s.getId() == seat.getId()).findFirst().orElse(null));
        intent.putExtra("TABLE_ID", scannedTable.getId());
        startActivity(intent);
    }


}
