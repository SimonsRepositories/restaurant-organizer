package com.example.restaurantorganizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.adapter.MenuAdapter;
import com.example.restaurantorganizer.model.Seat;
import com.example.restaurantorganizer.service.TableService;

import static android.content.Context.MODE_PRIVATE;


public class ShakeDetectedDialog extends DialogFragment
{
    TableService tableService;
    Seat selectedSeat;
    MenuAdapter menuAdapter;
    RecyclerView allItemsRecyclerView;

    public ShakeDetectedDialog(Seat selectedSeat, MenuAdapter menuAdapter, RecyclerView allItemsRecyclerView) {
        this.selectedSeat = selectedSeat;
        this.menuAdapter = menuAdapter;
        this.allItemsRecyclerView = allItemsRecyclerView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alerttitle);
        builder.setMessage(R.string.alertmsg)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //remove the order (all selected items) of this person
                        tableService = new TableService(getContext().getSharedPreferences("TABLES", MODE_PRIVATE));
                        selectedSeat.getOrderItems().clear();
                        menuAdapter.setOrderItems(selectedSeat.getOrderItems());
                        allItemsRecyclerView.setAdapter(menuAdapter);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
