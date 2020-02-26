package com.example.restaurantorganizer;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.adapter.SeatAdapter;
import com.example.restaurantorganizer.model.Seat;
import com.example.restaurantorganizer.model.Table;
import com.example.restaurantorganizer.service.TableService;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tableHeader;

    private RecyclerView seatsLeftRecyclerView;
    private RecyclerView seatsRightRecyclerView;

    private Table scannedTable;
    private TableService tableService;

    private NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tableService = new TableService(getSharedPreferences("TABLES", MODE_PRIVATE));
        scannedTable = tableService.getTableById(1L);

        tableHeader = findViewById(R.id.tableHeader);
        tableHeader.setText(scannedTable.getName());

        seatsLeftRecyclerView = findViewById(R.id.seatsLeft);
        seatsRightRecyclerView = findViewById(R.id.seatsRight);
        List<Seat> seats = scannedTable.getSeats();
        setupSeatRecyclerView(seatsLeftRecyclerView, seats.subList(0, seats.size() / 2 + (seats.size() % 2))); // first half of seat list
        setupSeatRecyclerView(seatsRightRecyclerView, scannedTable.getSeats().subList(seats.size() / 2 + (seats.size() % 2), seats.size())); // second half of seat list

        //Setup NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(this, "No NDEF message found", Toast.LENGTH_LONG).show();
            }
        }

        super.onNewIntent(intent);
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            scannedTable = tableService.getTableById(Long.parseLong(tagContent));

        } else {
            Toast.makeText(this, "No NDEF records found", Toast.LENGTH_LONG).show();
        }
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdeRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);

        scannedTable = tableService.getTableById(1L);

        super.onResume();
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);

        super.onPause();
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
        seatAdapter.setClickListener((v, position) -> onSeatClick(seatAdapter.getItem(position)));
    }

    private void onSeatClick(Seat seat) {
        Intent intent = new Intent(this, MenuSelectionActivitiy.class);
        intent.putExtra("SEAT", scannedTable.getSeats().stream().filter(s -> s.getId() == seat.getId()).findFirst().orElse(null));
        intent.putExtra("TABLE_ID", scannedTable.getId());
        startActivity(intent);
    }
}
