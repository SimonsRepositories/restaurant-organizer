package com.example.restaurantorganizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.adapter.MenuAdapter;
import com.example.restaurantorganizer.model.Seat;
import com.example.restaurantorganizer.service.TableService;

import static android.content.Context.MODE_PRIVATE;


public class ShakeDetectedDialog extends DialogFragment {
    private TableService tableService;
    private Seat selectedSeat;
    private MenuAdapter menuAdapter;
    private RecyclerView allItemsRecyclerView;

    public ShakeDetectedDialog(Seat selectedSeat, MenuAdapter menuAdapter, RecyclerView allItemsRecyclerView) {
        this.selectedSeat = selectedSeat;
        this.menuAdapter = menuAdapter;
        this.allItemsRecyclerView = allItemsRecyclerView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alerttitle);
        builder.setMessage(R.string.alertmsg)
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    //remove the order (all selected items) of this person
                    tableService = new TableService(getContext().getSharedPreferences("TABLES", MODE_PRIVATE));
                    selectedSeat.getOrderItems().clear();
                    menuAdapter.setOrderItems(selectedSeat.getOrderItems());
                    allItemsRecyclerView.setAdapter(menuAdapter);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    dialog.dismiss();
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
