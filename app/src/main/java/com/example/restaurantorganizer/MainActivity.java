package com.example.restaurantorganizer;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.adapter.SeatAdapter;
import com.example.restaurantorganizer.model.Seat;
import com.example.restaurantorganizer.model.Table;
import com.example.restaurantorganizer.service.TableService;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tableHeader;

    private RecyclerView seatsLeftRecyclerView;
    private RecyclerView seatsRightRecyclerView;

    ToggleButton tglReadWrite;
    EditText txtTagContent;

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

        //Setup NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        tglReadWrite = (ToggleButton)findViewById(R.id.tglReadWrite);
        txtTagContent = (EditText) findViewById(R.id.txtTagContent);

        if(nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC available", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFC not available :(", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(nfcAdapter != null)
        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v -> {
            System.out.println("click");
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Toast.makeText(this, "NFC intent received!", Toast.LENGTH_LONG).show();

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

        if(tglReadWrite.isChecked()) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(parcelables != null && parcelables.length > 0){
                readTextFromMessage((NdefMessage)parcelables[0]);
            } else {
                Toast.makeText(this, "No NDEF message found", Toast.LENGTH_LONG).show();
            }
        } else {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage(txtTagContent.getText() + "");

            writeNdefMessage(tag, ndefMessage);
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            txtTagContent.setText(tagContent);

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

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if(ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();
                return;
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag written", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if(tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if(ndef == null) {
                //format tag with the ndef format and writes the message .
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();

                if(!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writable", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "Tag written", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }
    }


    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        } catch (UnsupportedEncodingException e){
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    private NdefMessage createNdefMessage(String content) {
        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }

    public void tglReadWriteOnClick(View view) {
        txtTagContent.setText("");
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
        Intent intent = new Intent(this, MenuSelectionActivitiy.class);
        intent.putExtra("SEAT", scannedTable.getSeats().stream().filter(s -> s.getId() == seat.getId()).findFirst().orElse(null));
        intent.putExtra("TABLE_ID", scannedTable.getId());
        startActivity(intent);
    }
}
