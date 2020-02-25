package com.example.restaurantorganizer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.R;
import com.example.restaurantorganizer.model.Menu;
import com.example.restaurantorganizer.model.OrderItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>
{
    private List<Menu> mData;
    private List<OrderItem> selectedOrderItems;
    private LayoutInflater mInflater;
    private MenuAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    MenuAdapter(Context context, List<Menu> data, List<OrderItem> orderItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.selectedOrderItems = orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.selectedOrderItems = orderItems;
    }

    // inflates the row layout from xml when needed
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_view, parent, false);
        return new MenuAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {
        Menu item = mData.get(position);

        holder.tv_item.setText(mData.get(position).getName());
        holder.tv_ingredients.setText(mData.get(position).getIngredients());
        if (selectedOrderItems.stream().map(OrderItem::getMenu).collect(toList())
                .stream().anyMatch(menu -> menu.getId() == item.getId())) {
            holder.myitemView.setBackgroundColor(Color.GREEN);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View myitemView;

        TextView tv_item;
        TextView tv_ingredients;

        ViewHolder(View itemView) {
            super(itemView);
            myitemView = itemView;

            tv_item = itemView.findViewById(R.id.tv_item);
            tv_ingredients = itemView.findViewById(R.id.tv_ingredients);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Menu getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(MenuAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
