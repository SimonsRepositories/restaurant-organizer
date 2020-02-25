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

import com.example.restaurantorganizer.adapter.MenuSelectionActivitiy;
import com.example.restaurantorganizer.service.TableService;

import static android.content.Context.MODE_PRIVATE;


public class ShakeDetectedDialog extends DialogFragment
{
    TableService tableService;

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
                        tableService.getAllOrderItems().clear();
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
