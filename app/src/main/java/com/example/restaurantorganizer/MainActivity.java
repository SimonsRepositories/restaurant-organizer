package com.example.restaurantorganizer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.model.Seat;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView seatsLeftRecyclerView;

    private final List<Seat> seats = Arrays.asList(new Seat(1), new Seat(2), new Seat(3));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        seatsLeftRecyclerView = findViewById(R.id.seatsLeft);
        SeatAdapter seatsLeftAdapter = new SeatAdapter(this, seats);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        seatsLeftRecyclerView.setLayoutManager(layoutManager);


        seatsLeftRecyclerView.setAdapter(seatsLeftAdapter);




    }

}
