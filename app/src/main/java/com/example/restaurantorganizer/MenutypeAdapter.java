package com.example.restaurantorganizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.model.Menutype;

import java.util.List;

public class MenutypeAdapter extends RecyclerView.Adapter<MenutypeAdapter.ViewHolder>
{
    private List<Menutype> mData;
    private LayoutInflater mInflater;
    private MenutypeAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    MenutypeAdapter(Context context, List<Menutype> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public MenutypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.menutype_view, parent, false);
        return new MenutypeAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MenutypeAdapter.ViewHolder holder, int position) {
        Menutype menuType = mData.get(position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View menuTypeView;

        ViewHolder(View itemView) {
            super(itemView);
            menuTypeView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Menutype getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(MenutypeAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
